package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.domain.model.Announcement

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(announcement.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(announcement.description)

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}

