package com.pashteut.todoapp.model

import android.content.Context
import com.pashteut.todoapp.BuildConfig
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.model.api.TodoApi
import com.pashteut.todoapp.model.database.ToDoItemDao
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

/**
 * Repository for managing ToDo items, including CRUD operations and synchronization with a remote server.
 *
 * This repository is responsible for handling all data operations related to ToDo items, such as fetching from and saving to a local database,
 * as well as pushing changes to and fetching updates from a remote server. It utilizes a combination of Room database for local storage
 * and a REST API for remote interactions. Additionally, it manages OAuth token for authentication with the remote server and handles
 * synchronization logic to ensure data consistency between the local and remote sources.
 *
 * @property database Instance of [ToDoItemDao] for local database access.
 * @property appDispatchers Provides Coroutine Dispatchers for background operations.
 * @property api Instance of [TodoApi] for remote server interactions.
 * @property dataStore Instance of [MyDataStore] for storing OAuth token and other preferences.
 * @property context Android context, used for various operations requiring context access.
 */

class TodoItemsRepository @Inject constructor(
    private val database: ToDoItemDao,
    private val appDispatchers: AppDispatchers,
    private val api: TodoApi,
    private val dataStore: MyDataStore,
    @ApplicationContext private val context: Context,
) {

    fun getAllItems() = database.observeAll()

    private var lastKnownRevisionFlow: Flow<Int> = dataStore.lastKnownRevisionFlow

    private val oauthToken = dataStore.oauthToken

    init {
        CoroutineScope(appDispatchers.io).launch {
            val token = oauthToken.first()
            api.setOAuthToken(token)
            if (dataStore.lastUpdateTime.first() == 0L)
                dataStore.setLastUpdateTime(System.currentTimeMillis())
        }
    }

    suspend fun setOAuthToken(token: String): Boolean {
        api.setOAuthToken("OAuth $token")
        try {
            fetchRepository()
            dataStore.setOAuthToken("OAuth $token")
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                val oldToken = oauthToken.first()
                api.setOAuthToken(oldToken)
                return false
            }
        }
        return true
    }

    suspend fun logout() {
        BuildConfig.DEFAULT_APITOKEN.let {
            dataStore.setOAuthToken(it)
            api.setOAuthToken(it)
        }
    }

    suspend fun syncChanges() {
        val lastUpdateTime = dataStore.lastUpdateTime.first()
        val all = database.getAll()
        val changes = all.filter {
            if (it.updatedDate != null)
                it.updatedDate > lastUpdateTime
            else
                it.createdDate > lastUpdateTime
        }
        fetchRepository()
        changes.forEach {
            if (it.createdDate > lastUpdateTime)
                saveItem(it)
            else
                updateItem(it)
        }
    }

    private suspend fun fetchRepository(noDeleteId: String? = null, noSaveId: String? = null) =
        withContext(appDispatchers.io) {
            val response = api.getList()

            val apiItems = response.list.map { it.toToDoItem() }.associateBy { it.id }
            val localItems = database.getAll().associateBy { it.id }
            val itemsToDelete = localItems.keys - apiItems.keys

            itemsToDelete.forEach { id ->
                if (id != noDeleteId)
                    database.delete(id)
            }

            apiItems.values.forEach { item ->
                if (item.id != noSaveId)
                    database.insert(item)
            }
            setRevision(response.revision)
        }

    private suspend fun setRevision(revision: Int) {
        dataStore.setLastKnownRevision(revision)
        dataStore.setLastUpdateTime(System.currentTimeMillis())
    }

    suspend fun getItem(id: String): Flow<ToDoItem?> = flow {
        val cachedItem = withContext(appDispatchers.io) { database.getById(id) }
        emit(cachedItem)
        try {
            val backItem =
                withContext(appDispatchers.io) { api.getElementById(id).element.toToDoItem() }
            cachedItem?.let {
                if (backItem.updatedDate!! > (cachedItem.updatedDate
                        ?: cachedItem.createdDate)
                ) {
                    withContext(appDispatchers.io) { database.update(backItem) }
                    emit(backItem)
                }
            }
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound)
                cachedItem?.let { _saveItem(it, saveToCache = false) }
        }
    }

    private suspend fun _saveItem(item: ToDoItem, saveToCache: Boolean = true) =
        withContext(appDispatchers.io) {
            val newItem = item.copy(id = UUID.randomUUID().toString())
            if (saveToCache)
                database.insert(newItem)
            fetchRepository(noDeleteId = newItem.id)

            val responseBody = api.addElement(
                element = newItem
                    .toElementDTO()
                    .copy(lastUpdatedBy = getDeviceId(context)),
                lastKnownRevision = lastKnownRevisionFlow.first()
            )
            setRevision(responseBody.revision)
        }

    suspend fun saveItem(item: ToDoItem) = _saveItem(item)

    suspend fun updateItem(item: ToDoItem) =
        withContext(appDispatchers.io) {
            database.update(item)
            fetchRepository(noSaveId = item.id, noDeleteId = item.id)
            try {
                val responseBody = api.changeElement(
                    element = item
                        .toElementDTO()
                        .copy(lastUpdatedBy = getDeviceId(context)),
                    lastKnownRevision = lastKnownRevisionFlow.first()
                )
                setRevision(responseBody.revision)
            } catch (e: ClientRequestException) {
                if (e.response.status == HttpStatusCode.NotFound)
                    _saveItem(item, saveToCache = false)
                throw e
            }
        }

    suspend fun deleteItem(id: String) =
        withContext(appDispatchers.io) {
            database.delete(id)
            fetchRepository(noSaveId = id)
            val responseBody = api.deleteElementById(
                id = id,
                lastKnownRevision = lastKnownRevisionFlow.first()
            )
            setRevision(responseBody.revision)
        }

    suspend fun changeIsDone(id: String) =
        withContext(appDispatchers.io) {
            database.getById(id)?.let { item ->
                val newItem = item.copy(isDone = !item.isDone, updatedDate = System.currentTimeMillis())
                updateItem(newItem)
            }
        }
}