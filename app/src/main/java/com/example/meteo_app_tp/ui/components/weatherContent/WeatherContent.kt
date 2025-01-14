package com.example.meteo_app_tp.ui.components.weatherContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.local.entities.WeatherForecast
import com.example.meteo_app_tp.ui.states.WeatherState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun WeatherContent(
    weatherState: WeatherState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 0.dp)
    ) {
        val adjusted = getClosestHourForecasts(weatherState.forecasts)

        item { MainWeatherDisplay(adjusted.first()) }
        item { CondensedForecastsRow(adjusted.drop(1)) }
        item { RemainingForecastsColumn(adjusted.drop(1)) }
    }
}

fun getClosestHourForecasts(forecasts: List<WeatherForecast>): List<WeatherForecast> {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val currentTime = Calendar.getInstance()
    val startIndex = forecasts.indices.minByOrNull { index ->
        try {
            val forecastTime = Calendar.getInstance().apply {
                time = inputFormat.parse(forecasts[index].time)!!
            }
            abs(forecastTime.timeInMillis - currentTime.timeInMillis)
        } catch (e: Exception) {
            Long.MAX_VALUE
        }
    } ?: 0

    return forecasts.drop(startIndex)
}