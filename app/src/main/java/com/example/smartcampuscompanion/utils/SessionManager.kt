package com.example.smartcampuscompanion.utils

import android.content.Context

@Suppress("UNUSED_PARAMETER")
class SessionManager(context: Context) {
    fun isLoggedIn(): Boolean {
        // In a real app, you would check shared preferences or other storage
        return false
    }

    fun clearSession() {
        // In a real app, you would clear the session data
    }
}