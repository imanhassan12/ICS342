package com.example.todolist.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.models.ToDoItem

@Composable
fun ToDoItemView(
    todo: ToDoItem,
    onCheckedChange: (Boolean) -> Unit
) {
    val isChecked = remember { mutableStateOf(todo.completed) }

    Row(modifier = Modifier.padding(8.dp)) {
        Checkbox(
            checked = !(when (todo.completed) { is Number -> (todo.completed.toDouble() != 0.0) else -> todo.completed } as Boolean),
            onCheckedChange = {
                isChecked.value = it
                onCheckedChange(it)
            }
        )
        Text(text = todo.description, modifier = Modifier.padding(start = 8.dp))
    }
}
