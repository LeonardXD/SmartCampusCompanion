package com.example.smartcampuscompanion.features.announcements.viewmodel

import com.example.smartcampuscompanion.domain.model.Announcement

sealed interface AnnouncementUiState {
    object Loading : AnnouncementUiState
    object Empty : AnnouncementUiState
    data class Success(val announcements: List<Announcement>) : AnnouncementUiState
    data class Error(val message: String) : AnnouncementUiState
}

