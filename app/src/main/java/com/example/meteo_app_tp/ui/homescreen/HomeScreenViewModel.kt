package com.example.meteo_app_tp.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo_app_tp.data.repository.IWeatherRepository
import com.example.meteo_app_tp.data.repository.WeatherRepository
import com.example.meteo_app_tp.data.source.GeocodingApiDataSource
import com.example.meteo_app_tp.data.source.WeatherApiDataSource
import com.example.meteo_app_tp.data.source.WeatherCacheDataSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val weatherRepository: IWeatherRepository = WeatherRepository(
        WeatherApiDataSource(),
        WeatherCacheDataSource(),
        GeocodingApiDataSource()
    ),

    ) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private var lastLocation: Pair<String, String>? = null

    fun fetchWeatherData(lat: String, lon: String) {
        if (lastLocation?.first == lat && lastLocation?.second == lon) return
        lastLocation = Pair(lat, lon)

        viewModelScope.launch {
            if (_homeScreenState.value.weatherForecasts == null) {
                _homeScreenState.update { it.copy(isLoading = true) }
            } else {
                _homeScreenState.update { it.copy(isRefreshing = true) }
            }

            try {
                weatherRepository.getWeatherData(lat, lon).collect { result ->
                    result.fold(
                        onSuccess = { (cityName, forecasts) ->
                            _homeScreenState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    weatherForecasts = forecasts,
                                    city = cityName,
                                    errorMessage = null,
                                    dataSource = if (state.dataSource == DataSource.NONE) DataSource.CACHE else DataSource.API
                                )
                            }
                        },
                        onFailure = { error ->
                            if (_homeScreenState.value.weatherForecasts == null) {
                                _homeScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        isRefreshing = false,
                                        errorMessage = error.message ?: "Failed to fetch weather data"
                                    )
                                }
                            } else {
                                _homeScreenState.update { it.copy(isRefreshing = false) }
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                if (_homeScreenState.value.weatherForecasts == null) {
                    _homeScreenState.update { state ->
                        state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = e.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    fun refreshWeatherData() {
        lastLocation?.let { (lat, lon) ->
            fetchWeatherData(lat, lon)
        }
    }
}