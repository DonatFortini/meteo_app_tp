package com.example.meteo_app_tp.data.model

import com.example.meteo_app_tp.ui.homescreen.DataSource

data class WeatherData(
    val cityName: String,
    val forecasts: List<WeatherForecast>,
    val source: DataSource
)

sealed class WeatherDataState {
    object Loading : WeatherDataState()
    data class Success(val data: WeatherData) : WeatherDataState()
    data class Error(val message: String) : WeatherDataState()
}