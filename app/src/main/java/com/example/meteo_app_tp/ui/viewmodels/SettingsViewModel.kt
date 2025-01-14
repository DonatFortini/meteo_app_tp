package com.example.meteo_app_tp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.MainActivity
import com.example.meteo_app_tp.data.local.SettingsManager
import com.example.meteo_app_tp.ui.states.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init {
        viewModelScope.launch {
            settingsManager.settingsFlow.collect { settings ->
                _state.emit(
                    SettingsState(
                        language = settings.language,
                        isDarkMode = settings.isDarkMode
                    )
                )
            }
        }
    }

    fun updateLanguage(language: String) {
        settingsManager.updateSettings(language = language)
    }

    fun toggleTheme() {
        settingsManager.updateSettings(isDarkMode = !state.value.isDarkMode)
    }
}