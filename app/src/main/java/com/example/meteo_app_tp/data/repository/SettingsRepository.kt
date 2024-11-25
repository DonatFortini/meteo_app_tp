package com.example.meteo_app_tp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.meteo_app_tp.ui.settings.utils.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("language")
    }

    fun getCurrentLanguage(): Flow<Language> {
        return dataStore.data.map { preferences ->
            val languageCode = preferences[PreferencesKeys.LANGUAGE] ?: Language.ENGLISH.code
            Language.entries.find { it.code == languageCode } ?: Language.ENGLISH
        }
    }

    suspend fun setLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.code
        }
    }
}