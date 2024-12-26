package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    fun getWeatherData(lat: String, long: String): Flow<Result<Pair<String, List<WeatherForecast>>>>
}