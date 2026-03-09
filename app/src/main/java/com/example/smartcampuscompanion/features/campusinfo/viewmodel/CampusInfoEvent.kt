package com.example.smartcampuscompanion.features.campusinfo.viewmodel

sealed interface CampusInfoEvent {
    data object LoadDepartments : CampusInfoEvent
}
