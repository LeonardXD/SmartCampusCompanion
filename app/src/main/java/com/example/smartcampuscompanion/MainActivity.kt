package com.example.smartcampuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.smartcampuscompanion.core.ui.theme.SmartCampusCompanionTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.smartcampuscompanion.features.auth.data.SessionManager

/**
 * Main Activity class.
 * Sets the content to SmartCampusApp which handles the rest of the navigation logic.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val sessionManager = remember { SessionManager(context) }
            val isDarkMode by sessionManager.darkModeFlow.collectAsState()

            SmartCampusCompanionTheme(darkTheme = isDarkMode) {
                SmartCampusApp(sessionManager = sessionManager)
            }
        }
    }
}
