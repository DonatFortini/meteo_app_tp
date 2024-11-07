package com.example.meteo_app_tp.ui.homescreen

import com.example.meteo_app_tp.data.model.WeatherForecast
//todo fix connected + add a function
data class HomeScreenState(
    val isLoading: Boolean = false,
    val weatherForecasts: List<WeatherForecast>? = null,
    val errorMessage: String? = null,
    val city: String = "city_placeholder",
    val isConnected : Boolean = true
)
