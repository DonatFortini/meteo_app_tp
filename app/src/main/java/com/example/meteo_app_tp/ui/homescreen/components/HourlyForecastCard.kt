package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.ui.homescreen.components.common.StyledCard
import com.example.meteo_app_tp.ui.homescreen.utils.DateTimeUtils
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherIcons
import com.example.meteo_app_tp.ui.theme.IconTintColor
import com.example.meteo_app_tp.ui.theme.TextColorGray

@Composable
fun HourlyForecastCard(
    forecast: WeatherForecast,
    modifier: Modifier = Modifier
) {
    StyledCard(
        modifier = modifier
            .width(WeatherConstants.CONDENSED_FORECAST_CARD_WIDTH.dp)
            .height(WeatherConstants.CONDENSED_FORECAST_CARD_HEIGHT.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = DateTimeUtils.formatTime(forecast.time),
                color = TextColorGray,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${forecast.temp}°C",
                color = TextColorGray,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = WeatherIcons.getWeatherIcon(forecast),
                contentDescription = "Weather Icon",
                tint = IconTintColor,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "${forecast.rain}% rain",
                color = TextColorGray.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}