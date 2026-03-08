package com.example.smartcampuscompanion.features.taskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.domain.model.Task
import com.example.smartcampuscompanion.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        getTasks()
    }

    private fun getTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun addTask(title: String, description: String, dueDate: Long?) {
        viewModelScope.launch {
            repository.insertTask(
                Task(
                    title = title,
                    description = description,
                    dueDate = dueDate
                )
            )
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}
