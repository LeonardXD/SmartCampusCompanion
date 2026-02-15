package com.example.smartcampuscompanion.features.announcements.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcampuscompanion.domain.model.Announcement
import com.example.smartcampuscompanion.features.announcements.utils.CATEGORY_ICONS
import com.example.smartcampuscompanion.features.announcements.utils.formatDate

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    onDelete: (String) -> Unit,
    onClick: (() -> Unit)? = null
) {
    val categoryInfo = remember(announcement.category) {
        CATEGORY_ICONS[announcement.category] ?: CATEGORY_ICONS["General"]!!
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (onClick != null) Modifier.clickable(
                    indication = rememberRipple(),
                    onClick = { onClick() }
                ) else Modifier
            )
    ) {
        Column {
            // Important bar
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

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = categoryInfo.iconRes),
                        contentDescription = "Category: ${announcement.category}",
                        tint = Color(categoryInfo.color),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = announcement.category,
                        color = Color(categoryInfo.color),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row {
                    if (announcement.isImportant) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Important",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = { onDelete(announcement.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Title
            Text(
                text = announcement.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Content
            Text(
                text = announcement.content,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, bottom = 12.dp)
            )

            
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Time",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Text(
                    text = formatDate(announcement.createdAt),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}


