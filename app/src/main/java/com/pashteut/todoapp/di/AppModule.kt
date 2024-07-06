package com.pashteut.todoapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.common.UIMessagesStorage
import com.pashteut.todoapp.model.MyDataStore
import com.pashteut.todoapp.model.api.TodoApi
import com.pashteut.todoapp.model.database.ToDoItemDao
import com.pashteut.todoapp.model.database.ToDoItemsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Provides application-wide dependencies for the ToDo app.
 *
 * This object module uses Dagger Hilt to provide dependencies such as the database DAO, network client, and various utilities
 * across the application. It ensures that instances such as the ToDoItemDao, HttpClient, and DataStore are singleton and
 * available application-wide, facilitating dependency injection and reducing boilerplate code for initializing these instances.
 *
 * @see ToDoItemDao for accessing the local database.
 * @see HttpClient for making network requests.
 * @see DataStore for storing key-value pairs persistently.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTodoItemsDao(@ApplicationContext context: Context): ToDoItemDao =
        ToDoItemsDatabase.getInstance(context).toDoItemDao()

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideNetworkClient(): HttpClient =
        HttpClient(Android) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = false
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    explicitNulls = false
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpRequestRetry) {
                retryOnException(maxRetries = 2)
                exponentialDelay()
                retryOnExceptionIf { _, throwable ->
                    when (throwable) {
                        is ClientRequestException -> {
                            !(throwable.response.status == HttpStatusCode.Unauthorized ||
                                    throwable.response.status == HttpStatusCode.NotFound)
                        }
                        else -> true
                    }
                }
            }
        }

    @Provides
    @Singleton
    fun provideApi(client: HttpClient, dispatchers: AppDispatchers): TodoApi =
        TodoApi(client, dispatchers)

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun provideDataStore(dataStore: DataStore<Preferences>, appDispatchers: AppDispatchers) =
        MyDataStore(dataStore, appDispatchers)

    @Singleton
    @Provides
    fun provideUIError(@ApplicationContext context: Context) = UIMessagesStorage(context)

}