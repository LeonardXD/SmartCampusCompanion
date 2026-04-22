package com.example.smartcampuscompanion.data.mapper

import com.example.smartcampuscompanion.data.local.entity.TaskEntity
import com.example.smartcampuscompanion.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        ownerUsername = ownerUsername,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        ownerUsername = ownerUsername,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate
    )
}
