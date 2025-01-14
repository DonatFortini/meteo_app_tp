package com.example.meteo_app_tp.ui.components.weatherContent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.utils.DateTimeUtils

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyForecastRow(
    day: String,
    temperature: Double,
    windSpeed: Double,
    rain: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = DateTimeUtils.formatToDateOnly(day),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${temperature}°C",
                style = MaterialTheme.typography.bodyMedium,
                color = getTemperatureColor(temperature)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                WeatherDetailItem(
                    icon = Icons.Default.WaterDrop,
                    value = "${rain}mm",
                    compact = true,
                    color = if (rain > 0)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primary
                )
                WeatherDetailItem(
                    icon = Icons.Default.WindPower,
                    value = "${windSpeed}km/h",
                    compact = true,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(
    icon: ImageVector,
    value: String,
    compact: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(if (compact) 16.dp else 20.dp),
            tint = color
        )
        Text(
            text = value,
            style = if (compact)
                MaterialTheme.typography.bodySmall
            else
                MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}