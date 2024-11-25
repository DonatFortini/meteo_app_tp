package com.example.meteo_app_tp.ui.homescreen.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.ui.theme.GlassShadowColor
import com.example.meteo_app_tp.ui.theme.TextColorGray

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