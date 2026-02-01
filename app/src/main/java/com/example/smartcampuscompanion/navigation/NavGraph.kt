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
fun SetupNavGraph(
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
        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        // Pop up to the start destination of the graph to clear the login screen from the back stack
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppRoutes.DASHBOARD) {
            DashboardScreen(
                onNavigateToCampusInfo = {
                    navController.navigate(AppRoutes.CAMPUS_INFO)
                },
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate(AppRoutes.LOGIN) {
                        // Pop up to the start destination of the graph to clear the entire back stack
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppRoutes.CAMPUS_INFO) {
            CampusInfoScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}