package com.example.smartcampuscompanion.features.campusinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.data.static.CampusData

@Composable
fun CampusInfoScreen() {
    LazyColumn {
        items(CampusData.departments) { department ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = department.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = department.description)
                    Text(text = "Email: ${department.contactEmail}")
                    Text(text = "Phone: ${department.contactPhone}")
                }
            }
        }
    }
}