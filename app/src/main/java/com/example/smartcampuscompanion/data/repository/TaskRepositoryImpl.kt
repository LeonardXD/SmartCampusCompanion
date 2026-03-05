package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.local.dao.TaskDao
import com.example.smartcampuscompanion.data.mapper.toDomain
import com.example.smartcampuscompanion.data.mapper.toEntity
import com.example.smartcampuscompanion.domain.model.Task
import com.example.smartcampuscompanion.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return dao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.toEntity())
    }
}
