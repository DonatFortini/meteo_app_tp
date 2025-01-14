package com.example.meteo_app_tp.data.repository

import android.util.Log
import com.example.meteo_app_tp.data.local.dao.WeatherDao
import com.example.meteo_app_tp.data.local.entities.WeatherEntity
import com.example.meteo_app_tp.data.local.entities.WeatherForecast
import com.example.meteo_app_tp.data.remote.GeocodingService
import com.example.meteo_app_tp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val geocodingService: GeocodingService
) {
    private val _weatherFlow = MutableStateFlow<List<WeatherForecast>>(emptyList())
    val weatherFlow: StateFlow<List<WeatherForecast>> = _weatherFlow

    private val _cityNameFlow = MutableStateFlow<String>("Loading...")
    val cityNameFlow: StateFlow<String> = _cityNameFlow

    private var cachedForecasts: List<WeatherForecast> = emptyList()
    private var lastUpdateTimestamp: Long = 0
    private var lastLocationId: String? = null

    suspend fun ensureWeatherData(latitude: Double, longitude: Double, isConnected: Boolean) {
        try {
            Log.d("WeatherRepository", "Ensuring weather data for lat: $latitude, lon: $longitude, connected: $isConnected")
            val locationId = "${latitude}_${longitude}"

            if (locationId != lastLocationId) {
                weatherDao.getWeather(locationId)?.let { cachedWeather ->
                    Log.d("WeatherRepository", "Found cached weather for new location")
                    cachedForecasts = cachedWeather.forecasts
                    lastUpdateTimestamp = cachedWeather.timestamp
                    _cityNameFlow.emit(cachedWeather.cityName)
                    _weatherFlow.emit(cachedForecasts)
                }
            }

            if (!isConnected) {
                Log.d("WeatherRepository", "Device is offline, using cache only")
                return
            }

            val shouldRefresh = lastUpdateTimestamp == 0L ||
                    (System.currentTimeMillis() - lastUpdateTimestamp) > 30 * 60 * 1000 ||
                    locationId != lastLocationId

            if (shouldRefresh) {
                Log.d("WeatherRepository", "Fetching fresh weather data")
                val weatherResult = remoteDataSource.fetchWeather(latitude, longitude)
                val cityNameResult = geocodingService.getCityName(latitude, longitude)

                weatherResult.onSuccess { response ->
                    Log.d("WeatherRepository", "Successfully fetched new weather data")


                    val forecasts = response.hourly.time.mapIndexedNotNull { index, time ->
                        val temperature = response.hourly.temperature_2m.getOrNull(index)
                        val humidity = response.hourly.relative_humidity_2m.getOrNull(index)
                        val windSpeed = response.hourly.wind_speed_10m.getOrNull(index)
                        val rain = response.hourly.rain.getOrNull(index)
                        val apparentTemp = response.hourly.apparent_temperature.getOrNull(index)


                        if (temperature != null && humidity != null &&
                            windSpeed != null && rain != null && apparentTemp != null) {
                            WeatherForecast(
                                time = time,
                                temperature = temperature,
                                humidity = humidity,
                                windSpeed = windSpeed,
                                rain = rain,
                                apparentTemperature = apparentTemp
                            )
                        } else null
                    }

                    if (forecasts.isNotEmpty()) {
                        val cityName = cityNameResult.getOrDefault("Unknown Location")
                        _cityNameFlow.emit(cityName)

                        cachedForecasts = forecasts
                        lastUpdateTimestamp = System.currentTimeMillis()
                        lastLocationId = locationId

                        val weatherEntity = WeatherEntity(
                            id = locationId,
                            forecasts = forecasts,
                            cityName = cityName,
                            timestamp = lastUpdateTimestamp,
                            latitude = latitude,
                            longitude = longitude
                        )

                        Log.d("WeatherRepository", "Saving ${forecasts.size} forecasts to database")
                        weatherDao.insertWeather(weatherEntity)
                        _weatherFlow.emit(forecasts)
                    } else {
                        Log.e("WeatherRepository", "No valid forecasts available")
                    }
                }.onFailure { error ->
                    Log.e("WeatherRepository", "Failed to fetch weather", error)
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error in ensureWeatherData", e)
        }
    }
}