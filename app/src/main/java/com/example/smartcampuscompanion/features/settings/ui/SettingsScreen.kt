package com.example.smartcampuscompanion.features.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.smartcampuscompanion.core.ui.components.AppTopBar
import com.example.smartcampuscompanion.core.ui.components.PrimaryButton
import com.example.smartcampuscompanion.core.ui.theme.AppElevation
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing

@Composable
fun SettingsScreen(
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(title = "Settings")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppSpacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.Low),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.XLarge),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Settings features will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    PrimaryButton(
                        text = "Logout",
                        onClick = onLogout,
                        modifier = Modifier.padding(top = AppSpacing.Large)
                    )
                }
            }
        }
    }
}
