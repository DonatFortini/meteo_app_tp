package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.WeatherCacheManager
import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.model.WeatherForecast

class WeatherCacheDataSource(private val cacheManager: WeatherCacheManager) : IWeatherDataSource {
    override suspend fun getWeatherData(lat: String, long: String): WeatherResponse<List<WeatherForecast>> {
        return try {
            val cache = cacheManager.getCachedWeather(lat, long)
            if (cache != null) {
                WeatherResponse.Success(cache.forecasts)
            } else {
                WeatherResponse.Error("No cached data available")
            }
        } catch (e: Exception) {
            WeatherResponse.Error("Error reading cache: ${e.message}")
        }
    }

    suspend fun saveToCache(forecasts: List<WeatherForecast>, lat: String, lon: String, cityName: String) {
        cacheManager.saveToCache(forecasts, lat, lon, cityName)
    }
}