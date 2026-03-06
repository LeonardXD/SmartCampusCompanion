package com.example.smartcampuscompanion.features.announcements.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

   
    private val _uiState = MutableStateFlow<AnnouncementUiState>(AnnouncementUiState.Loading)
    val uiState: StateFlow<AnnouncementUiState> = _uiState

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        viewModelScope.launch {
            _uiState.value = AnnouncementUiState.Loading
            try {
                val announcements = repository.getAllAnnouncements()
                _uiState.value = if (announcements.isEmpty()) {
                    AnnouncementUiState.Empty
                } else {
                    AnnouncementUiState.Success(announcements)
                }
            } catch (e: Exception) {
                _uiState.value = AnnouncementUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /
    fun createAnnouncement(title: String, description: String) {
        viewModelScope.launch {
            try {
                repository.addAnnouncement(title, description)
                loadAnnouncements() 
            } catch (e: Exception) {
                _uiState.value = AnnouncementUiState.Error("Failed to create announcement")
            }
        }
    }

    
}
