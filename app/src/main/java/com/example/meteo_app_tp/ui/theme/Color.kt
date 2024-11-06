package com.example.meteo_app_tp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Define primary colors
val BackgroundLightYellow = Color(0xFFFFF9C4)
val BackgroundLightBlue = Color(0xFFB3E5FC)
val BackgroundDarkGray = Color(0xFF424242)
val BackgroundWhite = Color(0xFFFFFFFF)

// Other UI colors
val ShadowColor = Color.Black.copy(alpha = 0.8f)
val CardBackgroundColor = Color(0xFF1E3A8A).copy(alpha = 0.3f)
val TextColorWhite = Color.White
val TextColorGray = Color.DarkGray


val GlassBackgroundColor = Color(0x80FFFFFF) // White with 50% opacity
val GlassShadowColor = Color(0x4D000000) // Black with 30% opacity
val FrostedGlassCardColor = Color(0x66FFFFFF) // White with 40% opacity
val IconTintColor = Color(0xCCFFFFFF) // White with 80% opacity



val getBackgroundBrush: (rain: Double?) -> Brush = { rain ->
    if (rain != null && rain > 50) {
        Brush.verticalGradient(
            colors = listOf(
                BackgroundWhite,
                BackgroundDarkGray
            ),
            startY = 0.0f,
            endY = Float.POSITIVE_INFINITY
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                BackgroundLightYellow,
                BackgroundLightBlue
            ),
            startY = 0.0f,
            endY = Float.POSITIVE_INFINITY
        )
    }
}