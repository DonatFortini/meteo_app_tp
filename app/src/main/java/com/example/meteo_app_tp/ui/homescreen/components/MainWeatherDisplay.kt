package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.homescreen.components.common.StyledBoxWithShadow
import com.example.meteo_app_tp.ui.homescreen.utils.DateTimeUtils
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherIcons
import com.example.meteo_app_tp.ui.theme.IconTintColor
import com.example.meteo_app_tp.ui.theme.TextColorGray
import com.example.meteo_app_tp.R

@Composable
fun MainWeatherDisplay(
    forecast: WeatherForecast,
    modifier: Modifier = Modifier
) {
    val mainText = stringResource( id = R.string.main_text)
    StyledBoxWithShadow(
        modifier = modifier
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp)
            .height(WeatherConstants.MAIN_WEATHER_HEIGHT.dp),
        label = mainText
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Temperature Display
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${forecast.temp}°C",
                    color = TextColorGray,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = DateTimeUtils.formatTime(forecast.time),
                    color = TextColorGray.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Weather Info
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = WeatherIcons.getWeatherIcon(forecast),
                    contentDescription = "Weather Icon",
                    tint = IconTintColor,
                    modifier = Modifier.size(48.dp)
                )
                WeatherInfoRow(
                    items = listOf(
                        WeatherInfo(Icons.Default.Water, "${forecast.humidity}%", "Humidity"),
                        WeatherInfo(Icons.Default.WindPower, "${forecast.wind_speed}km/h", "Wind Speed"),
                        WeatherInfo(Icons.Default.WaterDrop, "${forecast.rain}mm", "Rain ")
                    )
                )
            }
        }
    }
}