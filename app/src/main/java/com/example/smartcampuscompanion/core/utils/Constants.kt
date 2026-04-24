package com.example.smartcampuscompanion.core.utils

/**
 * Object to hold constant values used throughout the application.
 */
object Constants {
    // SharedPreferences File Name
    const val PREF_NAME = "smart_campus_prefs"

    // Session Keys
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USERNAME = "username"
    const val KEY_USER_ROLE = "user_role"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"

    // Laravel API base URL. Use 10.0.2.2 for Android emulator to reach host machine localhost.
    const val API_BASE_URL = "http://192.168.254.200:8000/api/"

}
