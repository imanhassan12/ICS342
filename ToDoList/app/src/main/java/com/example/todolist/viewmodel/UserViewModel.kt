package com.example.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.models.ToDoItemRequest
import com.example.todolist.network.ToDoApiService
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

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
                    Log.d("getUserTodos", "Fetching todos for userId: $userId")
                    _userTodos.value = apiService.getUserTodos(userId, "Bearer $authToken", apiKey)
                } catch (e: Exception) {
                    val errorMessage = if (e is HttpException) {
                        e.response()?.errorBody()?.string() ?: "Unknown error"
                    } else {
                        e.message ?: "Unknown error"
                    }
                    _error.value = "Failed to load user todos: $errorMessage"
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
                    val errorMessage = if (e is HttpException) {
                        // Try to parse the error body
                        e.response()?.errorBody()?.string() ?: "Unknown error"
                    } else {
                        // Fallback to the exception message
                        e.message ?: "Unknown error"
                    }

                    _error.value = "Failed to create user todo: $errorMessage"
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
                    val errorMessage = if (e is HttpException) {
                        // Try to parse the error body
                        e.response()?.errorBody()?.string() ?: "Unknown error"
                    } else {
                        // Fallback to the exception message
                        e.message ?: "Unknown error"
                    }

                    _error.value = "Failed to update user todo: $errorMessage"
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
