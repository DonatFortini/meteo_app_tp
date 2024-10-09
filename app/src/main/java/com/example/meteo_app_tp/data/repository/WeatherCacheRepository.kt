package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

class WeatherCacheRepository:IWeatherRepository {
    override suspend fun getWeatherData(lat: String, long: String): Flow<List<WeatherForecast>> {
        TODO("Not yet implemented")
    }

}