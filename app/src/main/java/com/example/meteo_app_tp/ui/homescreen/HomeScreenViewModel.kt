package com.example.meteo_app_tp.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.data.WeatherCacheManager
import com.example.meteo_app_tp.data.repository.WeatherRepository
import com.example.meteo_app_tp.data.model.WeatherDataState
import com.example.meteo_app_tp.data.source.GeocodingApiDataSource
import com.example.meteo_app_tp.data.source.WeatherApiDataSource
import com.example.meteo_app_tp.data.source.WeatherCacheDataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val weatherRepository: WeatherRepository,
    private val cacheManager: WeatherCacheManager
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private var lastLocation: Pair<String, String>? = null
    private var currentJob: Job? = null

    fun fetchWeatherData(lat: String, lon: String, isOnline: Boolean) {
        if (lastLocation?.first == lat && lastLocation?.second == lon && !_homeScreenState.value.isRefreshing) return
        lastLocation = Pair(lat, lon)

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _homeScreenState.update { it.copy(isLoading = true) }

            if (!isOnline) {
                val cachedData = cacheManager.getCachedWeather(lat, lon)
                if (cachedData != null) {
                    _homeScreenState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isRefreshing = false,
                            weatherForecasts = cachedData.forecasts,
                            city = cachedData.cityName,
                            errorMessage = null,
                            isConnected = false
                        )
                    }
                    return@launch
                }
            }

            weatherRepository.getWeatherData(lat, lon,isOnline).collect { state ->
                when (state) {
                    is WeatherDataState.Success -> {
                        // Save to cache
                        cacheManager.saveToCache(
                            forecasts = state.data.forecasts,
                            lat = lat,
                            lon = lon,
                            cityName = state.data.cityName
                        )

                        _homeScreenState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isRefreshing = false,
                                weatherForecasts = state.data.forecasts,
                                city = state.data.cityName,
                                errorMessage = null,
                                isConnected = true
                            )
                        }
                    }
                    is WeatherDataState.Error -> {
                        _homeScreenState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = state.message,
                                isConnected = false
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun refreshWeatherData() {
        _homeScreenState.update { it.copy(isRefreshing = true) }
        lastLocation?.let { (lat, lon) ->
            fetchWeatherData(lat, lon, true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}

class HomeScreenViewModelFactory(
    private val cacheManager: WeatherCacheManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val cacheDataSource = WeatherCacheDataSource(cacheManager)
            return HomeScreenViewModel(
                WeatherRepository(
                    WeatherApiDataSource(),
                    cacheDataSource,
                    GeocodingApiDataSource()
                ),
                cacheManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}