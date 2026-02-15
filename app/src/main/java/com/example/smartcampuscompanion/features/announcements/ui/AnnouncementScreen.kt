package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.features.announcements.viewmodel.*

@Composable
fun AnnouncementScreen(viewModel: AnnouncementViewModel) {

    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (state) {

                AnnouncementUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                AnnouncementUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No announcements yet")
                    }
                }

                is AnnouncementUiState.Success -> {
                    val list = (state as AnnouncementUiState.Success).announcements

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(list) { ann ->
                            AnnouncementItem(
                                announcement = ann,
                                onDelete = { viewModel.deleteAnnouncement(ann.id) }
                            )
                        }
                    }
                }

                is AnnouncementUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error loading announcements. Try again.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Dialog for creating announcement
            if (showDialog) {
                CreateAnnouncementDialog(
                    onDismiss = { showDialog = false },
                    onCreate = { title, desc ->
                        viewModel.createAnnouncement(title, desc)
                        showDialog = false
                    }
                )
            }
        }
    }
}
