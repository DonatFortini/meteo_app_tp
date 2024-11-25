package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.ui.homescreen.HomeScreenState
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants
import com.example.meteo_app_tp.ui.theme.TextColorGray

@Composable
fun WeatherContent(
    homeScreenState: HomeScreenState,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(top = WeatherConstants.DEFAULT_PADDING.dp)) {
        TopBar(homeScreenState.city,onSettingsClick)

        when {
            homeScreenState.isLoading -> LoadingIndicator()
            homeScreenState.errorMessage != null -> ErrorMessage(homeScreenState.errorMessage)
            !homeScreenState.weatherForecasts.isNullOrEmpty() -> WeatherForecasts(
                forecasts = homeScreenState.weatherForecasts
            )
            else -> NoDataMessage()
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = TextColorGray)
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = "Error: $message",
        color = TextColorGray,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun NoDataMessage() {
    Text(
        text = "No data available",
        color = TextColorGray,
        modifier = Modifier.padding(16.dp)
    )
}