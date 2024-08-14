package com.example.todolist.viewmodel

import com.example.todolist.models.ToDoItem
import com.example.todolist.network.ToDoApiService
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val apiService = mockk<ToDoApiService>()
    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        viewModel = UserViewModel(apiService)
    }

    @After
    fun tearDown() {
        clearMocks(apiService)
    }

    @Test
    fun `setUser should update userId`() = runTest {
        val userId = 123L
        viewModel.setUser(userId)

        assertEquals(userId, viewModel.userId.first())
    }

    @Test
    fun `createUserTodo should add new todo to userTodos`() = runTest {
        val userId = 123L
        val description = "New Todo"
        val newTodo = ToDoItem(id = 1, description = description, completed = false, meta = null)

        coEvery { apiService.createUserTodo(userId, any(), any(), any()) } returns newTodo

        viewModel.setUser(userId)
        viewModel.setAuthToken("authToken")
        viewModel.createUserTodo(description)

        assertEquals(listOf(newTodo), viewModel.userTodos.first())
        assertNull(viewModel.error.first())
    }

    @Test
    fun `createUserTodo should handle error`() = runTest {
        val userId = 123L
        val description = "New Todo"
        val exception = mockk<HttpException>()
        every { exception.response()?.errorBody()?.string() } returns "Network error"

        coEvery { apiService.createUserTodo(userId, any(), any(), any()) } throws exception

        viewModel.setUser(userId)
        viewModel.setAuthToken("authToken")
        viewModel.createUserTodo(description)

        assertEquals("Failed to create user todo: Network error", viewModel.error.value)
        assertTrue(viewModel.userTodos.first().isEmpty())
    }

    @Test
    fun `updateUserTodo should update existing todo in userTodos`() = runTest {
        val userId = 123L
        val todo = ToDoItem(id = 1, description = "Todo 1", completed = false, meta = null)
        val updatedTodo = todo.copy(description = "Updated Todo", completed = true, meta = null)

        coEvery { apiService.createUserTodo(userId, any(), any(), any()) } returns todo
        coEvery { apiService.updateUserTodo(userId, todo.id, any(), any(), any()) } returns updatedTodo

        viewModel.setUser(userId)
        viewModel.setAuthToken("authToken")
        viewModel.createUserTodo(todo.description)
        viewModel.updateUserTodo(todo.id, updatedTodo.description, updatedTodo.completed as Boolean)

        assertEquals(listOf(updatedTodo), viewModel.userTodos.first())
        assertNull(viewModel.error.first())
    }

    @Test
    fun `updateUserTodo should handle error`() = runTest {
        val userId = 123L
        val todo = ToDoItem(id = 1, description = "Todo 1", completed = false, meta = null)
        val exception = mockk<HttpException>()
        every { exception.response()?.errorBody()?.string() } returns "Network error"

        coEvery { apiService.createUserTodo(userId, any(), any(), any()) } returns todo
        coEvery { apiService.updateUserTodo(userId, todo.id, any(), any(), any()) } throws exception

        viewModel.setUser(userId)
        viewModel.setAuthToken("authToken")
        viewModel.createUserTodo(todo.description)
        viewModel.updateUserTodo(todo.id, todo.description, todo.completed as Boolean)

        assertEquals("Failed to update user todo: Network error", viewModel.error.value)
        assertEquals(listOf(todo), viewModel.userTodos.first())
    }

    @Test
    fun `getUserTodos should fetch and update userTodos`() = runTest {
        // Arrange
        val userId = 123L
        val expectedTodos = listOf(
            ToDoItem(id = 1, description = "Todo 1", completed = false, meta = null),
            ToDoItem(id = 2, description = "Todo 2", completed = true, meta = null)
        )

        // Mock the API service to return the expected list
        coEvery { apiService.getUserTodos(userId, "Bearer authToken", any()) } returns expectedTodos

        // Act
        viewModel.setUser(userId)
        viewModel.setAuthToken("authToken")
        viewModel.getUserTodos()

        // Assert
        assertEquals(expectedTodos, viewModel.userTodos.value)
        assertNull(viewModel.error.first())
    }
}