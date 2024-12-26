package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.WeatherCache
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class WeatherCacheDataSource : IWeatherDataSource {
    private var cache: WeatherCache? = null
    private val cacheMutex = Mutex()
    private val cacheValidityDuration = TimeUnit.HOURS.toMillis(1)

    override suspend fun getWeatherData(lat: String, long: String): WeatherResponse<List<WeatherForecast>> {
        return cacheMutex.withLock {
            val currentCache = cache
            when {
                currentCache == null -> {
                    WeatherResponse.Error("No cached data available")
                }
                !isCacheValid(currentCache) -> {
                    cache = null
                    WeatherResponse.Error("Cache expired")
                }
                !isLocationMatch(currentCache, lat, long) -> {
                    WeatherResponse.Error("Location mismatch")
                }
                else -> {
                    WeatherResponse.Success(currentCache.forecasts)
                }
            }
        }
    }

    suspend fun saveToCache(forecasts: List<WeatherForecast>, lat: String, lon: String) {
        cacheMutex.withLock {
            cache = WeatherCache(
                forecasts = forecasts,
                latitude = lat,
                longitude = lon,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    private fun isCacheValid(cache: WeatherCache): Boolean {
        val age = System.currentTimeMillis() - cache.timestamp
        return age < cacheValidityDuration
    }

    private fun isLocationMatch(cache: WeatherCache, lat: String, long: String): Boolean {
        return try {
            val latDiff = abs(cache.latitude.toDouble() - lat.toDouble())
            val lonDiff = abs(cache.longitude.toDouble() - long.toDouble())
            latDiff < 0.0001 && lonDiff < 0.0001
        } catch (e: NumberFormatException) {
            false
        }
    }
}