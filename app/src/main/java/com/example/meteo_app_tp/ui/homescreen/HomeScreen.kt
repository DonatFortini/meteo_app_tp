package com.example.meteo_app_tp.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.ui.draw.shadow
import java.text.SimpleDateFormat
import java.util.Locale

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

    val backgroundColor = homeScreenState.weatherForecasts?.firstOrNull()?.let { forecast ->
        if (forecast.rain > 50) Color(0xFF1E3A8A)
        else Color(0xFF6A11CB)
    } ?: Color(0xFF6A11CB)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .padding(top = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "In ${homeScreenState.city}",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle Settings Action */ },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        }

        when {
            homeScreenState.isLoading -> {
                CircularProgressIndicator(color = Color.White)
            }
            homeScreenState.errorMessage != null -> {
                Text(text = "Error: ${homeScreenState.errorMessage}", color = Color.White)
            }
            homeScreenState.weatherForecasts != null && (homeScreenState.weatherForecasts as Collection<Any?>).isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        MainWeatherDisplay(homeScreenState.weatherForecasts!!.first())
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        CondensedForecastsRow(homeScreenState.weatherForecasts!!.drop(1))
                    }
                    item {
                        RemainingForecastsColumn(homeScreenState.weatherForecasts!!.drop(1))
                    }
                }
            }
            else -> {
                Text("No data available", color = Color.White)
            }
        }
    }
}



@Composable
fun MainWeatherDisplay(forecast: WeatherForecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(180.dp)
            .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${forecast.temp}°C",
            color = Color.White,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            IconText(Icons.Default.WaterDrop, "${forecast.humidity}%", "Humidity")
            Spacer(modifier = Modifier.height(8.dp))
            IconText(Icons.Default.WindPower, "${forecast.wind_speed} m/s", "Wind Speed")
            Spacer(modifier = Modifier.height(8.dp))
            IconText(Icons.Default.Thermostat, "${forecast.rain}%", "Rain Probability")
        }
    }
}


@Composable
fun CondensedForecastsRow(forecasts: List<WeatherForecast>) {
    val next12HoursForecasts = forecasts.take(12)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(16.dp))
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(next12HoursForecasts) { forecast ->
                CondensedWeatherForecastItem(forecast)
            }
        }
    }
}


@Composable
fun CondensedWeatherForecastItem(forecast: WeatherForecast) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(horizontal = 8.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatTime(forecast.time),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${forecast.temp}°C",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun RemainingForecastsColumn(forecasts: List<WeatherForecast>) {
    val remainingForecasts = forecasts.drop(12)
    val filteredForecasts = remainingForecasts.filterIndexed { index, forecast ->
        index % 12 == 0
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 16.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp), true)
            .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(filteredForecasts) { forecast ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${formatTime(forecast.time)}: ${forecast.temp}°C",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
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

fun formatTime(time: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    return try {
        val date = inputFormat.parse(time)
        if (date != null) {
            outputFormat.format(date)
        } else {
            "Invalid Time Format"
        }
    } catch (e: Exception) {
        "Invalid Time Format"
    }
}




