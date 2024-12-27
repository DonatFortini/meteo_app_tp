package com.example.meteo_app_tp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meteo_app_tp.data.WeatherCacheManager
import com.example.meteo_app_tp.data.model.SensorCoordinates
import com.example.meteo_app_tp.data.repository.SettingsRepository
import com.example.meteo_app_tp.ui.ConditionalWeatherScreen
import com.example.meteo_app_tp.ui.settings.SettingsScreen
import com.example.meteo_app_tp.ui.settings.utils.LocaleHelper
import com.example.meteo_app_tp.ui.theme.Meteo_app_tpTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat = mutableStateOf("")
    private var currentLon = mutableStateOf("")
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var weatherCacheManager: WeatherCacheManager

    override fun attachBaseContext(newBase: Context) {
        settingsRepository = SettingsRepository(newBase.dataStore)
        val language = runBlocking {
            settingsRepository.getCurrentLanguage().first()
        }
        super.attachBaseContext(LocaleHelper.setLocale(newBase, language.code.toString()))
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                showPermissionDeniedMessage()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        weatherCacheManager = WeatherCacheManager(
            getSharedPreferences(WeatherCacheManager.PREFS_NAME, MODE_PRIVATE)
        )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()

        setContent {
            val navController = rememberNavController()

            Meteo_app_tpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("home") {
                            ConditionalWeatherScreen(
                                lat = currentLat.value,
                                lon = currentLon.value,
                                onSettingsClick = { navController.navigate("settings") },
                                cacheManager = weatherCacheManager
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                settingsRepository = settingsRepository,
                                onLanguageChanged = {
                                    runOnUiThread {
                                        recreate()
                                        navController.navigate("home")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
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
            currentLat.value = lat
            currentLon.value = lon
        }
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(
            this,
            "Location permission is required to display weather information.",
            Toast.LENGTH_LONG
        ).show()
    }
}