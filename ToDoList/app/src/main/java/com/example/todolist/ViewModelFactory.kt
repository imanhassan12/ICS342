package com.example.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.network.ToDoApiService
import com.example.todolist.viewmodel.AuthViewModel
import com.example.todolist.viewmodel.ToDoViewModel
import com.example.todolist.viewmodel.UserViewModel

class ViewModelFactory(private val apiService: ToDoApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(apiService) as T
            modelClass.isAssignableFrom(ToDoViewModel::class.java) -> ToDoViewModel(apiService) as T
            modelClass.isAssignableFrom(UserViewModel::class.java) -> UserViewModel(apiService) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
