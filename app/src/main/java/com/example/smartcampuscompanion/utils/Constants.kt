package com.example.smartcampuscompanion.utils

/**
 * Object to hold constant values used throughout the application,
 * including SharedPreferences keys and mock authentication credentials.
 */
object Constants {
    // SharedPreferences File Name
    const val PREF_NAME = "smart_campus_prefs"

    // Session Keys
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USERNAME = "username"
    const val KEY_USER_ROLE = "user_role"

    // Mock Credentials (Hardcoded for prelim requirements)
    const val MOCK_USERNAME = "student"
    const val MOCK_PASSWORD = "password123"

    // Optional: Mock Admin Credentials
    const val MOCK_ADMIN_USER = "admin"
    const val MOCK_ADMIN_PASS = "admin123"
}