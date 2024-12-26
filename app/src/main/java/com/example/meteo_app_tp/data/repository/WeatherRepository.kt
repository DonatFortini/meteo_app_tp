package com.example.meteo_app_tp.data.repository

import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.source.IGeocodingDataSource
import com.example.meteo_app_tp.data.source.IWeatherDataSource
import com.example.meteo_app_tp.data.source.WeatherCacheDataSource
import kotlinx.coroutines.flow.*

class WeatherRepository(
    private val weatherApiDataSource: IWeatherDataSource,
    private val weatherCacheDataSource: WeatherCacheDataSource,
    private val geocodingDataSource: IGeocodingDataSource
) : IWeatherRepository {

    override fun getWeatherData(lat: String, long: String): Flow<Result<Pair<String, List<WeatherForecast>>>> = flow {
        try {
            val cacheResponse = weatherCacheDataSource.getWeatherData(lat, long)
            val cityResponse = geocodingDataSource.getClosestCityName(lat.toDouble(), long.toDouble())
            if (cityResponse !is CityResponse.Success) {
                throw Exception("Failed to get city name: ${(cityResponse as CityResponse.Error).message}")
            }

            when (cacheResponse) {
                is WeatherResponse.Success -> {
                    emit(Result.success(Pair(cityResponse.cityName, cacheResponse.value)))
                    try {
                        when (val apiResponse = weatherApiDataSource.getWeatherData(lat, long)) {
                            is WeatherResponse.Success -> {
                                weatherCacheDataSource.saveToCache(apiResponse.value, lat, long)
                                emit(Result.success(Pair(cityResponse.cityName, apiResponse.value)))
                            }
                            is WeatherResponse.Error -> {
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
                is WeatherResponse.Error -> {
                    when (val apiResponse = weatherApiDataSource.getWeatherData(lat, long)) {
                        is WeatherResponse.Success -> {
                            weatherCacheDataSource.saveToCache(apiResponse.value, lat, long)
                            emit(Result.success(Pair(cityResponse.cityName, apiResponse.value)))
                        }
                        is WeatherResponse.Error -> {
                            throw Exception("Failed to get weather data: ${apiResponse.errorMessage}")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}