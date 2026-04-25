package com.example.smartcampuscompanion.features.taskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.domain.model.Task
import com.example.smartcampuscompanion.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface TaskUiState {
    data object Idle : TaskUiState
    data object Loading : TaskUiState
    data class Success(val tasks: List<Task>) : TaskUiState
    data class Error(val message: String) : TaskUiState
}

class TaskViewModel(
    private val repository: TaskRepository,
    private val currentUsername: String
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        getTasks()
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.AddTask -> addTask(
                title = event.title,
                description = event.description,
                dueDate = event.dueDate
            )
            is TaskEvent.UpdateTask -> updateTask(event.task)
            is TaskEvent.DeleteTask -> deleteTask(event.task)
            is TaskEvent.ToggleTaskCompletion -> toggleTaskCompletion(event.task)
        }
    }

    private fun getTasks() {
        viewModelScope.launch {
            _uiState.value = TaskUiState.Loading
            repository.getAllTasks(currentUsername)
                .catch { e ->
                    _uiState.value = TaskUiState.Error(
                        e.message ?: "Unable to load tasks. Please try again."
                    )
                }
                .collect { taskList ->
                    _tasks.value = taskList
                    _uiState.value = TaskUiState.Success(taskList)
                }
        }
    }

    private fun addTask(title: String, description: String, dueDate: Long?) {
        viewModelScope.launch {
            try {
                repository.insertTask(
                    Task(
                        ownerUsername = currentUsername,
                        title = title,
                        description = description,
                        dueDate = dueDate
                    )
                )
                getTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error(
                    e.message ?: "Unable to add task. Please try again."
                )
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                getTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error(
                    e.message ?: "Unable to update task. Please try again."
                )
            }
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
                getTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error(
                    e.message ?: "Unable to delete task. Please try again."
                )
            }
        }
    }

    private fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task.copy(isCompleted = !task.isCompleted))
                getTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error(
                    e.message ?: "Unable to update task status. Please try again."
                )
            }
        }
    }
}
