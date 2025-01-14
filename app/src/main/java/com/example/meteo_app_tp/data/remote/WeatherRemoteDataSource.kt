package com.example.meteo_app_tp.data.remote

import android.util.Log
import com.example.meteo_app_tp.data.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherRemoteDataSource {
    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    suspend fun fetchWeather(latitude: Double, longitude: Double): Result<WeatherResponse> {
        return try {
            Log.d("WeatherAPI", "Fetching weather for lat: $latitude, lon: $longitude")
            val response = weatherApi.getWeather(latitude, longitude)
            Log.d("WeatherAPI", "Response received: $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("WeatherAPI", "Error fetching weather", e)
            Result.failure(e)
        }
    }
}