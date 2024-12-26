package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.model.CitySearchResult

interface IGeocodingDataSource {
    suspend fun getClosestCityName(latitude: Double, longitude: Double): CityResponse
    suspend fun searchCities(cityName: kotlin.String): kotlin.collections.List<com.example.meteo_app_tp.data.model.CitySearchResult>
}