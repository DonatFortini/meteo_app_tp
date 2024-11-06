package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.CityResponse

interface IGeocodingDataSource {
    suspend fun getClosestCityName(latitude: Double, longitude: Double): CityResponse
}