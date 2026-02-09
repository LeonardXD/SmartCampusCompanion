package com.example.smartcampuscompanion.features.auth.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages authentication state and mock validation.
 * Designed for PRELIM requirements (Local Mock Auth).
 */
class AuthManager(context: Context) {

    // Internal constants to ensure the app builds without external utility files
    private companion object {
        const val PREF_NAME = "SmartCampusPrefs"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USERNAME = "username"

        // MOCK CREDENTIALS
        const val MOCK_USER = "student"
        const val MOCK_PASS = "password123"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Data class to return the result of a login attempt
     */
    data class AuthResult(val success: Boolean, val message: String)

    /**
     * Validates credentials against hardcoded mock data.
     * If valid, saves the session.
     */
    fun login(username: String, password: String): AuthResult {
        if (username.isBlank() || password.isBlank()) {
            return AuthResult(false, "Please enter both username and password.")
        }

        // Mock Validation Logic
        return if (username == MOCK_USER && password == MOCK_PASS) {
            saveSession(username)
            AuthResult(true, "Login Successful")
        } else {
            AuthResult(false, "Invalid username or password.")
        }
    }

    /**
     * Persists the login state to SharedPreferences.
     */
    private fun saveSession(username: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    /**
     * Clears the session data.
     */
    fun logout() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }

    /**
     * Checks if a user is currently logged in.
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Returns the current username or null if not logged in.
     */
    fun getCurrentUser(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }
}