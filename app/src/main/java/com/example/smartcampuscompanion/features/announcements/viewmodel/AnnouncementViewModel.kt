package com.example.smartcampuscompanion.features.announcements.viewmodel

import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import com.example.smartcampuscompanion.domain.model.Announcement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnnouncementUiState>(AnnouncementUiState.Loading)
    val uiState: StateFlow<AnnouncementUiState> = _uiState.asStateFlow()

    init {
        observeAnnouncements()
    }

    fun onEvent(event: AnnouncementEvent) {
        when (event) {
            is AnnouncementEvent.CreateAnnouncement -> createAnnouncement(
                title = event.title,
                content = event.content
            )
            is AnnouncementEvent.DeleteAnnouncement -> deleteAnnouncement(event.id)
            is AnnouncementEvent.MarkAnnouncementRead -> markAnnouncementRead(event.id)
        }
    }

    private fun observeAnnouncements() {
        viewModelScope.launch {
            repository.getAnnouncements()
                .catch { e ->
                    _uiState.value = AnnouncementUiState.Error(
                        e.message ?: "Failed to load announcements"
                    )
                }
                .collect { announcements ->
                    _uiState.value = if (announcements.isEmpty()) {
                        AnnouncementUiState.Empty
                    } else {
                        AnnouncementUiState.Success(announcements)
                    }
                }
        }
    }

    private fun createAnnouncement(title: String, content: String) {
        viewModelScope.launch {
            try {
                val announcement = Announcement(
                    id = 0L,
                    title = title,
                    content = content,
                    category = "General",
                    isImportant = false,
                    createdAt = System.currentTimeMillis(),
                    isRead = false
                )
                repository.addAnnouncement(announcement)
            } catch (e: SQLiteException) {
                _uiState.value = AnnouncementUiState.Error(
                    e.message ?: "Failed to create announcement"
                )
            } catch (e: Exception) {
                _uiState.value = AnnouncementUiState.Error(
                    e.message ?: "Failed to create announcement"
                )
            }
        }
    }

    private fun deleteAnnouncement(id: Long) {
        viewModelScope.launch {
            val current = (_uiState.value as? AnnouncementUiState.Success)?.announcements ?: return@launch
            val target = current.firstOrNull { it.id == id } ?: return@launch
            try {
                repository.deleteAnnouncement(target)
            } catch (e: SQLiteException) {
                _uiState.value = AnnouncementUiState.Error(
                    e.message ?: "Failed to delete announcement"
                )
            } catch (e: Exception) {
                _uiState.value = AnnouncementUiState.Error(
                    e.message ?: "Failed to delete announcement"
                )
            }
        }
    }

    private fun markAnnouncementRead(id: Long) {
        viewModelScope.launch {
            try {
                repository.markAnnouncementAsRead(id)
            } catch (e: SQLiteException) {
                _uiState.value = AnnouncementUiState.Error(
                    e.message ?: "Failed to mark announcement as read"
                )
            } catch (e: Exception) {
                _uiState.value = AnnouncementUiState.Error(
                    e.message ?: "Failed to mark announcement as read"
                )
            }
        }
    }
}
