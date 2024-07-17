package com.pashteut.todoapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pashteut.todoapp.BuildConfig
import com.pashteut.todoapp.common.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Manages application preferences and settings using DataStore.
 *
 * This class provides an abstraction layer over the DataStore for storing and retrieving application-specific preferences such as OAuth tokens,
 * the last known revision of data synchronized with a remote server, and the timestamp of the last update. It utilizes Kotlin Coroutines
 * for asynchronous operations and ensures thread-safe access to the DataStore.
 *
 * @property dataStore The DataStore instance used for persisting preferences.
 * @property appDispatchers Provides Coroutine Dispatchers for executing operations in the appropriate threads.
 */

class MyDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appDispatchers: AppDispatchers,
) {
    private val OAUTH_TOKEN = stringPreferencesKey("oauth-token")
    val oauthToken: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[OAUTH_TOKEN] ?: BuildConfig.DEFAULT_APITOKEN
        }

    private val LAST_KNOWN_REVISION = intPreferencesKey("last_known_revision")
    val lastKnownRevisionFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[LAST_KNOWN_REVISION] ?: 0
        }

    private val LAST_UPDATE_TIME = longPreferencesKey("last_update_time")
    val lastUpdateTime: Flow<Long> = dataStore.data
        .map { preferences ->
            preferences[LAST_UPDATE_TIME] ?: 0
        }

    suspend fun setOAuthToken(token: String?) {
        withContext(appDispatchers.io) {
            dataStore.edit { settings ->
                settings[OAUTH_TOKEN] = token ?: BuildConfig.DEFAULT_APITOKEN
            }
        }
    }

    suspend fun setLastKnownRevision(revision: Int) {
        withContext(appDispatchers.io) {
            dataStore.edit { settings ->
                settings[LAST_KNOWN_REVISION] = revision
            }
        }
    }

    suspend fun setLastUpdateTime(time: Long) {
        withContext(appDispatchers.io) {
            dataStore.edit { settings ->
                settings[LAST_UPDATE_TIME] = time
            }
        }
    }
}