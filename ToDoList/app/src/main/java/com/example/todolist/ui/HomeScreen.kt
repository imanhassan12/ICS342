package com.example.todolist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.R
import com.example.todolist.models.ToDoItem
import com.example.todolist.viewmodel.ToDoViewModel
import com.example.todolist.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    toDoViewModel: ToDoViewModel,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val todos by userViewModel.userTodos.collectAsState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val newToDoText = remember { mutableStateOf(TextFieldValue("")) }
    val error by userViewModel.error.collectAsState()
    val toDoError by toDoViewModel.error.collectAsState()
    var errorMessage by remember { mutableStateOf("") }


    // Error Dialog
    if (!error.isNullOrBlank() || !toDoError.isNullOrBlank()) {
        AlertDialog(
            onDismissRequest = { userViewModel.clearError(); toDoViewModel.clearError() },
            confirmButton = {
                Button(onClick = { userViewModel.clearError(); toDoViewModel.clearError()  }) {
                    Text(text = stringResource(id = R.string.dialog_button_ok))
                }
            },
            title = { Text(text = stringResource(id = R.string.dialog_title_error)) },
            text = { Text(text = error ?: toDoError ?: "") }
        )
    }

    // Fetch todos when the HomeScreen is displayed
    LaunchedEffect(Unit) {
        userViewModel.getUserTodos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet.value = true }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.label_add_todo))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(todos) { todo ->
                    ToDoItemView(todo = todo) {
                        userViewModel.updateUserTodo(
                            id = todo.id,
                            description = todo.description,
                            completed = !(when (todo.completed) { is Number -> (todo.completed.toDouble() != 0.0) else -> todo.completed } as Boolean)
                        )
                    }
                }
            }
        }

        if (showBottomSheet.value) {
            BottomSheet(
                onDismiss = { showBottomSheet.value = false },
                onSave = {
                    if (newToDoText.value.text.isNotEmpty()) {
                        userViewModel.createUserTodo(
                            description = newToDoText.value.text
                        )
                        showBottomSheet.value = false
                    } else {
                        errorMessage = "Please enter a todo description"
                    }
                },
                onCancel = { showBottomSheet.value = false },
                newToDoText = newToDoText,
                errorMessage = errorMessage
            )
        }

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ToDoItemView(todo: ToDoItem, onToggleComplete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = (when (todo.completed) { is Number -> (todo.completed.toDouble() != 0.0) else -> todo.completed } as Boolean),
            onCheckedChange = { onToggleComplete() }
        )
        Text(
            text = todo.description,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    newToDoText: MutableState<TextFieldValue>,
    errorMessage: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .clickable { }
        ) {
            OutlinedTextField(
                value = newToDoText.value,
                onValueChange = { newToDoText.value = it },
                label = { Text(text = stringResource(id = R.string.label_new_todo)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { newToDoText.value = TextFieldValue("") }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(id = R.string.clear))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
            TextButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}
