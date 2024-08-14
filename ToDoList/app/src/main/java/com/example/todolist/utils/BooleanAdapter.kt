package com.example.todolist.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class BooleanAdapter {
    @ToJson
    fun toJson(value: Boolean): Int {
        return if (value) 1 else 0
    }

    @FromJson
    fun fromJson(value: Int): Boolean {
        return value == 1
    }
}