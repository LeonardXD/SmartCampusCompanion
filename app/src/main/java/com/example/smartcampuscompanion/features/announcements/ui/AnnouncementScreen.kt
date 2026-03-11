package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.smartcampuscompanion.core.ui.theme.AppRadius
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementEvent
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementUiState
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementViewModel
import com.example.smartcampuscompanion.core.ui.components.AppTopBar

@Composable
fun AnnouncementScreen(
    viewModel: AnnouncementViewModel,
    canCreate: Boolean,
    canMarkAsRead: Boolean,
    canDelete: Boolean,
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Updates",
                actions = {
                    if (canCreate) {
                        IconButton(onClick = onProfileClick) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (canCreate) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Announcement")
                }
            }
        }
    ) { padding ->
        when (val uiState = state) {
            AnnouncementUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            AnnouncementUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No announcements yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is AnnouncementUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            is AnnouncementUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(AppSpacing.Large),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                ) {
                    items(uiState.announcements, key = { it.id }) { ann ->
                        AnnouncementItem(
                            announcement = ann,
                            onMarkAsRead = {
                                viewModel.onEvent(AnnouncementEvent.MarkAnnouncementRead(ann.id))
                            },
                            onDelete = {
                                viewModel.onEvent(AnnouncementEvent.DeleteAnnouncement(ann.id))
                            },
                            canMarkAsRead = canMarkAsRead,
                            canDelete = canDelete
                        )
                    }
                }
            }
        }

        if (showDialog && canCreate) {
            CreateAnnouncementDialog(
                onDismiss = { showDialog = false },
                onCreate = { title, content ->
                    viewModel.onEvent(AnnouncementEvent.CreateAnnouncement(title, content))
                    showDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAnnouncementDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, content: String) -> Unit
) {
    val categories = listOf("General", "Events", "Academic", "Facilities", "Emergency")
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    val maxChars = 500
    val canPost = title.isNotBlank() && content.text.isNotBlank() && selectedCategory != null

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.Small),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.XLarge),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
            ) {
                Text(
                    text = "New Announcement",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Announcement Title",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Enter a descriptive title...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                Text(
                    text = "Category",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Content",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${content.text.length}/$maxChars characters",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        if (it.text.length <= maxChars) content = it
                    },
                    placeholder = { Text("Type your message here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = MaterialTheme.shapes.medium
                )

                Button(
                    onClick = {
                        val category = selectedCategory ?: return@Button
                        onCreate(
                            "[$category] ${title.trim()}",
                            content.text.trim()
                        )
                    },
                    enabled = canPost,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(AppRadius.Pill),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = " Post Announcement",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
