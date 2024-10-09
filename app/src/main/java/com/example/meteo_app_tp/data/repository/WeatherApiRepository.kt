package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.source.IWeatherDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherApiRepository(private val dataSource: IWeatherDataSource) : IWeatherRepository {
    override suspend fun getWeatherData(lat:String,long:String): Flow<List<WeatherForecast>> = flow {
        when (val response = dataSource.getWeatherData(lat,long)) {
            is WeatherResponse.Success -> {
                emit(response.value)
            }
            is WeatherResponse.Error -> {
                throw Exception(response.errorMessage)
            }
        }
    }
}
