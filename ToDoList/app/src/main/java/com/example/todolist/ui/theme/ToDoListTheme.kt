package com.example.todolist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.todolist.R

@Composable
fun ToDoListTheme(content: @Composable () -> Unit) {
    val darkColorPalette = darkColorScheme(
        primary = colorResource(id = R.color.Purple200),
        primaryContainer = colorResource(id = R.color.Purple700),
        secondary = colorResource(id = R.color.Teal200)
    )

    val lightColorPalette = lightColorScheme(
        primary = colorResource(id = R.color.Purple500),
        primaryContainer = colorResource(id = R.color.Purple700),
        secondary = colorResource(id = R.color.Teal200)
    )

    val colors = lightColorPalette // Or darkColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
