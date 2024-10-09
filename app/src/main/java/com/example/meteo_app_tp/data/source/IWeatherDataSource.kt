package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.model.WeatherForecast

interface IWeatherDataSource {
    suspend fun getWeatherData(lat:String,long:String): WeatherResponse<List<WeatherForecast>>
}