package com.example.smartcampuscompanion.features.announcements.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import com.example.smartcampuscompanion.domain.model.Announcement
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

    val uiState: StateFlow<AnnouncementUiState> =
        repository.getAnnouncements()
            .map { list ->
                if (list.isEmpty()) AnnouncementUiState.Empty
                else AnnouncementUiState.Success(list)
            }
            .catch { emit(AnnouncementUiState.Error("Failed to load")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnnouncementUiState.Loading)

    fun addAnnouncement(title: String, content: String) {
        viewModelScope.launch {
            repository.addAnnouncement(
                Announcement(
                    id = 0,
                    title = title,
                    content = content,
                    category = "General",
                    isImportant = false,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            repository.deleteAnnouncement(announcement)
        }
    }
}

