package com.example.smartcampuscompanion.domain.model
package com.example.smartcampuscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String,
    val isImportant: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

