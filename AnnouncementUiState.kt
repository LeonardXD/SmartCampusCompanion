package com.example.smartcampuscompanion.features.announcements.viewmodel

import com.example.smartcampuscompanion.domain.model.Announcement

/**
 * Represents the UI state of AnnouncementScreen
 */
sealed interface AnnouncementUiState {
    /** Screen is loading announcements */
    object Loading : AnnouncementUiState

    /** No announcements available */
    object Empty : AnnouncementUiState

    /** Successfully loaded list of announcements */
    data class Success(val announcements: List<Announcement>) : AnnouncementUiState

    /** Error occurred while loading announcements */
    data class Error(val message: String) : AnnouncementUiState
}
