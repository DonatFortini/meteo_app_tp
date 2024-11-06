package com.example.meteo_app_tp.data.source

import android.util.Log
import com.example.meteo_app_tp.data.model.CityResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class GeocodingApiDataSource : IGeocodingDataSource {

    private val client = OkHttpClient()

    override suspend fun getClosestCityName(lat: Double, lon: Double): CityResponse {
        return withContext(Dispatchers.IO) {
            val url = "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&format=json"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "YourAppName/1.0 (your-email@example.com)")
                .build()
            try {
                val response = client.newCall(request).execute()

                Log.d("le.meteo_app_tp", "Response code: ${response.code}")

                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    if (!jsonResponse.isNullOrEmpty()) {
                        val cityName = parseCityName(jsonResponse)
                        CityResponse.Success(cityName)
                    } else {
                        CityResponse.Error("Empty response body")
                    }
                } else {
                    CityResponse.Error("Failed to fetch city: ${response.code} ${response.message}")
                }
            } catch (e: IOException) {
                Log.e("le.meteo_app_tp", "Error while fetching city name: ${e.message}", e)
                CityResponse.Error("Network error: ${e.message}")
            }
        }
    }


    fun parseCityName(jsonResponse: String): String {
        val jsonObject = JSONObject(jsonResponse)
        val address = jsonObject.optJSONObject("address")

        return when {
            address?.has("town") == true -> address.getString("town")
            address?.has("municipality") == true -> address.getString("municipality")
            else -> {
                "Unknown City"
            }
        }
    }
}
