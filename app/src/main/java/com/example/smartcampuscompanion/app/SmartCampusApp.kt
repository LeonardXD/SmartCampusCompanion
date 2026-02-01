package com.example.smartcampuscompanion.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.app.AppRoutes
import com.example.smartcampuscompanion.utils.Constants
import com.example.smartcampuscompanion.utils.SessionManager

/**
 * The main entry point for the Compose UI.
 * Handles the navigation graph and initial session check.
 */
@Composable
fun SmartCampusApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Initialize SessionManager.
    // We use remember so it isn't recreated on every recomposition.
    val sessionManager = remember { SessionManager(context) }

    // Determine the start destination based on the current session state.
    // This is calculated once when the composable enters the composition.
    val startDestination = if (sessionManager.isLoggedIn()) {
        AppRoutes.DASHBOARD
    } else {
        AppRoutes.LOGIN
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // Login Route
            composable(route = AppRoutes.LOGIN) {
                LoginScreenPlaceholder(
                    onLoginSuccess = { username ->
                        // 1. Save Session
                        sessionManager.saveSession(username)

                        // 2. Navigate to Dashboard and clear backstack
                        navController.navigate(AppRoutes.DASHBOARD) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // Dashboard Route
            composable(route = AppRoutes.DASHBOARD) {
                DashboardScreenPlaceholder(
                    username = sessionManager.getUsername() ?: "User",
                    onLogout = {
                        // 1. Clear Session
                        sessionManager.clearSession()

                        // 2. Navigate to Login and clear backstack
                        navController.navigate(AppRoutes.LOGIN) {
                            // popUpTo(0) clears the entire backstack
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// --- Placeholder Composables (as requested per requirements) ---

@Composable
fun LoginScreenPlaceholder(onLoginSuccess: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Simulate a successful login with mock credentials
            onLoginSuccess(Constants.MOCK_USERNAME)
        }) {
            Text(text = "Log In")
        }
    }
}

@Composable
fun DashboardScreenPlaceholder(username: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, $username!", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Dashboard", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLogout) {
            Text(text = "Log Out")
        }
    }
}