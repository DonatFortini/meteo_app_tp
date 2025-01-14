package com.example.meteo_app_tp.ui.components.weatherContent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.R
import com.example.meteo_app_tp.data.local.entities.WeatherForecast
import com.example.meteo_app_tp.utils.DateTimeUtils
import com.example.meteo_app_tp.utils.WeatherConstants

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainWeatherDisplay(
    mainForecast: WeatherForecast,
    modifier: Modifier = Modifier
) {
    StyledBoxWithShadow(
        modifier = modifier
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp)
            .height(WeatherConstants.MAIN_WEATHER_HEIGHT.dp),
        label = stringResource(R.string.current_weather),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = DateTimeUtils.formatToHourOnly(mainForecast.time),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier)
                Text(
                    text = "${mainForecast.temperature}°C",
                    style = MaterialTheme.typography.displayLarge,
                    color = getTemperatureColor(mainForecast.temperature)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherInfoRow(
                    items = listOf(
                        WeatherInfo(
                            Icons.Default.Water,
                            "${mainForecast.humidity}%",
                            stringResource(R.string.humidity),
                            color = MaterialTheme.colorScheme.primary
                        ),
                        WeatherInfo(
                            Icons.Default.Air,
                            "${mainForecast.windSpeed}km/h",
                            stringResource(R.string.wind_speed),
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        WeatherInfo(
                            Icons.Default.WaterDrop,
                            "${mainForecast.rain}mm",
                            stringResource(R.string.rain),
                            color = if (mainForecast.rain > 0)
                                MaterialTheme.colorScheme.tertiary
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    )
                )
            }
        }
    }
}