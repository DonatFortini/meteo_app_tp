package com.example.meteo_app_tp.ui.states

import com.example.meteo_app_tp.data.local.entities.WeatherForecast

data class WeatherState(
    val forecasts: List<WeatherForecast> = emptyList(),
    val cityName: String = "Loading...",
    val error: String? = null,
    val isLoading: Boolean = false
) {
    val currentWeather: WeatherForecast?
        get() = forecasts.firstOrNull()
}