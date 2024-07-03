package com.example.todolist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel()) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val coroutineScope = rememberCoroutineScope()
    val todos = todoViewModel.todos.collectAsState()
    val (currentTodo, setCurrentTodo) = remember { mutableStateOf("") }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = currentTodo,
                    onValueChange = setCurrentTodo,
                    label = { Text("New Todo") },
                    trailingIcon = {
                        IconButton(onClick = { setCurrentTodo("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            setCurrentTodo("")
                            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                        }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (currentTodo.isNotBlank()) {
                                todoViewModel.addTodo(currentTodo)
                                setCurrentTodo("")
                                coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            } else {
                                // Show error (you can handle this by showing a Toast or a Snackbar)
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Todo List") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.hide()
                        }
                    }
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Todo")
                }
            },
            content = { paddingValues ->
                val horizontalPadding = 12.dp
                val verticalPadding = 8.dp

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + horizontalPadding,
                        top = paddingValues.calculateTopPadding() + verticalPadding,
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + horizontalPadding,
                        bottom = paddingValues.calculateBottomPadding() + verticalPadding
                    )
                ) {
                    items(todos.value) { todo ->
                        TodoItem(todo = todo, onCheckedChange = { isChecked ->
                            todoViewModel.updateTodo(todo.copy(isCompleted = isChecked))
                        })
                    }
                }
            }
        )
    }
}