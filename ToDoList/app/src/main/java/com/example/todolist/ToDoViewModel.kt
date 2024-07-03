package com.example.todolist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> get() = _todos

    fun addTodo(name: String) {
        _todos.value = _todos.value + Todo(
            id = _todos.value.size,
            name = name,
            isCompleted = false
        )
    }

    fun updateTodo(updatedTodo: Todo) {
        _todos.value = _todos.value.map { if (it.id == updatedTodo.id) updatedTodo else it }
    }
}
