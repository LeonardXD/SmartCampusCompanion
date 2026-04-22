package com.example.smartcampuscompanion.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val mainBottomNavItems = listOf(
    BottomNavItem(route = AppRoutes.HOME, label = "Home", icon = Icons.Filled.Home),
    BottomNavItem(route = AppRoutes.TASKS, label = "Tasks", icon = Icons.Filled.CheckCircle),
    BottomNavItem(
        route = AppRoutes.ANNOUNCEMENTS,
        label = "Updates",
        icon = Icons.Filled.Notifications
    ),
    BottomNavItem(route = AppRoutes.CAMPUS_INFO, label = "Campus", icon = Icons.Filled.Info),
    BottomNavItem(route = AppRoutes.SETTINGS, label = "Settings", icon = Icons.Filled.Settings)
)
