package com.example.smartcampuscompanion.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.smartcampuscompanion.core.utils.Constants
import com.example.smartcampuscompanion.data.local.dao.UserDao
import com.example.smartcampuscompanion.data.local.entity.UserEntity

class UserRepository(
    private val dao: UserDao
) {
    suspend fun authenticateStudent(username: String, password: String): Boolean {
        val user = dao.getUserByUsernameAndRole(username = username, role = STUDENT_ROLE)
        return user?.password == password
    }

    suspend fun registerStudent(username: String, password: String): RegisterResult {
        val sanitizedUsername = username.trim()
        if (sanitizedUsername.isBlank() || password.isBlank()) {
            return RegisterResult.Error("Username and password are required.")
        }
        if (sanitizedUsername.equals(Constants.ADMIN_USERNAME, ignoreCase = true)) {
            return RegisterResult.Error("This username is reserved.")
        }

        return try {
            dao.insertUser(
                UserEntity(
                    username = sanitizedUsername,
                    password = password,
                    role = STUDENT_ROLE
                )
            )
            RegisterResult.Success
        } catch (_: SQLiteConstraintException) {
            RegisterResult.Error("Username already exists.")
        } catch (_: IllegalStateException) {
            RegisterResult.Error("Username already exists.")
        }
    }

    sealed class RegisterResult {
        data object Success : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    private companion object {
        const val STUDENT_ROLE = "Student"
    }
}
