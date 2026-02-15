package com.example.smartcampuscompanion.domain.mapper

import com.example.smartcampuscompanion.data.local.entity.AnnouncementEntity
import com.example.smartcampuscompanion.domain.model.Announcement

fun AnnouncementEntity.toDomain(): Announcement {
    return Announcement(id, title, content, category, isImportant, createdAt)
}

fun Announcement.toEntity(): AnnouncementEntity {
    return AnnouncementEntity(id, title, content, category, isImportant, createdAt)
}


