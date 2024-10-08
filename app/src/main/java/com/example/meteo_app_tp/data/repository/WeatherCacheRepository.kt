package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

class WeatherCacheRepository:IWeatherRepository {
    override suspend fun getWeatherData(): Flow<WeatherForecast> {
        TODO("Not yet implemented")
    }
}