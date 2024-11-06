package com.example.meteo_app_tp.data.model

sealed class CityResponse {
    data class Success(val cityName: String) : CityResponse()
    data class Error(val message: String) : CityResponse()
}