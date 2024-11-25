package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.homescreen.components.common.StyledBoxWithShadow
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants
import com.example.meteo_app_tp.R

@Composable
fun RemainingForecastsColumn(
    forecasts: List<WeatherForecast>,
    modifier: Modifier = Modifier
) {
    val remainingForecasts = remember(forecasts) {
        forecasts.drop(WeatherConstants.HOURLY_FORECASTS_COUNT)
            .filterIndexed { index, _ -> index % 24 == 0 }
    }

    val thirdText = stringResource(id = R.string.third_text)

    StyledBoxWithShadow(
        modifier = modifier
            .height(WeatherConstants.DAILY_FORECAST_HEIGHT.dp)
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp),
        label = thirdText
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            state = rememberLazyListState()
        ) {
            items(
                items = remainingForecasts,
                key = { forecast -> forecast.time }
            ) { forecast ->
                DailyForecastRow(forecast = forecast)
            }
        }
    }
}