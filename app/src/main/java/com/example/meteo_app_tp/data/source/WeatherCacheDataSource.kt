package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse

class WeatherCacheDataSource: IWeatherDataSource {
    override suspend fun getWeatherData(
        lat: String,
        long: String
    ): WeatherResponse<List<WeatherForecast>> {
        TODO("Not yet implemented")
    }
}