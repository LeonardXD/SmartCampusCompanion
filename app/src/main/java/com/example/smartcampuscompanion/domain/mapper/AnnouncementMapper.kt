package com.example.smartcampuscompanion.domain.mapper

fun AnnouncementEntity.toDomain(): Announcement {
    return Announcement(id, title, content, category, isImportant, createdAt)
}

fun Announcement.toEntity(): AnnouncementEntity {
    return AnnouncementEntity(id, title, content, category, isImportant, createdAt)
}

