package com.pashteut.todoapp.features.todo_settings.ui_logic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.ui_kit.theme_state.ThemeState
import com.pashteut.todoapp.ui_kit.theme_state.ThemeStateStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val themeStateStore: ThemeStateStore
) : ViewModel() {
    val themeState = themeStateStore.themeState
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeState.System)

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("MainScreenViewModel", "Coroutine exception", exception)
    }
    private val scope = viewModelScope + handler

    fun setThemeState(themeState: ThemeState) =
        scope.launch {
            themeStateStore.setThemeState(themeState)
        }

}