package com.example.smartcampuscompanion.features.taskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.core.ui.components.AppTopBar
import com.example.smartcampuscompanion.core.ui.theme.AppRadius
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
import com.example.smartcampuscompanion.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun TaskScreen(
    viewModel: TaskViewModel
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var deletingTask by remember { mutableStateOf<Task?>(null) }
    var selectedTab by rememberSaveable { mutableStateOf(TaskTab.TODO) }

    val now = System.currentTimeMillis()
    val inProgressBoundary = now + TimeUnit.HOURS.toMillis(24)
    val completedTasks = tasks.filter { it.isCompleted }
        .sortedByDescending { it.dueDate ?: Long.MIN_VALUE }
    val inProgressTasks = tasks.filter { !it.isCompleted && it.dueDate != null && it.dueDate <= inProgressBoundary }
        .sortedBy { it.dueDate ?: Long.MAX_VALUE }
    val todoTasks = tasks.filter { !it.isCompleted && (it.dueDate == null || it.dueDate > inProgressBoundary) }
        .sortedBy { it.dueDate ?: Long.MAX_VALUE }

    val visibleTasks = when (selectedTab) {
        TaskTab.TODO -> todoTasks
        TaskTab.IN_PROGRESS -> inProgressTasks
        TaskTab.COMPLETED -> completedTasks
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Tasks")
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = AppSpacing.Medium, vertical = AppSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
            item {
                TaskTabSelector(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            if (visibleTasks.isEmpty()) {
                item {
                    Text(
                        text = "No tasks in ${selectedTab.label.lowercase(Locale.getDefault())}.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = AppSpacing.Small)
                    )
                }
            }

            items(visibleTasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onToggleCompletion = { viewModel.onEvent(TaskEvent.ToggleTaskCompletion(task)) },
                    onEdit = { editingTask = task },
                    onDelete = { deletingTask = task }
                )
            }

            if (selectedTab != TaskTab.COMPLETED && completedTasks.isNotEmpty()) {
                item {
                    Text(
                        text = "RECENTLY COMPLETED",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
                    )
                }

                items(completedTasks.take(3), key = { "recent_${it.id}" }) { task ->
                    CompletedTaskRow(task = task)
                }
            }
        }

        if (showAddDialog) {
            TaskFormDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, description, dueDate ->
                    viewModel.onEvent(TaskEvent.AddTask(title, description, dueDate))
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
                        viewModel.onEvent(
                            TaskEvent.UpdateTask(
                                it.copy(
                                    title = title,
                                    description = description,
                                    dueDate = dueDate
                                )
                            )
                        )
                    }
                    editingTask = null
                }
            )
        }

        if (deletingTask != null) {
            AlertDialog(
                onDismissRequest = { deletingTask = null },
                title = { Text("Delete task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deletingTask?.let { viewModel.onEvent(TaskEvent.DeleteTask(it)) }
                            deletingTask = null
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deletingTask = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun TaskTabSelector(
    selectedTab: TaskTab,
    onTabSelected: (TaskTab) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Large)
        ) {
            TaskTab.entries.forEach { tab ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(AppRadius.Small))
                        .combinedClickable(onClick = { onTabSelected(tab) })
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = tab.label,
                        color = if (tab == selectedTab) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (tab == selectedTab) FontWeight.SemiBold else FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.Small))
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(2.dp)
                            .background(
                                if (tab == selectedTab) MaterialTheme.colorScheme.primary else Color.Transparent,
                                RoundedCornerShape(50)
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.XSmall))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        )
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onToggleCompletion: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val priority = task.priorityForDisplay()
    val priorityPalette = priority.palette()
    val dueLabel = task.dueDate.toFriendlyDueText()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .combinedClickable(
                onClick = onEdit,
                onLongClick = onDelete
            ),
        shape = RoundedCornerShape(AppRadius.Large),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(priorityPalette.accentColor)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriorityChip(priority = priority, palette = priorityPalette)
                        Text(
                            text = task.description.ifBlank { "General" },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.Small))
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(AppSpacing.Small))
                        Text(
                            text = dueLabel,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                IconButton(onClick = onToggleCompletion) {
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = if (task.isCompleted) "Mark incomplete" else "Mark complete",
                        tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PriorityChip(
    priority: TaskPriority,
    palette: TaskPriorityPalette
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadius.Pill))
            .background(palette.chipColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = priority.label,
            color = palette.textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CompletedTaskRow(task: Task) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(AppRadius.Large)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(AppRadius.Pill))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = task.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = TextDecoration.LineThrough,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Completed",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
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
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
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

private enum class TaskTab(val label: String) {
    TODO("To-Do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}

private enum class TaskPriority(val label: String) {
    HIGH("HIGH PRIORITY"),
    MEDIUM("MEDIUM PRIORITY"),
    LOW("LOW PRIORITY")
}

private data class TaskPriorityPalette(
    val accentColor: Color,
    val chipColor: Color,
    val textColor: Color
)

@Composable
private fun TaskPriority.palette(): TaskPriorityPalette {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        TaskPriority.HIGH -> TaskPriorityPalette(
            accentColor = scheme.tertiary,
            chipColor = scheme.tertiaryContainer,
            textColor = scheme.onTertiaryContainer
        )

        TaskPriority.MEDIUM -> TaskPriorityPalette(
            accentColor = scheme.primary,
            chipColor = scheme.primaryContainer,
            textColor = scheme.onPrimaryContainer
        )

        TaskPriority.LOW -> TaskPriorityPalette(
            accentColor = scheme.secondary,
            chipColor = scheme.secondaryContainer,
            textColor = scheme.onSecondaryContainer
        )
    }
}

private fun Task.priorityForDisplay(): TaskPriority {
    val due = dueDate ?: return TaskPriority.LOW
    val remaining = due - System.currentTimeMillis()
    val oneDay = TimeUnit.DAYS.toMillis(1)
    return when {
        remaining <= oneDay -> TaskPriority.HIGH
        remaining <= TimeUnit.DAYS.toMillis(3) -> TaskPriority.MEDIUM
        else -> TaskPriority.LOW
    }
}

private fun Long?.toFriendlyDueText(): String {
    if (this == null) return "No due date"

    val dueDate = Date(this)
    val now = Calendar.getInstance()
    val due = Calendar.getInstance().apply { time = dueDate }

    val dayFormatter = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    return when {
        now.get(Calendar.YEAR) == due.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == due.get(Calendar.DAY_OF_YEAR) -> {
            "Today, ${timeFormatter.format(dueDate)}"
        }

        else -> dayFormatter.format(dueDate)
    }
}
