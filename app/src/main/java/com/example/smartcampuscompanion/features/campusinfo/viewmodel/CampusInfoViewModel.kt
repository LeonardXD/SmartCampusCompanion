package com.example.smartcampuscompanion.features.campusinfo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smartcampuscompanion.data.static.CampusData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CampusInfoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CampusInfoUiState>(CampusInfoUiState.Loading)
    val uiState: StateFlow<CampusInfoUiState> = _uiState.asStateFlow()

    fun onEvent(event: CampusInfoEvent) {
        when (event) {
            CampusInfoEvent.LoadDepartments -> {
                _uiState.value = CampusInfoUiState.Success(CampusData.departments)
            }
        }
    }
}

