package com.example.smartcampuscompanion.features.auth.viewmodel

sealed interface AuthEvent {
    data class Login(
        val username: String,
        val password: String,
        val isAdminLogin: Boolean
    ) : AuthEvent

    data class Register(
        val username: String,
        val password: String,
        val confirmPassword: String
    ) : AuthEvent

    data object ClearTransientState : AuthEvent
}
