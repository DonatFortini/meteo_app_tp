package com.example.meteo_app_tp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val id: String,
    val forecasts: List<WeatherForecast>,
    val cityName: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)

data class WeatherForecast(
    val time: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val rain: Double,
    val apparentTemperature: Double
)