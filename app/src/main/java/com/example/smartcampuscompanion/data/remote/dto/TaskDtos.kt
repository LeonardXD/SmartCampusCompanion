package com.example.smartcampuscompanion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    val id: Int,
    val title: String,
    val description: String?,
    val status: String?,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("due_date") val dueDate: String?
)

data class TaskCreateRequestDto(
    val title: String,
    val description: String?,
    val status: String,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("due_date") val dueDate: String?
)

data class TaskUpdateRequestDto(
    val title: String,
    val description: String?,
    val status: String,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("due_date") val dueDate: String?
)
