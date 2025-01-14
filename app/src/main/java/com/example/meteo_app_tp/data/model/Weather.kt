package com.example.meteo_app_tp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val hourly: Hourly,
    @SerializedName("hourly_units") val hourlyUnits: HourlyUnits? = null,
    @SerializedName("generationtime_ms") val generationTimeMs: Double? = null,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int? = null,
    val timezone: String? = null,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String? = null,
    val elevation: Double? = null
)

data class Hourly(
    val time: List<String>,
    @SerializedName("temperature_2m") val temperature_2m: List<Double?>,
    @SerializedName("relative_humidity_2m") val relative_humidity_2m: List<Int?>,
    @SerializedName("apparent_temperature") val apparent_temperature: List<Double?>,
    val rain: List<Double?>,  // Made nullable
    @SerializedName("wind_speed_10m") val wind_speed_10m: List<Double?>
)

data class HourlyUnits(
    val time: String,
    @SerializedName("temperature_2m") val temperature2m: String,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: String,
    @SerializedName("apparent_temperature") val apparentTemperature: String,
    val rain: String,
    @SerializedName("wind_speed_10m") val windSpeed10m: String
)