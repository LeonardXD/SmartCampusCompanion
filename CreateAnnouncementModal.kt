package com.example.smartcampuscompanion.features.announcements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Check
import androidx.compose.material3.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcampuscompanion.domain.model.Announcement

val CATEGORIES = listOf("General", "Academic", "Events", "Emergency")

@Composable
fun CreateAnnouncementModal(
    visible: Boolean,
    onClose: () -> Unit,
    onCreate: (Announcement) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf("General") }
    var isImportant by remember { mutableStateOf(false) }

    val isValid = title.text.trim().isNotEmpty() && content.text.trim().isNotEmpty()

    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(top = 200.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(16.dp)
            ) {

                // Modal Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        title = TextFieldValue("")
                        content = TextFieldValue("")
                        category = "General"
                        isImportant = false
                        onClose()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                    Text(
                        text = "New Announcement",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            if (isValid) {
                                onCreate(
                                    Announcement(
                                        title = title.text.trim(),
                                        content = content.text.trim(),
                                        category = category,
                                        isImportant = isImportant
                                    )
                                )
                                title = TextFieldValue("")
                                content = TextFieldValue("")
                                category = "General"
                                isImportant = false
                                onClose()
                            }
                        },
                        enabled = isValid
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Submit")
                    }
                }

                // Title Input
                Text("Title", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Announcement title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                )

                // Content Input
                Text("Content", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 16.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Write announcement details...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 8.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    maxLines = 4
                )

               
                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 8.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    CATEGORIES.forEach { cat ->
                        Button(
                            onClick = { category = cat },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (category == cat) Color.Gray else Color.Transparent
                            )
                        ) {
                            Text(text = cat)
                        }
                    }
                }

                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Mark as Important", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Important announcements are highlighted",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Switch(
                        checked = isImportant,
                        onCheckedChange = { isImportant = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Blue)
                    )
                }
            }
        }
    }
}
