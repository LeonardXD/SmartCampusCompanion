package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementViewModel
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementUiState

@Composable
fun AnnouncementScreen(
    viewModel: AnnouncementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ErrorBoundary {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Text(
                text = "Announcements",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            when (uiState) {
                is AnnouncementUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is AnnouncementUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No announcements yet")
                    }
                }

                is AnnouncementUiState.Success -> {
                    val list = (uiState as AnnouncementUiState.Success).announcements
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(list) { announcement ->
                            AnnouncementItem(
                                title = announcement.title,
                                description = announcement.description,
                                date = announcement.date,
                                author = announcement.author,
                                onDeleteClick = { viewModel.deleteAnnouncement(announcement.id) }
                            )
                        }
                    }
                }

                is AnnouncementUiState.Error -> {
                    val message = (uiState as AnnouncementUiState.Error).message
                    ErrorFallback(
                        message = message,
                        onRetry = { viewModel.retry() }
                    )
                }
            }

            // Modal dialog for creating announcement
            CreateAnnouncementDialog(
                onSubmit = { title, description ->
                    viewModel.createAnnouncement(title, description)
                }
            )
        }
    }
}
