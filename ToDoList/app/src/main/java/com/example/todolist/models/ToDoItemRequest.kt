package com.example.todolist.models

data class ToDoItemRequest(
    val description: String,
    val completed: Boolean = false,
    val meta: Any? = null
)
