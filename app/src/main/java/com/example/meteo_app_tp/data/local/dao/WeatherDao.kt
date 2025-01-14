package com.example.meteo_app_tp.data.local.dao

import androidx.room.Dao
import com.example.meteo_app_tp.data.local.entities.WeatherEntity

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE id = :id")
    suspend fun getWeather(id: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather")
    suspend fun clearWeather()
}