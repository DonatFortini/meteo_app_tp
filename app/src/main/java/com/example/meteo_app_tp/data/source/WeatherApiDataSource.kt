package com.example.meteo_app_tp.data.source

import com.example.meteo_app_tp.data.model.WeatherResponse
import com.example.meteo_app_tp.data.model.WeatherForecast
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class WeatherApiDataSource : IWeatherDataSource {

    private val client = OkHttpClient()

    override suspend fun getWeatherData(
        lat: String,
        long: String
    ): WeatherResponse<List<WeatherForecast>> {
        return withContext(Dispatchers.IO) {
            val url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,rain,wind_speed_10m&models=meteofrance_seamless",
                lat, long
            )
            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    if (!jsonResponse.isNullOrEmpty()) {
                        val weatherForecastList = parseWeatherData(jsonResponse)
                        WeatherResponse.Success(weatherForecastList)
                    } else {
                        WeatherResponse.Error("Empty response")
                    }
                } else {
                    WeatherResponse.Error("Failed to fetch data: ${response.message}")
                }
            } catch (e: IOException) {
                WeatherResponse.Error("Network error: ${e.message}")
            }
        }
    }

    private fun parseWeatherData(jsonResponse: String): List<WeatherForecast> {
        val jsonObject = JSONObject(jsonResponse)
        val hourly = jsonObject.getJSONObject("hourly")

        val times = hourly.getJSONArray("time")
        val temperatures = hourly.getJSONArray("temperature_2m")
        val humanities = hourly.getJSONArray("relative_humidity_2m")
        val apparentTemps = hourly.getJSONArray("apparent_temperature")
        val rains = hourly.getJSONArray("rain")
        val windSpeeds = hourly.getJSONArray("wind_speed_10m")

        val weatherForecastList = mutableListOf<WeatherForecast>()

        for (i in 0 until times.length()) {
            val time = times.getString(i)
            val temperature = temperatures.optDouble(i, Double.NaN)
            val humidity = humanities.optInt(i, -1)
            val apparentTemp = apparentTemps.optDouble(i, Double.NaN)
            val rain = rains.optDouble(i, Double.NaN)
            val windSpeed = windSpeeds.optDouble(i, Double.NaN)

            if (!temperature.isNaN() && humidity >= 0) {
                val weatherForecast = WeatherForecast(
                    time = time,
                    temp = temperature,
                    humidity = humidity,
                    app_temp = apparentTemp,
                    rain = rain,
                    wind_speed = windSpeed
                )
                weatherForecastList.add(weatherForecast)
            }
        }

        return weatherForecastList
    }
}
