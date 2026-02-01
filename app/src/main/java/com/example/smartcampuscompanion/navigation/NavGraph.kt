// smartcampuscompanion/navigation/NavGraph.kt
package com.example.smartcampuscompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartcampuscompanion.app.AppRoutes
import com.example.smartcampuscompanion.features.auth.LoginScreen
import com.example.smartcampuscompanion.features.campusinfo.CampusInfoScreen
import com.example.smartcampuscompanion.features.dashboard.DashboardScreen
import com.example.smartcampuscompanion.utils.SessionManager

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    sessionManager: SessionManager,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Login Route
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { username ->
                    sessionManager.saveSession(username)
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Dashboard Route
        composable(AppRoutes.DASHBOARD) {
            val username = sessionManager.getUsername() ?: "Student"
            DashboardScreen(
                username = username,
                onNavigateToCampusInfo = { navController.navigate(AppRoutes.CAMPUS_INFO) },
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Campus Info Route
        composable(AppRoutes.CAMPUS_INFO) {
            CampusInfoScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}