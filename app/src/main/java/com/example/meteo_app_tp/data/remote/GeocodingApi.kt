package com.example.meteo_app_tp.data.remote

import com.example.meteo_app_tp.data.model.NominatimResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {
    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "json"
    ): NominatimResponse
}
