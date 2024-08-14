package com.example.todolist.models

data class ToDoItem(
    val id: Long,
    val description: String,
    val completed: Any,
    val meta: Any?
)
