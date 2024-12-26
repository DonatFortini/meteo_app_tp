package com.example.meteo_app_tp.data.repository

import android.util.Log
import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.model.CitySearchResult
import com.example.meteo_app_tp.data.source.IGeocodingDataSource

class GeocodingRepository(private val geocodingDataSource: IGeocodingDataSource) {
    suspend fun getClosestCityName(latitude: Double, longitude: Double): CityResponse {
        return geocodingDataSource.getClosestCityName(latitude, longitude)
    }

    suspend fun searchCities(cityName: String): List<CitySearchResult> {
        return try {
            geocodingDataSource.searchCities(cityName)
        } catch (e: Exception) {
            Log.e("le.meteo_app_tp", "Error in repository while searching cities: ${e.message}", e)
            emptyList()
        }
    }
}
