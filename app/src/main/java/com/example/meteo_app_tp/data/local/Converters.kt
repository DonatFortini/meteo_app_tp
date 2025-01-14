package com.example.meteo_app_tp.data.local

import androidx.room.TypeConverter
import com.example.meteo_app_tp.data.local.entities.WeatherForecast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromForecastList(value: List<WeatherForecast>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toForecastList(value: String): List<WeatherForecast> {
        val listType = object : TypeToken<List<WeatherForecast>>() {}.type
        return Gson().fromJson(value, listType)
    }
}