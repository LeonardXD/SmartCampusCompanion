package com.example.smartcampuscompanion.data.static

import com.example.smartcampuscompanion.domain.model.Department

object CampusData {
    val departments = listOf(
        Department(
            name = "College of Computing Studies",
            description = "Handles IT programs.",
            contactEmail = "ccs@campus.edu",
            contactPhone = "+63 912 345 6789"
        ),
        Department(
            name = "College of Engineering",
            description = "Engineering courses and research.",
            contactEmail = "engineering@campus.edu",
            contactPhone = "+63 923 456 7890"
        ),
        Department(
            name = "Student Affairs Office",
            description = "Student services and support.",
            contactEmail = "sao@campus.edu",
            contactPhone = "+63 934 567 8901"
        )
    )
}
