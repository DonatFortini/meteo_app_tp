package com.example.meteo_app_tp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants

@Composable
fun SharedScreenLayout(
    backgroundBrush: Brush,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(horizontal = WeatherConstants.DEFAULT_PADDING.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}