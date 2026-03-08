package com.example.smartcampuscompanion.features.taskmanager

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.smartcampuscompanion.di.AppModule
import com.example.smartcampuscompanion.di.ViewModelFactory
import com.example.smartcampuscompanion.features.auth.data.SessionManager

const val TASK_MANAGER_ROUTE = "task_manager"

fun NavGraphBuilder.taskManagerGraph(
    context: android.content.Context
) {
    composable(TASK_MANAGER_ROUTE) {
        val database = AppModule.provideDatabase(context)
        val dao = AppModule.provideTaskDao(database)
        val repository = AppModule.provideTaskRepository(dao)
        val username = SessionManager(context).getUsername() ?: ""
        val factory = ViewModelFactory { TaskViewModel(repository, username) }
        
        val viewModel: TaskViewModel = viewModel(factory = factory)
        TaskScreen(viewModel = viewModel)
    }
}
