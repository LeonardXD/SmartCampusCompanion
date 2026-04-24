// smartcampuscompanion/features/auth/LoginScreen.kt
package com.example.smartcampuscompanion.features.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcampuscompanion.core.ui.components.PrimaryButton
import com.example.smartcampuscompanion.core.ui.theme.AppElevation
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
import com.example.smartcampuscompanion.di.AppModule
import com.example.smartcampuscompanion.di.ViewModelFactory
import com.example.smartcampuscompanion.features.auth.data.SessionManager
import com.example.smartcampuscompanion.features.auth.viewmodel.AuthEvent
import com.example.smartcampuscompanion.features.auth.viewmodel.AuthUiState
import com.example.smartcampuscompanion.features.auth.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember(context) { SessionManager(context) }
    val api = remember(sessionManager) { AppModule.provideApiService(sessionManager) }
    val userRepository = remember(api, sessionManager) {
        AppModule.provideUserRepository(api, sessionManager)
    }
    val factory = remember(userRepository) { ViewModelFactory { AuthViewModel(userRepository) } }
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val authState by authViewModel.uiState.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var selectedRole by rememberSaveable { mutableStateOf(LoginRole.Student) }
    val isLoading = authState is AuthUiState.Loading
    val errorMessage = (authState as? AuthUiState.Error)?.message

    LaunchedEffect(authState) {
        val state = authState
        if (state is AuthUiState.Authenticated) {
            onLoginSuccess(state.username)
            authViewModel.onEvent(AuthEvent.ClearTransientState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.XXLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Smart Campus",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            Text(
                text = "Sign in to access tasks, announcements, and campus information.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppSpacing.XXLarge))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.Low),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSpacing.XLarge),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
                ) {
                    Text(
                        text = "Login As",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
                    ) {
                        RoleChip(
                            text = "Student Login",
                            icon = Icons.Default.School,
                            selected = selectedRole == LoginRole.Student,
                            onClick = {
                                selectedRole = LoginRole.Student
                                authViewModel.onEvent(AuthEvent.ClearTransientState)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        RoleChip(
                            text = "Admin Login",
                            icon = Icons.Default.AdminPanelSettings,
                            selected = selectedRole == LoginRole.Admin,
                            onClick = {
                                selectedRole = LoginRole.Admin
                                authViewModel.onEvent(AuthEvent.ClearTransientState)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            authViewModel.onEvent(AuthEvent.ClearTransientState)
                        },
                        label = { Text("Username") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            authViewModel.onEvent(AuthEvent.ClearTransientState)
                        },
                        label = { Text("Password") },
                        visualTransformation = if (isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = if (isPasswordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    }
                                )
                            }
                        }
                    )

                    if (!errorMessage.isNullOrBlank()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (selectedRole == LoginRole.Student) {
                        TextButton(
                            onClick = onRegisterClick,
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Text("Don't have an account? Register here")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        PrimaryButton(
                            text = if (selectedRole == LoginRole.Student) {
                                "Student Login"
                            } else {
                                "Admin Login"
                            },
                            onClick = {
                                authViewModel.onEvent(
                                    AuthEvent.Login(
                                        username = username,
                                        password = password,
                                        isAdminLogin = selectedRole == LoginRole.Admin
                                    )
                                )
                            },
                            enabled = username.isNotBlank() && password.isNotBlank() && !isLoading
                        )
                    }
                }
            }
        }
    }
}

private enum class LoginRole {
    Student,
    Admin
}

@Composable
private fun RoleChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        label = {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        colors = FilterChipDefaults.filterChipColors()
    )
}
