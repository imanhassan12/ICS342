package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.CreateAccountScreen
import com.example.todolist.ui.HomeScreen
import com.example.todolist.ui.LoginScreen
import com.example.todolist.viewmodel.AuthViewModel
import com.example.todolist.viewmodel.ToDoViewModel
import com.example.todolist.viewmodel.UserViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    toDoViewModel: ToDoViewModel,
    userViewModel: UserViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                onLoginSuccess = { navController.navigate("home") },
                onNavigateToCreateAccount = { navController.navigate("createAccount") }
            )
        }
        composable(route = "createAccount") {
            CreateAccountScreen(
                navController = navController,
                authViewModel = authViewModel,
                onAccountCreated = { navController.navigate("login") }
            )
        }
        composable(route = "home") {
            HomeScreen(
                navController = navController,
                toDoViewModel = toDoViewModel,
                userViewModel = userViewModel
            )
        }
    }
}
