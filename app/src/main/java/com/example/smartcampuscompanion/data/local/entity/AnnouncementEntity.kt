package com.example.smartcampuscompanion.data.local.entity
@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val category: String,
    val isImportant: Boolean,
    val createdAt: Long
)

