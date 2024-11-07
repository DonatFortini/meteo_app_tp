package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.WeatherForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherForecastRepository(
    private val weatherApiRepository: WeatherApiRepository,
    private val weatherCacheRepository: WeatherCacheRepository,
    private val isConnected : Boolean
) : IWeatherRepository {

    override suspend fun getWeatherData(lat: String, lon: String): Flow<List<WeatherForecast>> = flow {
        if (isConnected) {
            try {
                weatherApiRepository.getWeatherData(lat, lon).collect { forecasts ->
                    weatherCacheRepository.cacheData(forecasts, lat, lon)
                    emit(forecasts)
                }
            } catch (e: Exception) {
                 weatherCacheRepository.getWeatherData(lat, lon).collect { cachedData ->
                    if (cachedData.isNotEmpty()) {
                        emit(cachedData)
                    } else {
                        emit(emptyList())
                    }
                }
            }
        } else {
            weatherCacheRepository.getWeatherData(lat, lon).collect { cachedData ->
                if (cachedData.isNotEmpty()) {
                    emit(cachedData)
                } else {
                    emit(emptyList())
                }
            }
        }
    }
}
