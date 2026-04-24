package com.example.smartcampuscompanion.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(
                username = event.username,
                password = event.password,
                isAdminLogin = event.isAdminLogin
            )
            is AuthEvent.Register -> register(
                username = event.username,
                password = event.password,
                confirmPassword = event.confirmPassword
            )
            AuthEvent.ClearTransientState -> clearTransientState()
        }
    }

    private fun login(username: String, password: String, isAdminLogin: Boolean) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Username and password are required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                when (val result = userRepository.login(username, password)) {
                    is UserRepository.LoginResult.Success -> {
                        if (isAdminLogin && result.role != "Admin") {
                            _uiState.value = AuthUiState.Error("This account is not an admin account.")
                        } else if (!isAdminLogin && result.role == "Admin") {
                            _uiState.value = AuthUiState.Error("Use Admin Login for admin accounts.")
                        } else {
                            _uiState.value = AuthUiState.Authenticated(
                                username = result.username,
                                role = result.role
                            )
                        }
                    }
                    is UserRepository.LoginResult.Error -> {
                        _uiState.value = AuthUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(
                    e.message ?: "Unable to login right now. Please try again."
                )
            }
        }
    }

    private fun register(username: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Passwords do not match.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                when (val result = userRepository.registerStudent(username, password)) {
                    is UserRepository.RegisterResult.Success -> {
                        _uiState.value = AuthUiState.Registered
                    }

                    is UserRepository.RegisterResult.Error -> {
                        _uiState.value = AuthUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(
                    e.message ?: "Unable to register right now. Please try again."
                )
            }
        }
    }

    private fun clearTransientState() {
        _uiState.value = AuthUiState.Idle
    }
}
