package com.example.meteo_app_tp.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meteo_app_tp.R
import com.example.meteo_app_tp.data.repository.SettingsRepository
import com.example.meteo_app_tp.ui.common.SharedScreenLayout
import com.example.meteo_app_tp.ui.settings.components.LanguageSelector
import com.example.meteo_app_tp.ui.theme.getBackgroundBrush

@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    onLanguageChanged: () -> Unit
) {
    val viewModel = remember { SettingsViewModel(settingsRepository) }
    val settingsState = viewModel.settingsState.collectAsState().value
    val isLanguageUpdated = viewModel.isLanguageUpdated.collectAsState().value
    val backgroundBrush = getBackgroundBrush(null)

    if (isLanguageUpdated) {
        onLanguageChanged()
        viewModel.resetLanguageUpdateFlag()
    }

    SharedScreenLayout(backgroundBrush = backgroundBrush) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = stringResource(R.string.language_selection),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LanguageSelector(
                selectedLanguage = settingsState.currentLanguage,
                onLanguageSelected = { language ->
                    viewModel.updateLanguage(language)
                }
            )
        }
    }
}


