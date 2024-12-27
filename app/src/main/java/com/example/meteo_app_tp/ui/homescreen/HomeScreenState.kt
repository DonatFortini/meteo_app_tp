package com.example.meteo_app_tp.ui.homescreen

import com.example.meteo_app_tp.data.model.WeatherForecast

data class HomeScreenState(
    val isLoading: Boolean = false,
    val weatherForecasts: List<WeatherForecast>? = null,
    val errorMessage: String? = null,
    val city: String = "Unknown City",
    val isConnected: Boolean = true,
    val isRefreshing: Boolean = false,
)

enum class DataSource {
    CACHE,
    API,
}