package com.example.todolist.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.todolist.models.UserResponse
import com.example.todolist.models.UserLoginRequest
import com.example.todolist.models.UserRegisterRequest
import com.example.todolist.network.ToDoApiService
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

class AuthViewModel(private val apiService: ToDoApiService) : BaseViewModel() {

    private val _user = MutableStateFlow<UserResponse?>(null)
    val user: StateFlow<UserResponse?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _error = MutableStateFlow<String?>(null) // Error state
    val error: StateFlow<String?> = _error

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val request = UserRegisterRequest(name, email, password)
                _user.value = apiService.registerUser(apiKey, request)
            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    // Try to parse the error body
                    e.response()?.errorBody()?.string() ?: "Unknown error"
                } else {
                    // Fallback to the exception message
                    e.message ?: "Unknown error"
                }

                _error.value = "Failed to register user: $errorMessage"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val request = UserLoginRequest(email, password)
                _user.value = apiService.loginUser(apiKey, request)
                _loginSuccess.value = true
            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    // Try to parse the error body
                    e.response()?.errorBody()?.string() ?: "Unknown error"
                } else {
                    // Fallback to the exception message
                    e.message ?: "Unknown error"
                }

                _error.value = "Failed to log in: $errorMessage"
                _loginSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logoutUser() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                apiService.logoutUser(apiKey)
                _user.value = null
            } catch (e: Exception) {
                _error.value = "Failed to log out: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearLoginSuccess() {
        _loginSuccess.value = false
    }
}
