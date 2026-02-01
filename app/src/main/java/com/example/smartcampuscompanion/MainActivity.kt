package com.example.smartcampuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.smartcampuscompanion.app.SmartCampusApp

/**
 * Main Activity class.
 * Sets the content to SmartCampusApp which handles the rest of the navigation logic.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SmartCampusApp()
            }
        }
    }
}