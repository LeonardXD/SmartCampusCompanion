package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Add
import androidx.compose.material3.icons.filled.Delete
import androidx.compose.material3.icons.filled.Flag
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcampuscompanion.domain.model.Announcement
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementUiState
import com.example.smartcampuscompanion.features.announcements.viewmodel.AnnouncementViewModel
import com.example.smartcampuscompanion.features.announcements.utils.formatDate

@Composable
fun AnnouncementScreen(viewModel: AnnouncementViewModel) {

    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Announcements") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "New Announcement")
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
                        Text("No announcements yet", color = Color.Gray)
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
                            AnnouncementCard(
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

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(6.dp, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (announcement.isImportant) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Red, Color(0xFFFFA500))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(announcement.title, style = MaterialTheme.typography.titleMedium, fontSize = 17.sp)
                if (announcement.isImportant) {
                    Icon(Icons.Filled.Flag, contentDescription = "Important", tint = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(announcement.description, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(formatDate(announcement.createdAt), fontSize = 12.sp, color = Color.Gray)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Gray)
                }
            }
        }
    }
}
