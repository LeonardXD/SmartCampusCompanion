package com.example.smartcampuscompanion.domain.model

data class Task(
    val id: Int = 0,
    val ownerUsername: String = "",
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val dueDate: Long? = null
)

