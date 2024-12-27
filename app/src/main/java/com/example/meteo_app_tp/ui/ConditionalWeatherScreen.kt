package com.example.meteo_app_tp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meteo_app_tp.data.WeatherCacheManager
import com.example.meteo_app_tp.ui.homescreen.HomeScreen
import com.example.meteo_app_tp.ui.homescreen.OfflineHomeScreen
import com.example.meteo_app_tp.ui.homescreen.HomeScreenViewModel
import com.example.meteo_app_tp.ui.homescreen.HomeScreenViewModelFactory


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConditionalWeatherScreen(
    lat: String,
    lon: String,
    onSettingsClick: () -> Unit,
    cacheManager: WeatherCacheManager,
    viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModelFactory(cacheManager)
    )
) {
    val context = LocalContext.current
    var isNetworkAvailable by remember { mutableStateOf(checkNetworkAvailability(context)) }
    val homeScreenState by viewModel.homeScreenState.collectAsState()

    DisposableEffect(context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                isNetworkAvailable = true
            }

            override fun onLost(network: android.net.Network) {
                isNetworkAvailable = false
            }
        }

        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    LaunchedEffect(lat, lon, isNetworkAvailable) {
        if (lat.isNotEmpty() && lon.isNotEmpty()) {
            viewModel.fetchWeatherData(lat, lon, isNetworkAvailable)
        }
    }

    when {
        homeScreenState.isLoading -> {

        }
        !isNetworkAvailable && homeScreenState.weatherForecasts == null -> {
            OfflineHomeScreen(onSettingsClick = onSettingsClick)
        }
        else -> {
            HomeScreen(
                lat = lat,
                lon = lon,
                onSettingsClick = onSettingsClick,
                viewModel = viewModel
            )
        }
    }
}

private fun checkNetworkAvailability(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } == true
}