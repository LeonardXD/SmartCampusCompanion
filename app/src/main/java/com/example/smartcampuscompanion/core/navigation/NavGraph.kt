// smartcampuscompanion/navigation/NavGraph.kt
package com.example.smartcampuscompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartcampuscompanion.di.AppModule
import com.example.smartcampuscompanion.di.ViewModelFactory
import com.example.smartcampuscompanion.features.auth.ui.LoginScreen
import com.example.smartcampuscompanion.features.announcements.ui.AnnouncementScreen
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementViewModel
import com.example.smartcampuscompanion.features.campusinfo.ui.CampusInfoScreen
import com.example.smartcampuscompanion.features.dashboard.ui.DashboardScreen
import com.example.smartcampuscompanion.features.auth.data.SessionManager
import com.example.smartcampuscompanion.features.taskmanager.TaskScreen
import com.example.smartcampuscompanion.features.taskmanager.TaskViewModel

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
                onNavigateToTaskManager = { navController.navigate(AppRoutes.TASK_MANAGER) },
                onNavigateToAnnouncements = { navController.navigate(AppRoutes.ANNOUNCEMENTS) },
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

        composable(AppRoutes.TASK_MANAGER) {
            val context = LocalContext.current
            val database = remember(context) { AppModule.provideDatabase(context) }
            val dao = remember(database) { AppModule.provideTaskDao(database) }
            val repository = remember(dao) { AppModule.provideTaskRepository(dao) }
            val factory = remember(repository) { ViewModelFactory { TaskViewModel(repository) } }
            val taskViewModel: TaskViewModel = viewModel(factory = factory)
            TaskScreen(viewModel = taskViewModel)
        }

        composable(AppRoutes.ANNOUNCEMENTS) {
            val context = LocalContext.current
            val database = remember(context) { AppModule.provideDatabase(context) }
            val dao = remember(database) { AppModule.provideAnnouncementDao(database) }
            val repository = remember(dao) { AppModule.provideAnnouncementRepository(dao) }
            val factory = remember(repository) { ViewModelFactory { AnnouncementViewModel(repository) } }
            val announcementViewModel: AnnouncementViewModel = viewModel(factory = factory)
            AnnouncementScreen(viewModel = announcementViewModel)
        }
    }
}
