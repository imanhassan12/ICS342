package com.example.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.models.ToDoItemRequest
import com.example.todolist.network.ToDoApiService
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val apiService: ToDoApiService) : BaseViewModel() {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId

    private val _userTodos = MutableStateFlow<List<ToDoItem>>(emptyList())
    val userTodos: StateFlow<List<ToDoItem>> = _userTodos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null) // Error state
    val error: StateFlow<String?> = _error

    fun setUser(userId: Long) {
        _userId.value = userId
    }

    private var authToken: String? = null // Store the auth token

    fun setAuthToken(token: String) {
        authToken = token
    }

    fun getUserTodos() {
        _userId.value?.let { userId ->
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    Log.d("get user to do", "${userId}")
                    _userTodos.value = apiService.getUserTodos(userId, "Bearer $authToken", apiKey)
                } catch (e: Exception) {
                    _error.value = "Failed to load user todos: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun createUserTodo(description: String) {
        _userId.value?.let { userId ->
            viewModelScope.launch {
                try {
                    val request = ToDoItemRequest(description = description)
                    val newTodo = apiService.createUserTodo(userId, "Bearer $authToken", apiKey, request)
                    _userTodos.value = _userTodos.value + newTodo
                } catch (e: Exception) {
                    _error.value = "Failed to create user todo: ${e.message}"
                }
            }
        }
    }

    fun updateUserTodo(id: Long, description: String, completed: Boolean) {
        _userId.value?.let { userId ->
            viewModelScope.launch {
                try {
                    val request = ToDoItemRequest(description = description, completed = completed)
                    val updatedTodo = apiService.updateUserTodo(userId, id, "Bearer $authToken",  apiKey,  request)
                    _userTodos.value = _userTodos.value.map { if (it.id == id) updatedTodo else it }
                } catch (e: Exception) {
                    _error.value = "Failed to update user todo: ${e.message}"
                }
            }
        }
    }

    fun deleteUserTodo(id: Long) {
        _userId.value?.let { userId ->
            viewModelScope.launch {
                try {
                    apiService.deleteUserTodo(userId, id, "Bearer $authToken", apiKey)
                    _userTodos.value = _userTodos.value.filter { it.id != id }
                } catch (e: Exception) {
                    _error.value = "Failed to delete user todo: ${e.message}"
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
