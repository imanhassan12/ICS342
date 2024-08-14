package com.example.todolist.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.models.ToDoItemRequest
import com.example.todolist.network.ToDoApiService
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ToDoViewModel(private val apiService: ToDoApiService) : BaseViewModel() {

    private val _todos = MutableStateFlow<List<ToDoItem>>(emptyList())
    val todos: StateFlow<List<ToDoItem>> = _todos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null) // Error state
    val error: StateFlow<String?> = _error

    fun clearError() {
        _error.value = null
    }
}
