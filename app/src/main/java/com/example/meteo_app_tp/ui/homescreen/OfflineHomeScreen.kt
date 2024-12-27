package com.example.meteo_app_tp.ui.homescreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.repository.GeocodingRepository
import com.example.meteo_app_tp.data.source.GeocodingApiDataSource
import com.example.meteo_app_tp.ui.common.SharedScreenLayout
import com.example.meteo_app_tp.ui.homescreen.components.*
import com.example.meteo_app_tp.ui.theme.getBackgroundBrush
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OfflineHomeScreen(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val currentTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    val offlineForecast = WeatherForecast(
        time = currentTime.format(formatter),
        temp = 0.0,
        humidity = 0,
        wind_speed = 0.0,
        rain = 0.0,
        app_temp = 0.0,
    )


    val offlineHourlyForecasts = List(24) { index ->
        WeatherForecast(
            time = currentTime.plusHours(index.toLong()).format(formatter),
            temp = 0.0,
            humidity = 0,
            wind_speed = 0.0,
            rain = 0.0,
            app_temp = 0.0,
        )
    }


    val offlineDailyForecasts = List(7) { index ->
        WeatherForecast(
            time = currentTime.plusDays(index.toLong()).format(formatter),
            temp = 0.0,
            humidity = 0,
            wind_speed = 0.0,
            rain = 0.0,
            app_temp = 0.0,
        )
    }

    val backgroundBrush = getBackgroundBrush(null)

    var geoc = GeocodingRepository(
        geocodingDataSource = GeocodingApiDataSource()
    )
    SharedScreenLayout(backgroundBrush = backgroundBrush) {
        WeatherContent(
            homeScreenState = HomeScreenState(
                city = "Offline Mode",
                weatherForecasts = listOf(offlineForecast) + offlineHourlyForecasts + offlineDailyForecasts,
                isLoading = false,
                isRefreshing = false,
                errorMessage = null
            ),
            onSettingsClick = onSettingsClick,
            modifier = modifier,
            geocodingRepository = geoc,
            onCitySelected = TODO()
        )
    }
}





