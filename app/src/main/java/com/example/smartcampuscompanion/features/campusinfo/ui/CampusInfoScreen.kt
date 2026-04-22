package com.example.smartcampuscompanion.features.campusinfo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.smartcampuscompanion.core.ui.components.AppTopBar
import com.example.smartcampuscompanion.core.ui.theme.AppElevation
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Campus Info",
                canNavigateBack = onBack != null,
                navigateUp = { onBack?.invoke() }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(AppSpacing.Large),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val departments = (state as? CampusInfoUiState.Success)?.departments.orEmpty()
            items(departments) { department ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.Low),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(AppSpacing.Large)) {
                        Text(
                            text = department.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(AppSpacing.XSmall))
                        Text(
                            text = department.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Divider(modifier = Modifier.padding(vertical = AppSpacing.Small))
                        Text(
                            text = "Email: ${department.contactEmail}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Phone: ${department.contactPhone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
