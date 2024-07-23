package com.pashteut.todoapp.ui_kit.theme_state

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pashteut.todoapp.common.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThemeStateStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appDispatchers: AppDispatchers,
) {
    private val THEME_STATE = stringPreferencesKey("theme_state")
    val themeState: Flow<ThemeState> = dataStore.data
        .map { preferences ->
            preferences[THEME_STATE]?.let{
                ThemeState.valueOf(it)
            } ?: ThemeState.System
        }

    suspend fun setThemeState(themeState: ThemeState) {
        withContext(appDispatchers.io) {
            dataStore.edit { settings ->
                settings[THEME_STATE] = themeState.name
            }
        }
    }
}