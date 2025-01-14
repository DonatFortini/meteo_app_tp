package com.example.meteo_app_tp.data.remote

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocodingService {
    private val nominatimApi: NominatimApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        nominatimApi = retrofit.create(NominatimApi::class.java)
    }

    suspend fun getCityName(latitude: Double, longitude: Double): Result<String> {
        return try {
            Log.d("GeocodingService", "Fetching city name for lat: $latitude, lon: $longitude")
            val response = nominatimApi.reverseGeocode(latitude, longitude)
            val cityName = with(response.address) {
                city ?: town ?: village ?: municipality ?: county ?: "Unknown Location"
            }
            Log.d("GeocodingService", "Successfully got city name: $cityName")
            Result.success(cityName)
        } catch (e: Exception) {
            Log.e("GeocodingService", "Error fetching city name", e)
            Result.failure(e)
        }
    }
}