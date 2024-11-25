package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.homescreen.utils.DateTimeUtils


@Composable
fun WeatherForecasts(
    forecasts: List<WeatherForecast>,
    modifier: Modifier = Modifier
) {
    val adjustedForecasts = remember(forecasts) {
        DateTimeUtils.getClosestHourForecasts(forecasts)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item { MainWeatherDisplay(adjustedForecasts.first()) }
        item { CondensedForecastsRow(adjustedForecasts.drop(1)) }
        item { RemainingForecastsColumn(adjustedForecasts.drop(1)) }
    }
}

