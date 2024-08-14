package com.example.todolist.models

data class UserResponse(
    val token: String,
    val id: Long,
    val name: String,
    val email: String,
    val enabled: Boolean,
    val admin: Int
)
