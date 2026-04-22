package com.example.smartcampuscompanion.features.announcements.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.core.ui.theme.AppRadius
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
import com.example.smartcampuscompanion.domain.model.Announcement
import kotlin.math.max

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit,
    canMarkAsRead: Boolean,
    canDelete: Boolean
) {
    StyledAnnouncementItem(
        announcement = announcement,
        showNewBadge = canMarkAsRead && !announcement.isRead,
        onMarkAsRead = onMarkAsRead,
        canDelete = canDelete,
        onDelete = onDelete
    )
}

@Composable
private fun StyledAnnouncementItem(
    announcement: Announcement,
    showNewBadge: Boolean,
    onMarkAsRead: () -> Unit,
    canDelete: Boolean,
    onDelete: () -> Unit
) {
    var showFullContent by remember { mutableStateOf(false) }
    val (category, cleanTitle) = extractCategoryAndTitle(announcement.title)
    val categoryVisual = categoryVisual(category)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.XSmall),
        shape = RoundedCornerShape(AppRadius.XLarge),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.Large),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(categoryVisual.badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = categoryVisual.icon,
                        contentDescription = null,
                        tint = categoryVisual.badgeText,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                    Text(
                        text = category.uppercase(),
                        color = categoryVisual.badgeText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = formatRelativeTime(announcement.createdAt),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = cleanTitle,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = announcement.content,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = if (announcement.isRead) 4 else 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showNewBadge) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NEW",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Spacer(modifier = Modifier)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = {
                            if (showNewBadge) {
                                onMarkAsRead()
                            }
                            showFullContent = true
                        }
                    ) {
                        Text(
                            text = "View More",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (canDelete) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete announcement",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFullContent) {
        AlertDialog(
            onDismissRequest = { showFullContent = false },
            title = { Text(cleanTitle) },
            text = { Text(announcement.content) },
            confirmButton = {
                TextButton(onClick = { showFullContent = false }) {
                    Text("Close")
                }
            }
        )
    }
}

private data class CategoryVisual(
    val icon: ImageVector,
    val badgeBg: Color,
    val badgeText: Color
)

@Composable
private fun categoryVisual(category: String): CategoryVisual {
    val scheme = MaterialTheme.colorScheme
    return when (category.trim().lowercase()) {
        "emergency" -> CategoryVisual(
            icon = Icons.Default.Warning,
            badgeBg = scheme.errorContainer,
            badgeText = scheme.onErrorContainer
        )

        "events" -> CategoryVisual(
            icon = Icons.Default.Event,
            badgeBg = scheme.secondaryContainer,
            badgeText = scheme.onSecondaryContainer
        )

        "academic" -> CategoryVisual(
            icon = Icons.Default.School,
            badgeBg = scheme.primaryContainer,
            badgeText = scheme.onPrimaryContainer
        )

        "general" -> CategoryVisual(
            icon = Icons.Default.Campaign,
            badgeBg = scheme.surfaceVariant,
            badgeText = scheme.onSurfaceVariant
        )

        else -> CategoryVisual(
            icon = Icons.Default.Info,
            badgeBg = scheme.surfaceVariant,
            badgeText = scheme.onSurfaceVariant
        )
    }
}

private fun extractCategoryAndTitle(title: String): Pair<String, String> {
    val trimmed = title.trim()
    if (trimmed.startsWith("[") && trimmed.contains("]")) {
        val closeIdx = trimmed.indexOf(']')
        if (closeIdx > 1) {
            val category = trimmed.substring(1, closeIdx).trim()
            val contentTitle = trimmed.substring(closeIdx + 1).trim().ifBlank { trimmed }
            return category to contentTitle
        }
    }
    return "General" to trimmed
}

private fun formatRelativeTime(createdAt: Long): String {
    val now = System.currentTimeMillis()
    val diff = max(0L, now - createdAt)
    val minute = 60_000L
    val hour = 60 * minute
    val day = 24 * hour
    return when {
        diff < hour -> "${max(1L, diff / minute)}m ago"
        diff < day -> "${diff / hour}h ago"
        else -> "${diff / day}d ago"
    }
}
