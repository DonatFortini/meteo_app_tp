package com.example.meteo_app_tp.ui.homescreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.model.WeatherCache
import com.example.meteo_app_tp.data.repository.GeocodingRepository
import com.example.meteo_app_tp.data.repository.WeatherApiRepository
import com.example.meteo_app_tp.data.repository.WeatherCacheRepository
import com.example.meteo_app_tp.data.repository.WeatherForecastRepository
import com.example.meteo_app_tp.data.source.GeocodingApiDataSource
import com.example.meteo_app_tp.data.source.WeatherApiDataSource
import com.example.meteo_app_tp.data.source.WeatherCacheDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO fix to add the new repo
class HomeScreenViewModel(
    weatherApiRepository: WeatherApiRepository = WeatherApiRepository(WeatherApiDataSource()),
    weatherCacheRepository: WeatherCacheRepository = WeatherCacheRepository(WeatherCacheDataSource()),
    private val geocodingRepository: GeocodingRepository = GeocodingRepository(GeocodingApiDataSource())
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
    private val weatherRepository: WeatherForecastRepository = WeatherForecastRepository (
        weatherApiRepository,
        weatherCacheRepository,
        homeScreenState.value.isConnected
    )
    fun fetchWeatherData(lat: String, lon: String) {
        viewModelScope.launch {
            _homeScreenState.update { it.copy(isLoading = true) }
            val cityName = getCityName(lat, lon)
            fetchAndUpdateWeather(lat, lon, cityName)
        }
    }


    private suspend fun getCityName(lat: String, lon: String): String {
        return try {
            val cityResponse = geocodingRepository.getClosestCityName(lat.toDouble(), lon.toDouble())
            when (cityResponse) {
                is CityResponse.Success -> cityResponse.cityName
                is CityResponse.Error -> "Unknown City"
            }
        } catch (e: Exception) {
            "Unknown City"
        }
    }


    private suspend fun fetchAndUpdateWeather(lat: String, lon: String, cityName: String) {
        try {
            weatherRepository.getWeatherData(lat, lon).collect { forecasts ->
                _homeScreenState.update {
                    it.copy(
                        isLoading = false,
                        weatherForecasts = forecasts,
                        city = cityName,
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
