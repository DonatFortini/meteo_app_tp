package com.example.meteo_app_tp.ui.homescreen.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.meteo_app_tp.data.model.WeatherForecast

object WeatherIcons {
    fun getWeatherIcon(weather: WeatherForecast): ImageVector {
        return when {
            isSunnyDay(weather) -> Icons.Default.WbSunny
            isPartlyCloudy(weather) -> Icons.Default.Cloud
            isRainy(weather) -> Icons.Default.WaterDrop
            isStormy(weather) -> Icons.Default.Bolt
            isLightRain(weather) -> Icons.Default.Cloud
            isMildWeather(weather) -> Icons.Default.WbCloudy
            isHotDay(weather) -> Icons.Default.WbSunny
            else -> Icons.Default.Bolt
        }
    }

    private fun isSunnyDay(weather: WeatherForecast) =
        weather.temp > 25 && weather.rain < 5 && weather.humidity < 50

    private fun isPartlyCloudy(weather: WeatherForecast) =
        weather.temp in 15.0..25.0 && weather.rain < 10 && weather.humidity in 50..75

    private fun isRainy(weather: WeatherForecast) =
        weather.rain > 20 && weather.humidity > 80

    private fun isStormy(weather: WeatherForecast) =
        weather.wind_speed > 20 && weather.rain > 10

    private fun isLightRain(weather: WeatherForecast) =
        weather.rain in 5.0..20.0 && weather.humidity in 60..80

    private fun isMildWeather(weather: WeatherForecast) =
        weather.temp in 10.0..15.0 && weather.rain < 5 && weather.humidity in 40..60

    private fun isHotDay(weather: WeatherForecast) =
        weather.temp > 35
}