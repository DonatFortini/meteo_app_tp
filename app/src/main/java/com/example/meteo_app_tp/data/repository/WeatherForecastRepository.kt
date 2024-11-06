package com.example.meteo_app_tp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.example.meteo_app_tp.data.model.WeatherForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.android.gms.location.LocationServices

class WeatherForecastRepository(
    private val weatherApiRepository: WeatherApiRepository,
    private val weatherCacheRepository: WeatherCacheRepository,
    private val context: Context
) : IWeatherRepository {

    override suspend fun getWeatherData(lat: String, lon: String): Flow<List<WeatherForecast>> = flow {
        val isNetworkAvailable = isNetworkAvailable(context)
        val location = getLastKnownLocation(context)

        if (isNetworkAvailable && location != null) {
            try {
                weatherApiRepository.getWeatherData(lat, lon).collect { forecasts ->
                    weatherCacheRepository.cacheData(forecasts, lat, lon)
                    emit(forecasts)
                }
            } catch (e: Exception) {
                 weatherCacheRepository.getWeatherData(lat, lon).collect { cachedData ->
                    if (cachedData.isNotEmpty()) {
                        emit(cachedData)
                    } else {
                        emit(emptyList())
                    }
                }
            }
        } else {
            weatherCacheRepository.getWeatherData(lat, lon).collect { cachedData ->
                if (cachedData.isNotEmpty()) {
                    emit(cachedData)
                } else {
                    emit(emptyList())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(context: Context): Location? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        var location: Location? = null
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            location = loc
        }
        return location
    }
}
