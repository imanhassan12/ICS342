package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.navigation.AppNavigation
import com.example.todolist.network.ToDoApiService
import com.example.todolist.network.RetrofitInstance
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.viewmodel.AuthViewModel
import com.example.todolist.viewmodel.ToDoViewModel
import com.example.todolist.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    // Create instances of the API service
    private val apiService by lazy {
        RetrofitInstance.apiService
    }

    // Initialize ViewModels using ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(apiService)).get(AuthViewModel::class.java)
    }

    private val toDoViewModel: ToDoViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(apiService)).get(ToDoViewModel::class.java)
    }

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(apiService)).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface {
                    MyApp(
                        authViewModel = authViewModel,
                        toDoViewModel = toDoViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp(
    authViewModel: AuthViewModel,
    toDoViewModel: ToDoViewModel,
    userViewModel: UserViewModel
) {
    AppNavigation(
        authViewModel = authViewModel,
        toDoViewModel = toDoViewModel,
        userViewModel = userViewModel
    )
}