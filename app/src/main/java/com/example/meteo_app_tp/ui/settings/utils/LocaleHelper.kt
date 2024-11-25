package com.example.meteo_app_tp.ui.settings.utils

import android.content.Context
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = when (languageCode) {
            "ar" -> Locale("ar")
            "fr" -> Locale("fr")
            else -> Locale("en")
        }

        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}