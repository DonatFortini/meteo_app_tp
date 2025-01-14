package com.example.meteo_app_tp.ui.components.weatherContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.R
import com.example.meteo_app_tp.data.local.entities.WeatherForecast
import com.example.meteo_app_tp.utils.WeatherConstants

@Composable
fun RemainingForecastsColumn(
    forecasts: List<WeatherForecast>,
    modifier: Modifier = Modifier
) {
    StyledBoxWithShadow(
        modifier = modifier
            .height(WeatherConstants.DAILY_FORECAST_HEIGHT.dp)
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp),
        label = stringResource(R.string.daily_forecast)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            state = rememberLazyListState()
        ) {
            val currentHourIndex = forecasts.firstOrNull()?.let {
                forecasts.indexOf(it)
            } ?: 0
            items(7) { dayOffset ->
                val forecastIndex = currentHourIndex + (dayOffset * 24)
                if (forecastIndex < forecasts.size) {
                    val forecast = forecasts[forecastIndex]
                    DailyForecastRow(
                        day = forecast.time,
                        temperature = forecast.temperature,
                        windSpeed = forecast.windSpeed,
                        rain = forecast.rain
                    )
                }
            }
        }
    }
}