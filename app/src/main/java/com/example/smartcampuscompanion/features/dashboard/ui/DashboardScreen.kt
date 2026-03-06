// smartcampuscompanion/features/dashboard/DashboardScreen.kt
package com.example.smartcampuscompanion.features.dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.core.ui.components.AppTopBar
import com.example.smartcampuscompanion.core.ui.components.PrimaryButton

@Composable
fun DashboardScreen(
    username: String,
    onNavigateToCampusInfo: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Dashboard",
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Welcome, $username!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Manage your campus life easily.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = "Menu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )

            PrimaryButton(
                text = "View Campus Info",
                onClick = onNavigateToCampusInfo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder for future Midterm features (Task Manager, etc.)
            OutlinedButton(
                onClick = { /* TODO: Midterm Requirement */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = false
            ) {
                Text("Task Manager (Coming Soon)")
            }
        }
    }
}
