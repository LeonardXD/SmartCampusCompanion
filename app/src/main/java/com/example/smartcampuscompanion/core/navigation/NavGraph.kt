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
import com.example.smartcampuscompanion.features.campusinfo.viewmodel.CampusInfoViewModel
import com.example.smartcampuscompanion.features.dashboard.ui.DashboardScreen
import com.example.smartcampuscompanion.features.auth.data.SessionManager
import com.example.smartcampuscompanion.features.settings.ui.SettingsScreen
import com.example.smartcampuscompanion.features.taskmanager.TaskScreen
import com.example.smartcampuscompanion.features.taskmanager.TaskViewModel
import com.example.smartcampuscompanion.features.auth.ui.RegisterScreen
import com.example.smartcampuscompanion.core.utils.Constants
import com.example.smartcampuscompanion.features.adminprofile.ui.AdminProfileScreen

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
                    val role = if (username == Constants.ADMIN_USERNAME) "Admin" else "Student"
                    sessionManager.saveSession(username, role)
                    val destination = if (role == "Admin") {
                        AppRoutes.ANNOUNCEMENTS
                    } else {
                        AppRoutes.DASHBOARD
                    }
                    navController.navigate(destination) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Dashboard Route
        composable(AppRoutes.HOME) {
            val context = LocalContext.current
            val database = remember(context) { AppModule.provideDatabase(context) }
            val dao = remember(database) { AppModule.provideTaskDao(database) }
            val repository = remember(dao) { AppModule.provideTaskRepository(dao) }
            val username = sessionManager.getUsername() ?: "Student"
            val factory = remember(repository, username) {
                ViewModelFactory { TaskViewModel(repository, username) }
            }
            val taskViewModel: TaskViewModel = viewModel(factory = factory)
            DashboardScreen(
                username = username,
                taskViewModel = taskViewModel
            )
        }

        // Campus Info Route
        composable(AppRoutes.CAMPUS_INFO) {
            val campusInfoViewModel: CampusInfoViewModel = viewModel()
            CampusInfoScreen(viewModel = campusInfoViewModel)
        }

        composable(AppRoutes.TASKS) {
            val context = LocalContext.current
            val database = remember(context) { AppModule.provideDatabase(context) }
            val dao = remember(database) { AppModule.provideTaskDao(database) }
            val repository = remember(dao) { AppModule.provideTaskRepository(dao) }
            val username = sessionManager.getUsername() ?: ""
            val factory = remember(repository, username) {
                ViewModelFactory { TaskViewModel(repository, username) }
            }
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
            val isAdmin = sessionManager.getRole() == "Admin"
            AnnouncementScreen(
                viewModel = announcementViewModel,
                canCreate = isAdmin,
                canMarkAsRead = !isAdmin,
                canDelete = isAdmin,
                onProfileClick = {
                    if (isAdmin) {
                        navController.navigate(AppRoutes.SETTINGS)
                    }
                }
            )
        }

        composable(AppRoutes.ADMIN_PROFILE) {
            AdminProfileScreen(
                onSettingsClick = {
                    navController.navigate(AppRoutes.SETTINGS)
                }
            )
        }

        composable(AppRoutes.SETTINGS) {
            SettingsScreen(
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
