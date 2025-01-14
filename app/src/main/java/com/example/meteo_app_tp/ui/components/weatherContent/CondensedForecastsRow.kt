package com.example.meteo_app_tp.ui.components.weatherContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.R
import com.example.meteo_app_tp.data.local.entities.WeatherForecast
import com.example.meteo_app_tp.utils.WeatherConstants

@Composable
fun CondensedForecastsRow(
    forecasts: List<WeatherForecast>,
    modifier: Modifier = Modifier
) {
    StyledBoxWithShadow(
        modifier = modifier
            .height(WeatherConstants.HOURLY_FORECAST_HEIGHT.dp)
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp),
        label = stringResource(R.string.hourly_forecast)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            state = rememberLazyListState()
        ) {
            items(
                count = forecasts.take(12).size
            ) { index ->
                val forecast = forecasts[index]
                HourlyForecastCard(
                    hour = forecast.time,
                    temperature = forecast.temperature,
                    rainChance = forecast.rain,
                    modifier = modifier
                )
            }
        }
    }
}