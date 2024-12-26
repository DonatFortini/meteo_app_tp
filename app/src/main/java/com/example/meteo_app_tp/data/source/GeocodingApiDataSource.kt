package com.example.meteo_app_tp.data.source

import android.util.Log
import com.example.meteo_app_tp.data.model.CityResponse
import com.example.meteo_app_tp.data.model.CitySearchResult

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
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

    override suspend fun searchCities(cityName: String): List<CitySearchResult> {
        return withContext(Dispatchers.IO) {
            val encodedCityName = java.net.URLEncoder.encode(cityName, "UTF-8")
            val url = "https://nominatim.openstreetmap.org/search?q=$encodedCityName&format=json&limit=5&featureType=city"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "YourAppName/1.0 (your-email@example.com)")
                .build()

            try {
                val response = client.newCall(request).execute()
                Log.d("le.meteo_app_tp", "Search cities response code: ${response.code}")

                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    if (!jsonResponse.isNullOrEmpty()) {
                        parseCitySearchResults(jsonResponse)
                    } else {
                        emptyList()
                    }
                } else {
                    Log.e("le.meteo_app_tp", "Failed to search cities: ${response.code} ${response.message}")
                    emptyList()
                }
            } catch (e: IOException) {
                Log.e("le.meteo_app_tp", "Error while searching cities: ${e.message}", e)
                emptyList()
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

     fun parseCitySearchResults(jsonResponse: String): List<CitySearchResult> {
        return try {
            val jsonArray = JSONArray(jsonResponse)
            val results = mutableListOf<CitySearchResult>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                // Check if it's a city/town/village
                val type = item.optString("type")
                if (type in listOf("city", "town", "village", "municipality")) {
                    val displayName = item.getString("display_name")
                    val nameParts = displayName.split(", ")

                    results.add(CitySearchResult(
                        name = nameParts.firstOrNull() ?: "Unknown",
                        country = nameParts.lastOrNull() ?: "Unknown",
                        lat = item.getDouble("lat"),
                        lon = item.getDouble("lon")
                    ))
                }
            }
            results
        } catch (e: Exception) {
            Log.e("le.meteo_app_tp", "Error parsing city search results: ${e.message}", e)
            emptyList()
        }
    }
}
