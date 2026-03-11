package com.example.smartcampuscompanion.features.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcampuscompanion.core.ui.components.AppTopBar
import com.example.smartcampuscompanion.core.ui.components.PrimaryButton
import com.example.smartcampuscompanion.core.ui.theme.AppElevation
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
import com.example.smartcampuscompanion.di.AppModule
import com.example.smartcampuscompanion.di.ViewModelFactory
import com.example.smartcampuscompanion.features.auth.viewmodel.AuthEvent
import com.example.smartcampuscompanion.features.auth.viewmodel.AuthUiState
import com.example.smartcampuscompanion.features.auth.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember(context) { AppModule.provideDatabase(context) }
    val userDao = remember(database) { AppModule.provideUserDao(database) }
    val userRepository = remember(userDao) { AppModule.provideUserRepository(userDao) }
    val factory = remember(userRepository) { ViewModelFactory { AuthViewModel(userRepository) } }
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val authState by authViewModel.uiState.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val isSubmitting = authState is AuthUiState.Loading
    val errorMessage = (authState as? AuthUiState.Error)?.message

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Registered) {
            onRegisterSuccess()
            authViewModel.onEvent(AuthEvent.ClearTransientState)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Student Registration",
                canNavigateBack = true,
                navigateUp = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppSpacing.XXLarge),
            verticalArrangement = Arrangement.Center
        ) {
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
                        .padding(AppSpacing.XLarge)
                ) {
                    Text(
                        text = "Create your student account",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Large))

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

                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            authViewModel.onEvent(AuthEvent.ClearTransientState)
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            authViewModel.onEvent(AuthEvent.ClearTransientState)
                        },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (isConfirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (isConfirmPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!errorMessage.isNullOrBlank()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = AppSpacing.Medium)
                        )
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.Large))

                    PrimaryButton(
                        text = if (isSubmitting) "Creating..." else "Create Account",
                        onClick = {
                            authViewModel.onEvent(
                                AuthEvent.Register(
                                    username = username,
                                    password = password,
                                    confirmPassword = confirmPassword
                                )
                            )
                        },
                        enabled = username.isNotBlank() &&
                            password.isNotBlank() &&
                            confirmPassword.isNotBlank() &&
                            !isSubmitting
                    )

                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to login")
                    }
                }
            }
        }
    }
}
