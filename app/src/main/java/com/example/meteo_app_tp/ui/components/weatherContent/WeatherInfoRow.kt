package com.example.meteo_app_tp.ui.components.weatherContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun WeatherInfoRow(
    items: List<WeatherInfo>,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp)
    ) {
        items.forEach { info ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = info.icon,
                    contentDescription = info.description,
                    modifier = Modifier.size(if (compact) 16.dp else 20.dp),
                    tint = info.color ?: MaterialTheme.colorScheme.primary
                )
                Text(
                    text = info.value,
                    style = if (compact)
                        MaterialTheme.typography.bodySmall
                    else
                        MaterialTheme.typography.bodyMedium,
                    color = info.color ?: MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

data class WeatherInfo(
    val icon: ImageVector,
    val value: String,
    val description: String,
    val color: Color? = null
)