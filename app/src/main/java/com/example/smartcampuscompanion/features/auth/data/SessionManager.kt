package com.example.smartcampuscompanion.features.auth.data

import android.content.Context
import android.content.SharedPreferences
import com.example.smartcampuscompanion.core.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _darkModeFlow = MutableStateFlow(isDarkMode())
    val darkModeFlow = _darkModeFlow.asStateFlow()

    /**
     * Saves the user session details upon successful login.
     *
     * @param username The username of the logged-in user.
     * @param role Optional user role (e.g., "Student", "Admin"). Defaults to "Student".
     */
    fun saveSession(
        username: String,
        role: String = "Student",
        token: String? = null,
        userId: Long? = null
    ) {
        prefs.edit().apply {
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            putString(Constants.KEY_USERNAME, username)
            putString(Constants.KEY_USER_ROLE, role)
            putString(Constants.KEY_AUTH_TOKEN, token)
            if (userId != null) {
                putLong(Constants.KEY_USER_ID, userId)
            } else {
                remove(Constants.KEY_USER_ID)
            }
            apply()
        }

        if (userId != null) {
            migrateLegacyReadAnnouncementIds(userId)
        }
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

    fun getToken(): String? {
        return prefs.getString(Constants.KEY_AUTH_TOKEN, null)
    }

    fun getUserId(): Long? {
        return if (prefs.contains(Constants.KEY_USER_ID)) {
            prefs.getLong(Constants.KEY_USER_ID, 0L)
        } else {
            null
        }
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean("key_dark_mode", false)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit()
            .putBoolean("key_dark_mode", enabled)
            .apply()
        _darkModeFlow.value = enabled
    }

    fun getReadAnnouncementIds(): Set<Long> {
        val set = prefs.getStringSet(readAnnouncementKey(), emptySet()) ?: emptySet()
        return set.mapNotNull { it.toLongOrNull() }.toSet()
    }

    fun saveReadAnnouncementId(id: Long) {
        val current = prefs.getStringSet(readAnnouncementKey(), emptySet()) ?: emptySet()
        val updated = current + id.toString()
        prefs.edit()
            .putStringSet(readAnnouncementKey(), updated)
            .apply()
    }

    /**
     * Clears all session data (Logout).
     */
    fun clearSession() {
        prefs.edit().apply {
            remove(Constants.KEY_IS_LOGGED_IN)
            remove(Constants.KEY_USERNAME)
            remove(Constants.KEY_USER_ROLE)
            remove(Constants.KEY_AUTH_TOKEN)
            remove(Constants.KEY_USER_ID)
            apply()
        }
    }

    private fun readAnnouncementKey(userId: Long? = getUserId()): String {
        return if (userId != null) {
            "$KEY_READ_ANNOUNCEMENTS_PREFIX$userId"
        } else {
            KEY_READ_ANNOUNCEMENTS_LEGACY
        }
    }

    private fun migrateLegacyReadAnnouncementIds(userId: Long) {
        val legacyReadIds = prefs.getStringSet(KEY_READ_ANNOUNCEMENTS_LEGACY, emptySet()).orEmpty()
        if (legacyReadIds.isEmpty()) return

        val userKey = readAnnouncementKey(userId)
        val currentUserReadIds = prefs.getStringSet(userKey, emptySet()).orEmpty()
        if (currentUserReadIds.isNotEmpty()) return

        prefs.edit()
            .putStringSet(userKey, legacyReadIds)
            .apply()
    }

    private companion object {
        const val KEY_READ_ANNOUNCEMENTS_LEGACY = "key_read_announcements"
        const val KEY_READ_ANNOUNCEMENTS_PREFIX = "key_read_announcements_user_"
    }
}
