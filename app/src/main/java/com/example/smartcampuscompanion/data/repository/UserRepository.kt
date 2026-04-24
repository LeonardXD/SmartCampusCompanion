package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.remote.CampusApiService
import com.example.smartcampuscompanion.data.remote.dto.LoginRequestDto
import com.example.smartcampuscompanion.data.remote.dto.RegisterRequestDto
import com.example.smartcampuscompanion.features.auth.data.SessionManager
import retrofit2.HttpException

class UserRepository(
    private val api: CampusApiService,
    private val sessionManager: SessionManager
) {
    suspend fun login(identifier: String, password: String): LoginResult {
        return try {
            val response = api.login(
                LoginRequestDto(
                    identifier = identifier.trim(),
                    password = password
                )
            )
            val username = response.user.username ?: response.user.email
            val role = response.user.role ?: "Student"
            sessionManager.saveSession(
                username = username,
                role = role,
                token = response.token,
                userId = response.user.id
            )
            LoginResult.Success(
                username = username,
                role = role
            )
        } catch (e: HttpException) {
            val message = if (e.code() == 401) {
                "Invalid credentials. Please check your username and password."
            } else {
                e.message()
            }
            LoginResult.Error(message)
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Unable to login right now. Please try again.")
        }
    }

    suspend fun registerStudent(username: String, password: String): RegisterResult {
        val sanitizedUsername = username.trim()
        if (sanitizedUsername.isBlank() || password.isBlank()) {
            return RegisterResult.Error("Username and password are required.")
        }

        return try {
            val sanitizedEmail = "${sanitizedUsername.lowercase()}@smartcampus.local"
            api.register(
                RegisterRequestDto(
                    name = sanitizedUsername,
                    username = sanitizedUsername,
                    email = sanitizedEmail,
                    password = password,
                    role = STUDENT_ROLE
                )
            )
            RegisterResult.Success
        } catch (e: HttpException) {
            RegisterResult.Error(
                e.message() ?: "Unable to register right now. Please try again."
            )
        } catch (e: Exception) {
            RegisterResult.Error(
                e.message ?: "Unable to register right now. Please try again."
            )
        }
    }

    suspend fun logout() {
        try {
            api.logout()
        } catch (_: Exception) {
            // Best effort logout.
        } finally {
            sessionManager.clearSession()
        }
    }

    sealed class LoginResult {
        data class Success(val username: String, val role: String) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    sealed class RegisterResult {
        data object Success : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    private companion object {
        const val STUDENT_ROLE = "Student"
    }
}
