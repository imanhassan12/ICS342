package com.example.todolist.viewmodel

import com.example.todolist.models.UserResponse
import com.example.todolist.models.UserLoginRequest
import com.example.todolist.models.UserRegisterRequest
import com.example.todolist.network.ToDoApiService
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val apiService = mockk<ToDoApiService>()
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(apiService)
    }

    @After
    fun tearDown() {
        clearMocks(apiService)
    }

    @Test
    fun `registerUser success`() = runTest {
        // Given
        val userResponse = UserResponse(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            token = "token123",
            enabled = true,
            admin = 1
        )
        val request = UserRegisterRequest(
            name = "Test User",
            email = "test@example.com",
            password = "password"
        )

        coEvery { apiService.registerUser(any(), request) } returns userResponse

        // When
        authViewModel.registerUser("Test User", "test@example.com", "password")

        // Then
        assertEquals(userResponse, authViewModel.user.value)
        assertNull(authViewModel.error.value)
        assertEquals(false, authViewModel.isLoading.value)

        coVerify { apiService.registerUser(any(), request) }
    }

    @Test
    fun `registerUser failure`() = runTest {
        // Given
        val request = UserRegisterRequest(
            name = "Test User",
            email = "test@example.com",
            password = "password"
        )
        val exception = mockk<HttpException>()
        every { exception.response()?.errorBody()?.string() } returns "Unknown error"
        coEvery { apiService.registerUser(any(), any()) } throws exception

        // When
        authViewModel.registerUser("Test User", "test@example.com", "password")

        // Then
        assertNull(authViewModel.user.value)
        assertEquals("Failed to register user: Unknown error", authViewModel.error.value)
        assertEquals(false, authViewModel.isLoading.value)

        coVerify { apiService.registerUser(any(), request) }
    }

    @Test
    fun `loginUser success`() = runTest {
        // Given
        val userResponse = UserResponse(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            token = "token123",
            enabled = true,
            admin = 1
        )
        val request = UserLoginRequest(email = "test@example.com", password = "password")

        coEvery { apiService.loginUser(any(), request) } returns userResponse

        // When
        authViewModel.loginUser("test@example.com", "password")

        // Then
        assertEquals(userResponse, authViewModel.user.value)
        assertNull(authViewModel.error.value)
        assertEquals(true, authViewModel.loginSuccess.value)
        assertEquals(false, authViewModel.isLoading.value)

        coVerify { apiService.loginUser(any(), request) }
    }

    @Test
    fun `loginUser failure`() = runTest {
        // Given
        val request = UserLoginRequest(email = "test@example.com", password = "password")
        val exception = mockk<HttpException>()
        every { exception.response()?.errorBody()?.string() } returns "Unknown error"
        coEvery { apiService.loginUser(any(), any()) } throws exception

        // When
        authViewModel.loginUser("test@example.com", "password")

        // Then
        assertNull(authViewModel.user.value)
        assertEquals("Failed to log in: Unknown error", authViewModel.error.value)
        assertEquals(false, authViewModel.loginSuccess.value)
        assertEquals(false, authViewModel.isLoading.value)

        coVerify { apiService.loginUser(any(), request) }
    }
}
// Custom rule to set the main dispatcher to a TestCoroutineDispatcher for unit testing
@ExperimentalCoroutinesApi
class MainDispatcherRule : TestWatcher() {
    private val testDispatcher = UnconfinedTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
