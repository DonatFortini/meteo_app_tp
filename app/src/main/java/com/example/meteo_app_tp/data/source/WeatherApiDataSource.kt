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
    ): WeatherResponse<WeatherForecast> {
        return withContext(Dispatchers.IO) {
            val url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%S&longitude=%S&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,rain,wind_speed_10m&models=meteofrance_seamless",lat,long)
            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    if (!jsonResponse.isNullOrEmpty()) {
                        val weatherForecast = parseWeatherData(jsonResponse)
                        WeatherResponse.Success(weatherForecast)
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

    private fun parseWeatherData(jsonResponse: String): WeatherForecast {
        val jsonObject = JSONObject(jsonResponse)
        val hourly = jsonObject.getJSONObject("hourly")

        val time = hourly.getJSONArray("time").getString(0)
        val temperature = hourly.getJSONArray("temperature_2m").getDouble(0)
        val humidity = hourly.getJSONArray("relative_humidity_2m").getInt(0)
        val apparentTemp = hourly.getJSONArray("apparent_temperature").getDouble(0)
        val rain = hourly.getJSONArray("rain").getDouble(0)
        val windSpeed = hourly.getJSONArray("wind_speed_10m").getDouble(0)

        return WeatherForecast(
            time = time,
            temp = temperature,
            humdity = humidity,
            app_temp = apparentTemp,
            rain = rain,
            wind_speed = windSpeed
        )
    }


}



