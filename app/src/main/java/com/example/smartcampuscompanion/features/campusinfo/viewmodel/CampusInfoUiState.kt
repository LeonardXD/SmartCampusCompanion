package com.example.smartcampuscompanion.features.campusinfo.viewmodel

import com.example.smartcampuscompanion.domain.model.Department

sealed interface CampusInfoUiState {
    data object Loading : CampusInfoUiState
    data class Success(val departments: List<Department>) : CampusInfoUiState
    data class Error(val message: String) : CampusInfoUiState
}

