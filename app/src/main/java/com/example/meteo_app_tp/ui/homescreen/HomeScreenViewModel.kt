package com.example.meteo_app_tp.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.data.repository.WeatherApiRepository
import com.example.meteo_app_tp.data.source.WeatherApiDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val repository: WeatherApiRepository = WeatherApiRepository(WeatherApiDataSource())
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    fun fetchWeatherData(lat: String, lon: String) {
        viewModelScope.launch {
            _homeScreenState.update { it.copy(isLoading = true) }

            try {
                repository.getWeatherData(lat, lon).collect { forecasts ->
                    _homeScreenState.update {
                        it.copy(
                            isLoading = false,
                            weatherForecasts = forecasts,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _homeScreenState.update {
                    it.copy(
                        isLoading = false,
                        weatherForecasts = null,
                        errorMessage = e.message ?: "Failed to fetch data"
                    )
                }
            }
        }
    }
}

