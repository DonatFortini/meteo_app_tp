package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.source.WeatherCacheDataSource
import kotlinx.coroutines.flow.Flow

class WeatherCacheRepository(private val weatherCacheDataSource: WeatherCacheDataSource) : IWeatherRepository {

    fun cacheData(forecasts: List<WeatherForecast>, lat: String, lon: String) {
        weatherCacheDataSource.saveToCache(forecasts, lat, lon)
    }

    override suspend fun getWeatherData(
        lat: String,
        lon: String
    ): Flow<List<WeatherForecast>> {
        val response = weatherCacheDataSource.getWeatherData(lat, lon)
        return when (response) {
            is WeatherResponse.Success -> response.value
            is WeatherResponse.Error -> emptyList()
        } as Flow<List<WeatherForecast>>
    }
}
