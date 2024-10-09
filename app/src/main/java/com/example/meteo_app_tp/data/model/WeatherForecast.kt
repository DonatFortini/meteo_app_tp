package com.example.meteo_app_tp.data.model



    data class WeatherForecast(
        val time:String,
        val temp: Double,
        val humidity:Int,
        val app_temp:Double,
        val rain: Double,
        val wind_speed:Double
    )

