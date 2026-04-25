package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.remote.CampusApiService
import com.example.smartcampuscompanion.data.remote.dto.TaskCreateRequestDto
import com.example.smartcampuscompanion.data.remote.dto.TaskDto
import com.example.smartcampuscompanion.data.remote.dto.TaskUpdateRequestDto
import com.example.smartcampuscompanion.domain.model.Task
import com.example.smartcampuscompanion.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TaskRepositoryImpl(
    private val api: CampusApiService
) : TaskRepository {

    override fun getAllTasks(ownerUsername: String): Flow<List<Task>> {
        return flow {
            val tasks = api.getTasks().map { it.toDomain(ownerUsername) }
            emit(tasks)
        }
    }

    override suspend fun getTaskById(id: Int, ownerUsername: String): Task? {
        return api.getTasks()
            .firstOrNull { it.id == id }
            ?.toDomain(ownerUsername)
    }

    override suspend fun insertTask(task: Task) {
        api.createTask(
            TaskCreateRequestDto(
                title = task.title,
                description = task.description.ifBlank { null },
                status = if (task.isCompleted) "completed" else "pending",
                isCompleted = task.isCompleted,
                dueDate = task.dueDate.toApiDate()
            )
        )
    }

    override suspend fun updateTask(task: Task) {
        api.updateTask(
            id = task.id,
            request = TaskUpdateRequestDto(
                title = task.title,
                description = task.description.ifBlank { null },
                status = if (task.isCompleted) "completed" else "pending",
                isCompleted = task.isCompleted,
                dueDate = task.dueDate.toApiDate()
            )
        )
    }

    override suspend fun deleteTask(task: Task) {
        api.deleteTask(task.id)
    }

    private fun TaskDto.toDomain(ownerUsername: String): Task {
        return Task(
            id = id,
            ownerUsername = ownerUsername,
            title = title,
            description = description.orEmpty(),
            isCompleted = isCompleted,
            dueDate = dueDate.toEpochMillis()
        )
    }

    private fun String?.toEpochMillis(): Long? {
        if (this.isNullOrBlank()) return null

        return try {
            // Handle Laravel's default "YYYY-MM-DD HH:MM:SS" by converting to ISO 8601
            val isoString = if (this.contains(" ") && !this.contains("T")) {
                this.replace(" ", "T") + "Z"
            } else {
                this
            }
            Instant.parse(isoString).toEpochMilli()
        } catch (_: Exception) {
            null
        }
    }

    private fun Long?.toApiDate(): String? {
        if (this == null) return null
        return DateTimeFormatter.ISO_INSTANT
            .format(Instant.ofEpochMilli(this))
    }
}
