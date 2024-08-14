package com.example.todolist.network

import com.example.todolist.models.*
import retrofit2.http.*

interface ToDoApiService {

    // 3. Retrieve all todos for a user
    @GET("/api/users/{user_id}/todos")
    suspend fun getUserTodos(
        @Path("user_id") userId: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String
    ): List<ToDoItem>

    // 4. Create a new todo for a user
    @POST("/api/users/{user_id}/todos")
    suspend fun createUserTodo(
        @Path("user_id") userId: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String,
        @Body todo: ToDoItemRequest
    ): ToDoItem

    // 5. Retrieve specific todo (Generic)
    @GET("/api/todos/{id}")
    suspend fun getTodo(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String
    ): ToDoItem

    // 6. Update specific todo (Generic)
    @PUT("/api/todos/{id}")
    suspend fun updateTodo(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String,
        @Body todo: ToDoItemRequest
    ): ToDoItem

    // 7. Retrieve specific todo for a user
    @GET("/api/users/{user_id}/todos/{id}")
    suspend fun getUserTodo(
        @Path("user_id") userId: Long,
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String
    ): ToDoItem

    // 8. Update specific todo for a user
    @PUT("/api/users/{user_id}/todos/{id}")
    suspend fun updateUserTodo(
        @Path("user_id") userId: Long,
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String,
        @Body todo: ToDoItemRequest
    ): ToDoItem

    // 9. Delete specific todo (Generic)
    @DELETE("/api/todo/{id}")
    suspend fun deleteTodo(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String
    )

    // 10. Delete specific todo for a user
    @DELETE("/api/users/{user_id}/todo/{id}")
    suspend fun deleteUserTodo(
        @Path("user_id") userId: Long,
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Query("apikey") apiKey: String
    )

    // 11. Delete all todos (Generic)
    @DELETE("/api/todos/")
    suspend fun deleteAllTodos(
        @Query("apikey") apiKey: String,
        @Header("Authorization") token: String,
        @Query("completed") completed: Boolean? = null
    )

    // 12. Delete all todos for a user
    @DELETE("/api/users/{user_id}/todos/")
    suspend fun deleteAllUserTodos(
        @Path("user_id") userId: Long,
        @Query("apikey") apiKey: String,
        @Header("Authorization") token: String,
        @Query("completed") completed: Boolean? = null
    )

    // 13. User registration
    @Headers("Content-Type: application/json")
    @POST("/api/users/register")
    suspend fun registerUser(
        @Query("apikey") apiKey: String,
        @Body request: UserRegisterRequest
    ): UserResponse

    // 14. User login
    @POST("/api/users/login")
    suspend fun loginUser(
        @Query("apikey") apiKey: String,
        @Body request: UserLoginRequest
    ): UserResponse

    // 15. User logout
    @POST("/api/users/logout")
    suspend fun logoutUser(
        @Query("apikey") apiKey: String
    )
}
