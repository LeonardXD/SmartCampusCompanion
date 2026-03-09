package com.example.smartcampuscompanion.features.taskmanager

import com.example.smartcampuscompanion.domain.model.Task

sealed interface TaskEvent {
    data class AddTask(
        val title: String,
        val description: String,
        val dueDate: Long?
    ) : TaskEvent

    data class UpdateTask(val task: Task) : TaskEvent
    data class DeleteTask(val task: Task) : TaskEvent
    data class ToggleTaskCompletion(val task: Task) : TaskEvent
}
