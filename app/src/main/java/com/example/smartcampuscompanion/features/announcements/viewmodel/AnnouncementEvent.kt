package com.example.smartcampuscompanion.features.announcements.viewmodel

sealed interface AnnouncementEvent {
    data class CreateAnnouncement(
        val title: String,
        val content: String
    ) : AnnouncementEvent

    data class DeleteAnnouncement(val id: Long) : AnnouncementEvent
    data class MarkAnnouncementRead(val id: Long) : AnnouncementEvent
}
