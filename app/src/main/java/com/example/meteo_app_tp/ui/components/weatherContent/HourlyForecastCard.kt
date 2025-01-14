package com.example.meteo_app_tp.ui.components.weatherContent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.utils.DateTimeUtils

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastCard(
    hour: String,
    temperature: Double,
    rainChance: Double,
    modifier: Modifier = Modifier
) {
    val rainColor = when {
        rainChance > 70 -> MaterialTheme.colorScheme.error
        rainChance > 30 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .width(100.dp)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = DateTimeUtils.formatToHourOnly(hour),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${temperature}°C",
                style = MaterialTheme.typography.bodyLarge,
                color = getTemperatureColor(temperature)
            )
            Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = "Rain chance",
                modifier = Modifier.size(24.dp),
                tint = rainColor
            )
            Text(
                text = "${rainChance}mm",
                style = MaterialTheme.typography.bodySmall,
                color = rainColor
            )
        }
    }
}

@Composable
fun getTemperatureColor(temperature: Double): Color {
    return when {
        temperature >= 30 -> MaterialTheme.colorScheme.error
        temperature >= 25 -> MaterialTheme.colorScheme.tertiary
        temperature <= 0 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }
}