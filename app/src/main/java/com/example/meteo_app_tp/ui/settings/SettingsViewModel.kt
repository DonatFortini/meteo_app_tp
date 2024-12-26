package com.example.meteo_app_tp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.data.repository.SettingsRepository
import com.example.meteo_app_tp.ui.settings.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    private val _isLanguageUpdated = MutableStateFlow(false)
    val isLanguageUpdated: StateFlow<Boolean> = _isLanguageUpdated.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getCurrentLanguage().collect { language ->
                _settingsState.value = SettingsState(currentLanguage = language)
            }
        }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
            _isLanguageUpdated.value = true // Indicate update is done
        }
    }

    fun resetLanguageUpdateFlag() {
        _isLanguageUpdated.value = false
    }
}