package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import com.example.meteo_app_tp.ui.theme.IconTintColor
import com.example.meteo_app_tp.ui.theme.TextColorGray
import androidx.compose.material3.MaterialTheme
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
                    tint = IconTintColor,
                    modifier = Modifier.size(if (compact) 16.dp else 20.dp)
                )
                Text(
                    text = info.value,
                    color = TextColorGray,
                    style = if (compact)
                        MaterialTheme.typography.bodySmall
                    else
                        MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class WeatherInfo(
    val icon: ImageVector,
    val value: String,
    val description: String
)