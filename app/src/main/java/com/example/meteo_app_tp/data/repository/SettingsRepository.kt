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
        val DEFAULT_CITY_LAT = stringPreferencesKey("default_city_lat")
        val DEFAULT_CITY_LON = stringPreferencesKey("default_city_lon")
        val DEFAULT_CITY_NAME = stringPreferencesKey("default_city_name")
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

    fun getDefaultCity(): Flow<Triple<String?, String?, String?>> {
        return dataStore.data.map { preferences ->
            Triple(
                preferences[PreferencesKeys.DEFAULT_CITY_LAT],
                preferences[PreferencesKeys.DEFAULT_CITY_LON],
                preferences[PreferencesKeys.DEFAULT_CITY_NAME]
            )
        }
    }

    suspend fun setDefaultCity(lat: String, lon: String, cityName: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_CITY_LAT] = lat
            preferences[PreferencesKeys.DEFAULT_CITY_LON] = lon
            preferences[PreferencesKeys.DEFAULT_CITY_NAME] = cityName
        }
    }
}