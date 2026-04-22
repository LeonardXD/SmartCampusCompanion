package com.example.smartcampuscompanion.features.auth.data

import android.content.Context
import android.content.SharedPreferences
import com.example.smartcampuscompanion.core.utils.Constants

/**
 * Manages authentication state.
 * Legacy helper kept for compatibility with older calls.
 */
class AuthManager(context: Context) {

    // Internal constants to ensure the app builds without external utility files
    private companion object {
        const val PREF_NAME = "SmartCampusPrefs"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USERNAME = "username"

    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Data class to return the result of a login attempt
     */
    data class AuthResult(val success: Boolean, val message: String)

    /**
     * Validates admin credentials.
     */
    fun login(username: String, password: String): AuthResult {
        if (username.isBlank() || password.isBlank()) {
            return AuthResult(false, "Please enter both username and password.")
        }

        return if (username == Constants.ADMIN_USERNAME && password == Constants.ADMIN_PASSWORD) {
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
