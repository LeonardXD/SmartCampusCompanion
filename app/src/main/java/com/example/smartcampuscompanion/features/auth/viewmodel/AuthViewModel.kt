package com.example.smartcampuscompanion.features.auth.viewmodel

import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.core.utils.Constants
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

    fun login(username: String, password: String, isAdminLogin: Boolean) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Username and password are required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val isValid = if (isAdminLogin) {
                    username == Constants.ADMIN_USERNAME &&
                        password == Constants.ADMIN_PASSWORD
                } else {
                    userRepository.authenticateStudent(username, password)
                }

                _uiState.value = if (isValid) {
                    AuthUiState.Authenticated(username)
                } else {
                    AuthUiState.Error(
                        if (isAdminLogin) {
                            "Invalid admin credentials"
                        } else {
                            "Invalid student credentials"
                        }
                    )
                }
            } catch (e: SQLiteException) {
                _uiState.value = AuthUiState.Error(
                    e.message ?: "Unable to login right now. Please try again."
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(
                    e.message ?: "Unable to login right now. Please try again."
                )
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
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
            } catch (e: SQLiteException) {
                _uiState.value = AuthUiState.Error(
                    e.message ?: "Unable to register right now. Please try again."
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(
                    e.message ?: "Unable to register right now. Please try again."
                )
            }
        }
    }

    fun clearTransientState() {
        _uiState.value = AuthUiState.Idle
    }
}
