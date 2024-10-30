package com.example.meteo_app_tp.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower


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
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A11CB), // Purple-blue
                        Color(0xFF2575FC)  // Light blue
                    )
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            homeScreenState.isLoading -> {
                CircularProgressIndicator(color = Color.White)
            }

            homeScreenState.errorMessage != null -> {
                Text(text = "Error: ${homeScreenState.errorMessage}", color = Color.White)
            }

            homeScreenState.weatherForecasts != null -> {
                WeatherContent(homeScreenState.weatherForecasts!!)
            }

            else -> {
                Text("unknown state", color = Color.White)
            }
        }
    }
}


@Composable
fun WeatherContent(forecasts: List<WeatherForecast>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(forecasts) { forecast ->
            WeatherForecastItem(forecast)
        }
    }
}

@Composable
fun WeatherForecastItem(forecast: WeatherForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Time: ${forecast.time}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconText(Icons.Default.Thermostat, "${forecast.temp}°C", "Temperature")
                Spacer(modifier = Modifier.width(16.dp))
                IconText(Icons.Default.WaterDrop, "${forecast.humidity}%", "Humidity")
                Spacer(modifier = Modifier.width(16.dp))
                IconText(Icons.Default.WindPower, "${forecast.wind_speed} m/s", "Wind Speed")
            }
        }
    }
}

@Composable
fun IconText(icon: ImageVector, text: String, contentDescription: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

