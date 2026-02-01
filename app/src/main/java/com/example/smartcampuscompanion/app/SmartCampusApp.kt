// smartcampuscompanion/app/SmartCampusApp.kt
package com.example.smartcampuscompanion.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.navigation.NavGraph
import com.example.smartcampuscompanion.utils.SessionManager

@Composable
fun SmartCampusApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val sessionManager = remember { SessionManager(context) }

    // Determine initial screen
    val startDestination = if (sessionManager.isLoggedIn()) {
        AppRoutes.DASHBOARD
    } else {
        AppRoutes.LOGIN
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = startDestination,
            sessionManager = sessionManager,
            modifier = Modifier.padding(innerPadding)
        )
    }
}