package com.example.meteo_app_tp.data.model

sealed class WeatherResponse<out T> {
    data class Success<out R>(val value:R):WeatherResponse<R>()
    data class Error(val errorMessage:String):WeatherResponse<Nothing>()
}

