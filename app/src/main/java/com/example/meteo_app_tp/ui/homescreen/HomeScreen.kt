package com.example.meteo_app_tp.ui.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meteo_app_tp.ui.common.SharedScreenLayout
import com.example.meteo_app_tp.ui.homescreen.components.WeatherContent
import com.example.meteo_app_tp.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    lat: String,
    lon: String,
    onSettingsClick: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = homeScreenState.isRefreshing,
        onRefresh = { viewModel.refreshWeatherData() }
    )

    LaunchedEffect(lat, lon) {
        if (lat.isNotEmpty() && lon.isNotEmpty()) {
            viewModel.fetchWeatherData(lat, lon,true)
        }
    }

    val backgroundBrush = remember(homeScreenState.weatherForecasts) {
        getBackgroundBrush(homeScreenState.weatherForecasts?.firstOrNull()?.rain)
    }

    SharedScreenLayout(backgroundBrush = backgroundBrush) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            WeatherContent(
                homeScreenState = homeScreenState,
                onSettingsClick = onSettingsClick,
                modifier = Modifier.fillMaxSize()
            )

            PullRefreshIndicator(
                refreshing = homeScreenState.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}