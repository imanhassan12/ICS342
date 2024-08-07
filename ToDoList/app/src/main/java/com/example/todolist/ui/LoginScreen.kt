package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolist.R
import com.example.todolist.viewmodel.AuthViewModel
import com.example.todolist.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToCreateAccount: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val user by authViewModel.user.collectAsState()
    val error by authViewModel.error.collectAsState()
    val loginSuccess by authViewModel.loginSuccess.collectAsState()

    // Observe login success
    if (loginSuccess && user != null) {
        onLoginSuccess()
        userViewModel.setUser(user!!.id)
        userViewModel.setAuthToken(user!!.token)
        authViewModel.clearLoginSuccess() // Reset the state after handling success
    }

// Error Dialog
    if (error != null) {
        AlertDialog(
            onDismissRequest = { authViewModel.clearError() },
            confirmButton = {
                Button(onClick = { authViewModel.clearError() }) {
                    Text(text = stringResource(id = R.string.dialog_button_ok))
                }
            },
            title = { Text(text = stringResource(id = R.string.dialog_title_error)) },
            text = { Text(text = error ?: "") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(id = R.string.label_email_address)) }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.label_password)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                authViewModel.loginUser(email = email, password = password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.label_login))
        }

        TextButton(
            onClick = onNavigateToCreateAccount,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.label_create_an_account))
        }
    }
}
