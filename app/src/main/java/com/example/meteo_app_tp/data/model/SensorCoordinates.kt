package com.example.meteo_app_tp.data.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

class SensorCoordinates(private val fusedLocationClient: FusedLocationProviderClient) : ICoordinates {

    private var _latitude: String = ""
    private var _longitude: String = ""

    override val latitude: String
        get() = _latitude

    override val longitude: String
        get() = _longitude

    fun startLocationUpdates(context: Context, callback: (latitude: String, longitude: String) -> Unit) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    _latitude = location.latitude.toString()
                    _longitude = location.longitude.toString()
                    callback(_latitude, _longitude)
                }
            }
        }, null)
    }
}
