package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.features.announcements.viewmodel.*

@Composable
fun AnnouncementScreen(viewModel: AnnouncementViewModel) {

    val state by viewModel.uiState.collectAsState()

    when (state) {
        AnnouncementUiState.Loading -> CircularProgressIndicator()

        AnnouncementUiState.Empty -> Text("No announcements")

        is AnnouncementUiState.Success -> {
            val list = (state as AnnouncementUiState.Success).announcements

            Column {
                Button(onClick = { viewModel.addAnnouncement("Sample", "Hello Campus") }) {
                    Text("Add")
                }

                LazyColumn {
                    items(list) { ann ->
                        Text(
                            text = ann.title,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        is AnnouncementUiState.Error -> Text("Error loading announcements")
    }
}

