package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.homescreen.utils.DateTimeUtils
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherIcons
import com.example.meteo_app_tp.ui.theme.CardBackgroundColor
import com.example.meteo_app_tp.ui.theme.IconTintColor
import com.example.meteo_app_tp.ui.theme.TextColorGray

@Composable
fun DailyForecastRow(
    forecast: WeatherForecast,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = CardBackgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = DateTimeUtils.formatTime(forecast.time, onlyDay = true),
                color = TextColorGray,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${forecast.temp}°C",
                color = TextColorGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherInfoRow(
                items = listOf(
                    WeatherInfo(Icons.Default.WaterDrop, "${forecast.rain}mm", "Rain"),
                    WeatherInfo(Icons.Default.WindPower, "${forecast.wind_speed}km/h", "Wind")
                ),
                compact = true
            )

            Icon(
                imageVector = WeatherIcons.getWeatherIcon(forecast),
                contentDescription = "Weather Icon",
                tint = IconTintColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}