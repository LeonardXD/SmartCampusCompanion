package com.example.smartcampuscompanion.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages user session using Android SharedPreferences.
 * Handles saving login state, retrieving user details, and clearing session (logout).
 *
 * @param context The context used to access SharedPreferences (uses applicationContext internally).
 */
class SessionManager(context: Context) {

    // Prevent memory leaks by using applicationContext
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        Constants.PREF_NAME,
        Context.MODE_PRIVATE
    )

    private val editor: SharedPreferences.Editor = prefs.edit()

    /**
     * Saves the user session details upon successful login.
     *
     * @param username The username of the logged-in user.
     * @param role Optional user role (e.g., "Student", "Admin"). Defaults to "Student".
     */
    fun saveSession(username: String, role: String = "Student") {
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true)
        editor.putString(Constants.KEY_USERNAME, username)
        editor.putString(Constants.KEY_USER_ROLE, role)
        editor.apply() // Apply changes asynchronously
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return True if a session exists, false otherwise.
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }

    /**
     * Retrieves the stored username.
     *
     * @return The username if logged in, null otherwise.
     */
    fun getUsername(): String? {
        return prefs.getString(Constants.KEY_USERNAME, null)
    }

    /**
     * Retrieves the stored user role.
     *
     * @return The user role if logged in, null otherwise.
     */
    fun getRole(): String? {
        return prefs.getString(Constants.KEY_USER_ROLE, null)
    }

    /**
     * Clears all session data (Logout).
     */
    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}