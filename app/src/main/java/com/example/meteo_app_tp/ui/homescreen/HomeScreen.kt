package com.example.meteo_app_tp.ui.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    lat: String,
    lon: String,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()

    LaunchedEffect(lat, lon) {
        viewModel.fetchWeatherData(lat, lon)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            homeScreenState.isLoading -> {
                CircularProgressIndicator()
            }

            homeScreenState.errorMessage != null -> {
                Text(text = "Error: ${homeScreenState.errorMessage}")
            }

            homeScreenState.weatherForecasts != null -> {
                WeatherContent(homeScreenState.weatherForecasts!!)
            }

            else -> {
                Text("unknown state")
            }
        }

    }
}

@Composable
fun WeatherContent(forecasts: List<WeatherForecast>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(forecasts) { forecast ->
            WeatherForecastItem(forecast)
        }
    }
}

@Composable
fun WeatherForecastItem(forecast: WeatherForecast) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Time: ${forecast.time}")
        Text(text = "Temperature: ${forecast.temp}°C")
        Text(text = "Humidity: ${forecast.humidity}%")
        Text(text = "Apparent Temperature: ${forecast.app_temp}°C")
        Text(text = "Rain: ${forecast.rain} mm")
        Text(text = "Wind Speed: ${forecast.wind_speed} m/s")
    }
}
