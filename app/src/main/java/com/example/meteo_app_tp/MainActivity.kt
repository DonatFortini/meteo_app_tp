package com.example.meteo_app_tp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meteo_app_tp.data.local.SettingsManager
import com.example.meteo_app_tp.data.local.WeatherDatabase
import com.example.meteo_app_tp.data.remote.GeocodingService
import com.example.meteo_app_tp.data.remote.WeatherRemoteDataSource
import com.example.meteo_app_tp.data.repository.WeatherRepository
import com.example.meteo_app_tp.ui.screens.MainScreen
import com.example.meteo_app_tp.ui.screens.SettingsScreen
import com.example.meteo_app_tp.ui.theme.WeatherAppTheme
import com.example.meteo_app_tp.ui.viewmodels.SettingsViewModel
import com.example.meteo_app_tp.ui.viewmodels.WeatherViewModel
import com.example.meteo_app_tp.utils.NetworkConnectivityManager
import com.google.android.gms.location.LocationServices
import java.util.Locale

sealed class Screen(val route: String) {
    object Weather : Screen("weather")
    object Settings : Screen("settings")
}

class MainActivity : ComponentActivity() {
    private lateinit var db: WeatherDatabase
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var settingsManager: SettingsManager
    private lateinit var networkConnectivityManager: NetworkConnectivityManager
    private lateinit var geocodingService: GeocodingService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = WeatherDatabase.getDatabase(applicationContext)
        geocodingService = GeocodingService()
        weatherRepository = WeatherRepository(
            db.weatherDao(),
            WeatherRemoteDataSource(),
            geocodingService
        )
        val settingsManager = SettingsManager(applicationContext)
        val language = settingsManager.settingsFlow.value.language
        updateLocale(language)
        networkConnectivityManager = NetworkConnectivityManager(this)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        networkConnectivityManager.startMonitoring()

        setContent {
            val navController = rememberNavController()
            val settingsViewModel = viewModel {
                SettingsViewModel(settingsManager)
            }
            val weatherViewModel = viewModel {
                WeatherViewModel(
                    context = applicationContext,
                    weatherRepository = weatherRepository,
                    fusedLocationClient = fusedLocationClient,
                    networkConnectivityManager = networkConnectivityManager
                )
            }
            val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
            val networkState by networkConnectivityManager.isConnected.collectAsStateWithLifecycle()

            LaunchedEffect(settingsState.language) {
                updateLocale(settingsState.language)
            }

            WeatherAppTheme(darkTheme = settingsState.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Weather.route
                        ) {
                            composable(Screen.Weather.route) {
                                MainScreen(
                                    weatherViewModel = weatherViewModel,
                                    onSettingsClick = {
                                        navController.navigate(Screen.Settings.route)
                                    },
                                    isNetworkConnected = networkState
                                )
                            }
                            composable(Screen.Settings.route) {
                                SettingsScreen(
                                    settingsViewModel = settingsViewModel,
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = resources.configuration
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}