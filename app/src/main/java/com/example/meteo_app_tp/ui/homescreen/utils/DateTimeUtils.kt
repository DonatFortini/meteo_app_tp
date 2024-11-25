package com.example.meteo_app_tp.ui.homescreen.utils

import com.example.meteo_app_tp.data.model.WeatherForecast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

object DateTimeUtils {

    fun formatTime(time: String, onlyDay: Boolean = false): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = if (onlyDay) {
            SimpleDateFormat("dd MMM", Locale.getDefault())
        } else {
            SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        }
        return try {
            outputFormat.format(inputFormat.parse(time)!!)
        } catch (e: Exception) {
            "Invalid Time Format"
        }
    }

    fun getClosestHourForecasts(forecasts: List<WeatherForecast>): List<WeatherForecast> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val currentTime = Calendar.getInstance()
        val startIndex = forecasts.indexOfMinBy { forecast ->
            try {
                val forecastTime = Calendar.getInstance().apply {
                    time = inputFormat.parse(forecast.time)!!
                }
                abs(forecastTime.timeInMillis - currentTime.timeInMillis)
            } catch (e: Exception) {
                Long.MAX_VALUE
            }
        }.coerceAtLeast(0)

        return forecasts.drop(startIndex)
    }

    private fun <T> List<T>.indexOfMinBy(selector: (T) -> Long): Int {
        if (isEmpty()) return -1
        var minValue = selector(first())
        var minIndex = 0

        for (i in 1..lastIndex) {
            val value = selector(get(i))
            if (value < minValue) {
                minValue = value
                minIndex = i
            }
        }

        return minIndex
    }
}

