package com.example.smartcampuscompanion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnnouncementDto(
    val id: Long,
    val title: String,
    val content: String,
    val category: String,
    @SerializedName("is_important") val isImportant: Boolean,
    @SerializedName("created_at") val createdAt: String?
)

data class AnnouncementCreateRequestDto(
    val title: String,
    val content: String,
    val category: String,
    @SerializedName("is_important") val isImportant: Boolean = false
)

data class DepartmentDto(
    val name: String,
    val description: String?,
    @SerializedName("contact_email") val contactEmail: String?,
    @SerializedName("contact_phone") val contactPhone: String?
)
