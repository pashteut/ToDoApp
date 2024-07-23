package com.pashteut.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.ui_kit.theme_state.ThemeState
import com.pashteut.todoapp.ui_kit.theme_state.ThemeStateStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val themeStateStore: ThemeStateStore,
) : ViewModel() {
    val themeState = themeStateStore.themeState
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeState.System)
}