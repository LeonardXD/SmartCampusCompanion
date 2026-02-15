package com.example.smartcampuscompanion.domain.model

data class Announcement(
    val id: Long,
    val title: String,
    val content: String,
    val category: String,
    val isImportant: Boolean,
    val createdAt: Long
)
