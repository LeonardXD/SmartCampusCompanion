// smartcampuscompanion/app/SmartCampusApp.kt
package com.example.smartcampuscompanion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.core.navigation.AppRoutes
import com.example.smartcampuscompanion.core.navigation.NavGraph
import com.example.smartcampuscompanion.core.navigation.mainBottomNavItems
import com.example.smartcampuscompanion.di.AppModule
import com.example.smartcampuscompanion.features.auth.data.SessionManager

@Composable
fun SmartCampusApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val sessionManager = remember { SessionManager(context) }
    val database = remember(context) { AppModule.provideDatabase(context) }
    val announcementDao = remember(database) { AppModule.provideAnnouncementDao(database) }
    val announcementRepository = remember(announcementDao) {
        AppModule.provideAnnouncementRepository(announcementDao)
    }
    val unreadAnnouncementCount = announcementRepository
        .getUnreadAnnouncementCount()
        .collectAsState(initial = 0)
        .value

    // Determine initial screen
    val startDestination = if (sessionManager.isLoggedIn()) {
        if (sessionManager.getRole() == "Admin") {
            AppRoutes.ANNOUNCEMENTS
        } else {
            AppRoutes.DASHBOARD
        }
    } else {
        AppRoutes.LOGIN
    }

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route
    val isAdmin = sessionManager.getRole() == "Admin"
    val shouldShowBottomBar = !isAdmin && mainBottomNavItems.any { it.route == currentRoute }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar) {
                Surface(shadowElevation = 2.dp) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp
                    ) {
                        mainBottomNavItems.forEach { item ->
                            val isSelected = currentDestination
                                ?.hierarchy
                                ?.any { it.route == item.route } == true

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    val showUnreadBadge = item.route == AppRoutes.ANNOUNCEMENTS &&
                                        unreadAnnouncementCount > 0
                                    if (showUnreadBadge) {
                                        BadgedBox(
                                            badge = {
                                                Badge {
                                                    Text(
                                                        text = if (unreadAnnouncementCount > 99) {
                                                            "99+"
                                                        } else {
                                                            unreadAnnouncementCount.toString()
                                                        }
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.label
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label
                                        )
                                    }
                                },
                                label = { Text(item.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = startDestination,
            sessionManager = sessionManager,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
