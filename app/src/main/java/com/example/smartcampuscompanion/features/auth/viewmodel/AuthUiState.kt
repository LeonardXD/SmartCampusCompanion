package com.example.smartcampuscompanion.features.auth.viewmodel

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Authenticated(val username: String, val role: String) : AuthUiState
    data object Registered : AuthUiState
    data class Error(val message: String) : AuthUiState
}
