package com.example.todolist.models

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
