package com.example.smartcampuscompanion.features.campusinfo.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.repository.DepartmentRepository
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CampusInfoViewModel(
    private val repository: DepartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CampusInfoUiState>(CampusInfoUiState.Loading)
    val uiState: StateFlow<CampusInfoUiState> = _uiState.asStateFlow()

    fun onEvent(event: CampusInfoEvent) {
        when (event) {
            CampusInfoEvent.LoadDepartments -> {
                loadDepartments()
            }
        }
    }

    private fun loadDepartments() {
        viewModelScope.launch {
            _uiState.value = CampusInfoUiState.Loading
            try {
                _uiState.value = CampusInfoUiState.Success(repository.getDepartments())
            } catch (e: Exception) {
                _uiState.value = CampusInfoUiState.Error(
                    e.message ?: "Unable to load departments right now."
                )
            }
        }
    }
}

