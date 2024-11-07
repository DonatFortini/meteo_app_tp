package com.example.meteo_app_tp.ui.homescreen

import android.icu.number.IntegerWidth
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.theme.*
import java.util.Locale

@Composable
fun HomeScreen(
    lat: String,
    lon: String,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()
    LaunchedEffect(lat, lon) { viewModel.fetchWeatherData(lat, lon) }

    val rainValue = homeScreenState.weatherForecasts?.firstOrNull()?.rain
    val backgroundBrush = getBackgroundBrush(rainValue)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(16.dp)
            .padding(top = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            TopBar(homeScreenState.city)

            when {
                homeScreenState.isLoading -> CircularProgressIndicator(color = TextColorGray)
                homeScreenState.errorMessage != null -> Text("Error: ${homeScreenState.errorMessage}", color = TextColorGray)
                !homeScreenState.weatherForecasts.isNullOrEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        item { MainWeatherDisplay(homeScreenState.weatherForecasts!!.first()) }
                        item { CondensedForecastsRow(homeScreenState.weatherForecasts!!.drop(1)) }
                        item { RemainingForecastsColumn(homeScreenState.weatherForecasts!!.drop(1)) }
                    }
                }
                else -> Text("No data available", color = TextColorGray)
            }
        }
    }
}

@Composable
fun TopBar(city: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "In $city",
            color = TextColorGray,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        IconButton(onClick = { /* Handle Settings Action */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = TextColorGray
            )
        }
    }
}
// TODO fix the hour get the right hour for forecast
@Composable
fun MainWeatherDisplay(forecast: WeatherForecast) {
    val icon = getWeatherIcon(forecast)
    StyledBoxWithShadow(
        modifier = Modifier
            .padding(16.dp)
            .height(180.dp),
        label = "Today's Forecast"
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "${forecast.temp}°C",
                color = TextColorGray,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Icon(icon, contentDescription = "Weather Icon", tint = IconTintColor)
                IconText(Icons.Default.Water, "${forecast.humidity}%", "Humidity")
                Spacer(modifier = Modifier.height(8.dp))
                IconText(Icons.Default.WindPower, "${forecast.wind_speed} m/s", "Wind Speed")
                Spacer(modifier = Modifier.height(8.dp))
                IconText(Icons.Default.WaterDrop, "${forecast.rain}%", "Rain Probability")
            }
        }
    }
}

@Composable
fun CondensedForecastsRow(forecasts: List<WeatherForecast>) {
    StyledBoxWithShadow(
        modifier = Modifier
            .height(200.dp)
            .padding(16.dp),
        label = "Next 12 Hours"
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            //TODO fix this
            items(forecasts.take(12)) { forecast -> CondensedWeatherForecastItem(forecast) }
        }
    }
}

@Composable
fun CondensedWeatherForecastItem(forecast: WeatherForecast) {
    val icon = getWeatherIcon(forecast)
    StyledCard(modifier = Modifier.width(160.dp).height(140.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(formatTime(forecast.time), color = TextColorGray, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("${forecast.temp}°C", color = TextColorGray, style = MaterialTheme.typography.bodyLarge)
            Icon(icon, contentDescription = "Weather Icon", tint = IconTintColor)
        }
    }
}

@Composable
fun RemainingForecastsColumn(forecasts: List<WeatherForecast>) {
    val remainingForecasts = forecasts.drop(12).filterIndexed { index, _ -> index % 24 == 0 }
    StyledBoxWithShadow(
        modifier = Modifier
            .height(300.dp)
            .padding(16.dp),
        label = "Upcoming Days"
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(remainingForecasts) { forecast ->
                val icon = getWeatherIcon(forecast)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(CardBackgroundColor, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${formatTime(forecast.time, onlyDay = true)}: ${forecast.temp}°C", color = TextColorGray)
                    Icon(icon, contentDescription = "Weather Icon", tint = IconTintColor)
                }
            }
        }
    }
}


@Composable
fun StyledBoxWithShadow(
    modifier: Modifier = Modifier,
    label: String,
    width: Dp =500.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .width(width)
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .background(GlassShadowColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(label, color = TextColorGray, style = MaterialTheme.typography.bodyMedium)
        Box(content = content)
    }
}

@Composable
fun StyledCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}

@Composable
fun IconText(icon: ImageVector, text: String, contentDescription: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription, tint = IconTintColor)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, color = TextColorGray, style = MaterialTheme.typography.bodyMedium)
    }
}

fun formatTime(time: String, onlyDay: Boolean = false): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val outputFormat = if (onlyDay) SimpleDateFormat("dd MMM", Locale.getDefault())
    else SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    return runCatching { outputFormat.format(inputFormat.parse(time)!!) }.getOrDefault("Invalid Time Format")
}

fun getWeatherIcon(weather: WeatherForecast): ImageVector {
    return when {
        weather.temp > 25 && weather.rain < 5 && weather.humidity < 50 -> Icons.Default.WbSunny
        weather.temp in 15.0..25.0 && weather.rain < 10 && weather.humidity in 50..75 -> Icons.Default.Cloud
        weather.rain > 20 && weather.humidity > 80 -> Icons.Default.WaterDrop
        weather.wind_speed > 20 && weather.rain > 10 -> Icons.Default.Bolt
        weather.rain in 5.0..20.0 && weather.humidity in 60..80 -> Icons.Default.Cloud
        weather.temp in 10.0..15.0 && weather.rain < 5 && weather.humidity in 40..60 -> Icons.Default.WbCloudy
        weather.temp > 35 -> Icons.Default.WbSunny
        else -> Icons.Default.Bolt
    }
}
