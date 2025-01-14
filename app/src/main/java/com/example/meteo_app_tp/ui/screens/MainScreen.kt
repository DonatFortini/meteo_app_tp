package com.example.meteo_app_tp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meteo_app_tp.R
import com.example.meteo_app_tp.ui.components.weatherContent.WeatherContent
import com.example.meteo_app_tp.ui.viewmodels.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    weatherViewModel: WeatherViewModel,
    onSettingsClick: () -> Unit,
    isNetworkConnected: Boolean
) {
    val state by weatherViewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(state.cityName) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (!isNetworkConnected) {
                NoInternetBanner()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.error != null -> {
                        ErrorMessage(
                            message = state.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        WeatherContent(
                            weatherState = state,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoInternetBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.9f))
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.no_internet),
            color = MaterialTheme.colorScheme.onError,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ErrorMessage(
    message: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = message ?: stringResource(R.string.unknown_error),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
    }
}