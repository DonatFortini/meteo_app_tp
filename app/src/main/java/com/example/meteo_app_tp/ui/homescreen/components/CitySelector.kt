package com.example.meteo_app_tp.ui.homescreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.data.model.CitySearchResult
import com.example.meteo_app_tp.data.repository.GeocodingRepository
import kotlinx.coroutines.launch

@Composable
fun CitySelector(
    currentCity: String,
    geocodingRepository: GeocodingRepository,
    onCitySelected: (lat: String, lon: String, cityName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<CitySearchResult>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentCity,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .clickable { showDialog = true }
                .padding(8.dp)
        )
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Change city",
            modifier = Modifier
                .clickable { showDialog = true }
                .padding(8.dp)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Search City") },
            text = {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            scope.launch {
                                if (query.length >= 3) {
                                    searchResults = geocodingRepository.searchCities(query)
                                }
                            }
                        },
                        label = { Text("Enter city name") }
                    )
                    LazyColumn(
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(searchResults) { result ->
                            Text(
                                text = "${result.name}, ${result.country}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCitySelected(
                                            result.lat.toString(),
                                            result.lon.toString(),
                                            result.name
                                        )
                                        showDialog = false
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}