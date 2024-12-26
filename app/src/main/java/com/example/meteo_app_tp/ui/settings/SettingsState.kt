package com.example.meteo_app_tp.ui.settings

import com.example.meteo_app_tp.ui.settings.utils.Language

data class SettingsState(
    val currentLanguage: Language = Language.ENGLISH
)