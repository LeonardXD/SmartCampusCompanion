package com.example.smartcampuscompanion.features.announcements.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcampuscompanion.domain.model.Announcement
import com.example.smartcampuscompanion.features.announcements.utils.CATEGORY_ICONS

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onDelete: () -> Unit
) {
    val categoryInfo = CATEGORY_ICONS[announcement.category] ?: CATEGORY_ICONS["General"]!!

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Category and important marker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = announcement.category,
                    color = Color(categoryInfo.color),
                    fontSize = 12.sp
                )

                if (announcement.isImportant) {
                    Text(
                        text = "IMPORTANT",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.background(
                            Color.Red.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        ).padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Title
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = announcement.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Delete button aligned to end
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete announcement",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}
