package com.example.smartcampuscompanion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val identifier: String,
    val password: String
)

data class RegisterRequestDto(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val role: String = "Student"
)

data class AuthResponseDto(
    val user: UserDto,
    val token: String,
    @SerializedName("token_type") val tokenType: String? = null
)

data class UserDto(
    val id: Long,
    val name: String,
    val username: String?,
    val email: String,
    val role: String?
)
