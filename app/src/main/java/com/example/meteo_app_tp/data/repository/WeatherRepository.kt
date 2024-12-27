package com.example.meteo_app_tp.data.repository


import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.model.WeatherData
import com.example.meteo_app_tp.data.model.WeatherDataState
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.source.IGeocodingDataSource
import com.example.meteo_app_tp.data.source.IWeatherDataSource
import com.example.meteo_app_tp.data.source.WeatherCacheDataSource
import com.example.meteo_app_tp.ui.homescreen.DataSource
import kotlinx.coroutines.flow.*

class WeatherRepository(
    private val weatherApiDataSource: IWeatherDataSource,
    private val weatherCacheDataSource: WeatherCacheDataSource,
    private val geocodingDataSource: IGeocodingDataSource
) {


    fun getWeatherData(lat: String, long: String, isNetworkAvailable: Boolean): Flow<WeatherDataState> = flow {
        emit(WeatherDataState.Loading)

        try {
            val cityName = when (val cityResponse = geocodingDataSource.getClosestCityName(lat.toDouble(), long.toDouble())) {
                is CityResponse.Success -> cityResponse.cityName
                is CityResponse.Error -> "Location (${lat}, ${long})"
            }

            if (!isNetworkAvailable) {
                when (val cacheResponse = weatherCacheDataSource.getWeatherData(lat, long)) {
                    is WeatherResponse.Success -> {
                        emit(WeatherDataState.Success(
                            WeatherData(
                                cityName,
                                cacheResponse.value,
                                DataSource.CACHE
                            )
                        ))
                    }
                    is WeatherResponse.Error -> {
                        emit(WeatherDataState.Error("No internet connection and no cached data available"))
                    }
                }
            } else {
                when (val cacheResponse = weatherCacheDataSource.getWeatherData(lat, long)) {
                    is WeatherResponse.Success -> {
                        emit(WeatherDataState.Success(
                            WeatherData(
                                cityName,
                                cacheResponse.value,
                                DataSource.CACHE
                            )
                        ))
                    }
                    is WeatherResponse.Error -> {
                    }
                }

                when (val apiResponse = weatherApiDataSource.getWeatherData(lat, long)) {
                    is WeatherResponse.Success -> {
                        weatherCacheDataSource.saveToCache(
                            apiResponse.value,
                            lat,
                            long,
                            cityName
                        )
                        emit(WeatherDataState.Success(
                            WeatherData(
                                cityName,
                                apiResponse.value,
                                DataSource.API
                            )
                        ))
                    }
                    is WeatherResponse.Error -> {
                        emit(WeatherDataState.Error("Failed to fetch from API: ${apiResponse.errorMessage}"))
                    }
                }
            }
        } catch (e: Exception) {
            emit(WeatherDataState.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}



