package com.example.meteo_app_tp.ui.settings.utils

enum class Language(val code: String) {
    ENGLISH("en"),
    FRENCH("fr"),
    ARABIC("ar");

    fun getDisplayName(): String = when (this) {
        ENGLISH -> "English"
        FRENCH -> "Français"
        ARABIC -> "العربية"
    }

    companion object {
        fun fromCode(code: String): Language {
            return Language.entries.find { it.code == code } ?: ENGLISH
        }
    }
}