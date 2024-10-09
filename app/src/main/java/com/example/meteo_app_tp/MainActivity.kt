package com.example.meteo_app_tp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.meteo_app_tp.data.model.SensorCoordinates
import com.example.meteo_app_tp.ui.homescreen.HomeScreen
import com.example.meteo_app_tp.ui.theme.Meteo_app_tpTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat: String = ""
    private var currentLon: String = ""

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                // Handle the case where permission is not granted
                // E.g., show a message to the user
                showPermissionDeniedMessage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()
        setContent {
            Meteo_app_tpTheme {
                HomeScreen(
                    lat = currentLat,
                    lon = currentLon
                )
            }
        }
    }

    private fun requestLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun startLocationUpdates() {
        val sensorCoordinates = SensorCoordinates(fusedLocationClient)
        sensorCoordinates.startLocationUpdates(this) { lat, lon ->
            currentLat = lat
            currentLon = lon
        }
    }

    private fun showPermissionDeniedMessage() {
        // Implement your logic to show a message to the user
    }
}
