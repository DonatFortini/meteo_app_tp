package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.source.IGeocodingDataSource

class GeocodingRepository(private val geocodingDataSource: IGeocodingDataSource) {
    suspend fun getClosestCityName(latitude: Double, longitude: Double): CityResponse {
        return geocodingDataSource.getClosestCityName(latitude, longitude)
    }
}
