package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.homescreen.components.common.StyledBoxWithShadow
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants
import com.example.meteo_app_tp.R

@Composable
fun CondensedForecastsRow(
    forecasts: List<WeatherForecast>,
    modifier: Modifier = Modifier
) {
    val secText = stringResource(id = R.string.second_text)
    StyledBoxWithShadow(
        modifier = modifier
            .height(WeatherConstants.HOURLY_FORECAST_HEIGHT.dp)
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp),
        label = secText
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
                items = forecasts.take(WeatherConstants.HOURLY_FORECASTS_COUNT),
                key = { forecast -> forecast.time }
            ) { forecast ->
                HourlyForecastCard(forecast = forecast)
            }
        }
    }
}

