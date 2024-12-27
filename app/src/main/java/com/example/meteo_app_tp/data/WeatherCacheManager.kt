package com.example.meteo_app_tp.data

import android.content.SharedPreferences
import com.example.meteo_app_tp.data.model.WeatherCache
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class WeatherCacheManager(private val sharedPreferences: SharedPreferences) {
    private val mutex = Mutex()
    private val gson = Gson()
    private val cacheValidityDuration = TimeUnit.HOURS.toMillis(1)

    suspend fun getCachedWeather(lat: String, long: String): WeatherCache? {
        return mutex.withLock {
            val cacheJson = sharedPreferences.getString(getCacheKey(lat, long), null)
            cacheJson?.let {
                try {
                    gson.fromJson(it, WeatherCache::class.java)?.takeIf { cache ->
                        isCacheValid(cache) && isLocationMatch(cache, lat, long)
                    }
                } catch (e: Exception) {
                    clearCache(lat, long)
                    null
                }
            }
        }
    }

    suspend fun saveToCache(forecasts: List<WeatherForecast>, lat: String, lon: String, cityName: String) {
        mutex.withLock {
            try {
                val cache = WeatherCache(
                    forecasts = forecasts,
                    latitude = lat,
                    longitude = lon,
                    cityName = cityName,
                    timestamp = System.currentTimeMillis()
                )
                val cacheJson = gson.toJson(cache)
                sharedPreferences.edit()
                    .putString(getCacheKey(lat, lon), cacheJson)
                    .apply()
            } catch (e: Exception) {
                // Handle serialization error if needed
            }
        }
    }

    private fun clearCache(lat: String, long: String) {
        sharedPreferences.edit().remove(getCacheKey(lat, long)).apply()
    }

    private fun getCacheKey(lat: String, long: String): String = "weather_cache_${lat}_${long}"

    private fun isCacheValid(cache: WeatherCache): Boolean =
        System.currentTimeMillis() - cache.timestamp < cacheValidityDuration

    private fun isLocationMatch(cache: WeatherCache, lat: String, long: String): Boolean {
        return try {
            val latDiff = abs(cache.latitude.toDouble() - lat.toDouble())
            val lonDiff = abs(cache.longitude.toDouble() - long.toDouble())
            latDiff < 0.0001 && lonDiff < 0.0001
        } catch (e: NumberFormatException) {
            false
        }
    }

    companion object {
        const val PREFS_NAME = "weather_cache_prefs"
    }
}