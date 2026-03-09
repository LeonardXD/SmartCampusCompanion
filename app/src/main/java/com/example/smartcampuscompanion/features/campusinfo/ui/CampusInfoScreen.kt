// smartcampuscompanion/features/campusinfo/CampusInfoScreen.kt
package com.example.smartcampuscompanion.features.campusinfo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.core.ui.components.AppTopBar
import com.example.smartcampuscompanion.features.campusinfo.viewmodel.CampusInfoEvent
import com.example.smartcampuscompanion.features.campusinfo.viewmodel.CampusInfoUiState
import com.example.smartcampuscompanion.features.campusinfo.viewmodel.CampusInfoViewModel

@Composable
fun CampusInfoScreen(
    viewModel: CampusInfoViewModel,
    onBack: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CampusInfoEvent.LoadDepartments)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Campus Info",
                canNavigateBack = onBack != null,
                navigateUp = { onBack?.invoke() }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(padding)
        ) {
            val departments = (state as? CampusInfoUiState.Success)?.departments.orEmpty()
            items(departments) { department ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = department.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = department.description, style = MaterialTheme.typography.bodyMedium)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(text = "📧 ${department.contactEmail}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "📞 ${department.contactPhone}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
