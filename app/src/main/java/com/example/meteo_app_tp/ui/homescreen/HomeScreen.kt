package com.example.meteo_app_tp.ui.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meteo_app_tp.ui.common.SharedScreenLayout
import com.example.meteo_app_tp.ui.homescreen.components.WeatherContent
import com.example.meteo_app_tp.ui.theme.*



@Composable
fun HomeScreen(
    lat: String,
    lon: String,
    onSettingsClick: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()

    LaunchedEffect(lat, lon) {
        viewModel.fetchWeatherData(lat, lon)
    }

    val backgroundBrush = remember(homeScreenState.weatherForecasts) {
        getBackgroundBrush(homeScreenState.weatherForecasts?.firstOrNull()?.rain)
    }

    SharedScreenLayout(backgroundBrush = backgroundBrush) {
        WeatherContent(
            homeScreenState = homeScreenState,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}





