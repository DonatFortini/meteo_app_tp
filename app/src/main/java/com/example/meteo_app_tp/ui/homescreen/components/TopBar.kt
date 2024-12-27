package com.example.meteo_app_tp.ui.homescreen.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.R
import com.example.meteo_app_tp.data.model.CitySearchResult
import com.example.meteo_app_tp.data.repository.GeocodingRepository
import com.example.meteo_app_tp.ui.homescreen.utils.WeatherConstants
import com.example.meteo_app_tp.ui.theme.TextColorGray
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "TopBar"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    city: String,
    geocodingRepository: GeocodingRepository,
    onCitySelected: (lat: String, lon: String, cityName: String) -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<CitySearchResult>>(emptyList()) }
    var selectedCity by remember { mutableStateOf(city) }
    var isSearching by remember { mutableStateOf(false) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    // Update selectedCity when the city parameter changes
    LaunchedEffect(city) {
        selectedCity = city
    }

    // Main TopBar layout
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = WeatherConstants.DEFAULT_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                showDialog = true
                searchQuery = ""
                searchResults = emptyList()
            }
        ) {
            val locText = stringResource(id = R.string.location)
            Text(
                text = buildAnnotatedString {
                    append("$locText ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(selectedCity)
                    }
                },
                color = TextColorGray,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Change location",
                tint = TextColorGray,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(24.dp)
            )
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = TextColorGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Search Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                searchQuery = ""
                searchResults = emptyList()
            },
            title = { Text("Search City") },
            text = {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            Log.d(TAG, "Search query changed: $query")

                            // Cancel previous job if exists
                            searchJob?.cancel()

                            if (query.length >= 3) {
                                isSearching = true
                                searchJob = scope.launch {
                                    try {
                                        delay(500) // Debounce delay
                                        Log.d(TAG, "Searching for cities with query: $query")
                                        val results = geocodingRepository.searchCities(query)
                                        Log.d(TAG, "Search results received: ${results.size} cities")
                                        searchResults = results
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error searching cities", e)
                                        searchResults = emptyList()
                                    } finally {
                                        isSearching = false
                                    }
                                }
                            } else {
                                searchResults = emptyList()
                            }
                        },
                        label = { Text("Enter city name (minimum 3 characters)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .height(300.dp) // Increased height for better visibility
                            .fillMaxWidth()
                    ) {
                        when {
                            isSearching -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            searchResults.isEmpty() && searchQuery.length >= 3 -> {
                                Text(
                                    text = "No cities found",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp)
                                )
                            }
                            else -> {
                                LazyColumn {
                                    items(searchResults) { result ->
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text = "${result.name}, ${result.country}",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            modifier = Modifier
                                                .clickable {
                                                    Log.d(TAG, "City selected: ${result.name}")
                                                    selectedCity = result.name // Update local state
                                                    onCitySelected(
                                                        result.lat.toString(),
                                                        result.lon.toString(),
                                                        result.name
                                                    )
                                                    showDialog = false
                                                    searchQuery = ""
                                                    searchResults = emptyList()
                                                }
                                                .fillMaxWidth()
                                        )
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        searchQuery = ""
                        searchResults = emptyList()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}