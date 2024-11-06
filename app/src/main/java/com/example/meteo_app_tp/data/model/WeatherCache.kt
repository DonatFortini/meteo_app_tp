package com.example.meteo_app_tp.data.model

data class WeatherCache(
    val forecasts: List<WeatherForecast>,
    val latitude: String,
    val longitude: String,
    val timestamp: Long
)
