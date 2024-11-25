package com.example.meteo_app_tp.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meteo_app_tp.ui.homescreen.components.WeatherContent
import com.example.meteo_app_tp.ui.theme.*
import com.example.meteo_app_tp.ui.homescreen.utils.*


@Composable
fun HomeScreen(
    lat: String,
    lon: String,
    onSettingsClick: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()
    val spacing = WeatherConstants.DEFAULT_PADDING.dp

    LaunchedEffect(lat, lon) {
        viewModel.fetchWeatherData(lat, lon)
    }

    val backgroundBrush = remember(homeScreenState.weatherForecasts) {
        getBackgroundBrush(homeScreenState.weatherForecasts?.firstOrNull()?.rain)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(spacing)
            .padding(top = WeatherConstants.TOP_PADDING.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        WeatherContent(
            homeScreenState = homeScreenState,
            onSettingsClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}





