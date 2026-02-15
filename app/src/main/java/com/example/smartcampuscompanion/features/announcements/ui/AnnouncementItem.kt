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
import androidx.compose.ui.graphics.Brush
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
            .padding(vertical = 6.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Top bar for category & important
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (announcement.isImportant) Brush.horizontalGradient(
                            colors = listOf(Color.Red, Color(0xFFFFA500))
                        ) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.category,
                    color = Color(categoryInfo.color),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )

                if (announcement.isImportant) {
                    Text(
                        text = "IMPORTANT",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(
                                Color.Red.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

          
            Text(
                text = announcement.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            
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
