package com.example.smartcampuscompanion.features.taskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                    onEdit = { editingTask = task },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }

        if (showAddDialog) {
            TaskFormDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, description, dueDate ->
                    viewModel.addTask(title, description, dueDate)
                    showAddDialog = false
                }
            )
        }

        if (editingTask != null) {
            TaskFormDialog(
                initialTask = editingTask,
                onDismiss = { editingTask = null },
                onConfirm = { title, description, dueDate ->
                    editingTask?.let {
                        viewModel.updateTask(
                            it.copy(
                                title = title,
                                description = description,
                                dueDate = dueDate
                            )
                        )
                    }
                    editingTask = null
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleCompletion: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleCompletion() }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                task.dueDate?.let {
                    Text(
                        text = "Due: ${formatDueDate(it)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Task")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}

@Composable
fun TaskFormDialog(
    initialTask: Task? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long?) -> Unit
) {
    val context = LocalContext.current
    var title by remember(initialTask?.id) { mutableStateOf(initialTask?.title ?: "") }
    var description by remember(initialTask?.id) { mutableStateOf(initialTask?.description ?: "") }
    var dueDateMillis by remember(initialTask?.id) { mutableStateOf(initialTask?.dueDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTask == null) "Add New Task" else "Edit Task") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        val now = Calendar.getInstance()
                        val datePicker = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val selected = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth)
                                }
                                val timePicker = TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        selected.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                        selected.set(Calendar.MINUTE, minute)
                                        selected.set(Calendar.SECOND, 0)
                                        selected.set(Calendar.MILLISECOND, 0)
                                        dueDateMillis = selected.timeInMillis
                                    },
                                    now.get(Calendar.HOUR_OF_DAY),
                                    now.get(Calendar.MINUTE),
                                    false
                                )
                                timePicker.show()
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (dueDateMillis == null) {
                            "Pick Date & Time"
                        } else {
                            "Due: ${formatDueDate(dueDateMillis!!)}"
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description, dueDateMillis)
                    }
                }
            ) {
                Text(if (initialTask == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatDueDate(timestamp: Long): String {
    return SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(Date(timestamp))
}
