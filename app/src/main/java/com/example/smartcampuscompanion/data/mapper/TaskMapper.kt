package com.example.smartcampuscompanion.data.mapper

import com.example.smartcampuscompanion.data.local.entity.TaskEntity
import com.example.smartcampuscompanion.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate
    )
}
