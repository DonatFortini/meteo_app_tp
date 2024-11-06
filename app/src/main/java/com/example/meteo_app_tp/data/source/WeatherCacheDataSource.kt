package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.WeatherCache
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.model.WeatherResponse
import java.time.Instant

class WeatherCacheDataSource : IWeatherDataSource {

    private var cache: WeatherCache? = null
    private val cacheExpirationTimeMillis = 10 * 60 * 1000

    override suspend fun getWeatherData(lat: String, long: String): WeatherResponse<List<WeatherForecast>> {
        val currentTime = Instant.now().toEpochMilli()
        return if (cache != null && cache!!.latitude == lat && cache!!.longitude == long &&
            currentTime - cache!!.timestamp < cacheExpirationTimeMillis) {
            WeatherResponse.Success(cache!!.forecasts)
        } else {
            WeatherResponse.Error("Cache expired or no data")
        }
    }

    fun saveToCache(forecasts: List<WeatherForecast>, lat: String, lon: String) {
        cache = WeatherCache(forecasts, lat, lon, Instant.now().toEpochMilli())
    }
}
