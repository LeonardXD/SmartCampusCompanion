package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.remote.CampusApiService
import com.example.smartcampuscompanion.domain.model.Department

class DepartmentRepository(
    private val api: CampusApiService
) {
    suspend fun getDepartments(): List<Department> {
        return api.getDepartments().map { department ->
            Department(
                name = department.name,
                description = department.description.orEmpty(),
                contactEmail = department.contactEmail.orEmpty(),
                contactPhone = department.contactPhone.orEmpty()
            )
        }
    }
}
