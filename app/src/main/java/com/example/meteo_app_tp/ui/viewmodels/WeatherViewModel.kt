package com.example.meteo_app_tp.ui.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.data.model.Location
import com.example.meteo_app_tp.data.repository.WeatherRepository
import com.example.meteo_app_tp.ui.states.WeatherState
import com.example.meteo_app_tp.utils.NetworkConnectivityManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("StaticFieldLeak")
class WeatherViewModel(
    private val context: Context,
    private val weatherRepository: WeatherRepository,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val networkConnectivityManager: NetworkConnectivityManager,

) : ViewModel() {
    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    private var lastLocation: Location? = null
    private var isConnected = false
    private val cancellationTokenSource = CancellationTokenSource()

    init {
        Log.d("WeatherViewModel", "Initializing")

        viewModelScope.launch {
            networkConnectivityManager.isConnected.collect { connected ->
                Log.d("WeatherViewModel", "Network state changed: $connected")
                isConnected = connected
                if (connected) {
                    lastLocation?.let {
                        Log.d("WeatherViewModel", "Network restored, refreshing weather")
                        fetchWeatherForLocation()
                    }
                }
            }
        }

        viewModelScope.launch {
            weatherRepository.weatherFlow.collect { forecasts ->
                Log.d("WeatherViewModel", "Received forecasts update: ${forecasts.size} items")
                _state.update { currentState ->
                    currentState.copy(
                        forecasts = forecasts,
                        isLoading = false,
                        error = when {
                            forecasts.isEmpty() && !isConnected -> "No internet connection available"
                            forecasts.isEmpty() -> "Unable to fetch weather data"
                            else -> null
                        }
                    )
                }
            }
        }

        viewModelScope.launch {
            weatherRepository.cityNameFlow.collect { cityName ->
                Log.d("WeatherViewModel", "Received city name update: $cityName")
                _state.update { currentState ->
                    currentState.copy(
                        cityName = cityName
                    )
                }
            }
        }

        fetchWeatherForLocation()
    }

    fun fetchWeatherForLocation() {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Fetching location")
                _state.update { it.copy(isLoading = true, error = null) }

                val location = requestLocation()
                if (location == null) {
                    Log.e("WeatherViewModel", "Failed to get location")
                    _state.update {
                        it.copy(
                            error = "Unable to get location. Please ensure location services are enabled.",
                            isLoading = false
                        )
                    }
                    return@launch
                }

                Log.d("WeatherViewModel", "Got location: ${location.latitude}, ${location.longitude}")
                lastLocation = location

                weatherRepository.ensureWeatherData(
                    location.latitude,
                    location.longitude,
                    isConnected = isConnected
                )
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather", e)
                _state.update {
                    it.copy(
                        error = "Failed to fetch weather data: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun requestLocation(): Location? {
        return try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("WeatherViewModel", "Location permission not granted")
                return null
            }

            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await()

            location?.let {
                Log.d("WeatherViewModel", "Location received: ${it.latitude}, ${it.longitude}")
                Location(it.latitude, it.longitude)
            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Error getting location", e)
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSource.cancel()
    }
}