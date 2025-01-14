package com.example.meteo_app_tp.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AppSettings(
    val language: String = "en",
    val isDarkMode: Boolean = false
)

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(
        AppSettings(
            language = prefs.getString("language", "en") ?: "en",
            isDarkMode = prefs.getBoolean("isDarkMode", false)
        )
    )
    val settingsFlow: StateFlow<AppSettings> = _settingsFlow.asStateFlow()

    fun updateSettings(
        language: String = _settingsFlow.value.language,
        isDarkMode: Boolean = _settingsFlow.value.isDarkMode
    ) {
        prefs.edit().apply {
            putString("language", language)
            putBoolean("isDarkMode", isDarkMode)
        }.apply()

        _settingsFlow.update {
            AppSettings(
                language = language,
                isDarkMode = isDarkMode
            )
        }
    }
}