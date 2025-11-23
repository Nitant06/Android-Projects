package com.example.chitchat.feature_auth.presentation.login

import com.example.chitchat.feature_auth.domain.use_case.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.example.chitchat.core.domain.model.Result

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val mockLoginUseCase: LoginUseCase = mock()

    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(mockLoginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLoginClick with successful result updates state to success`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        whenever(mockLoginUseCase(email, password)).thenReturn(Result.Success(Unit))

        viewModel.onLoginClick(email, password)

        assertFalse(viewModel.state.isLoading)
        assertTrue(viewModel.state.isLoginSuccess)
        assertNull(viewModel.state.loginError)
    }

    @Test
    fun `onLoginClick with error result updates state with error message`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "Invalid credentials"
        whenever(mockLoginUseCase(email, password)).thenReturn(Result.Error(Exception(errorMessage)))

        viewModel.onLoginClick(email, password)

        assertFalse(viewModel.state.isLoading)
        assertFalse(viewModel.state.isLoginSuccess)
        assertEquals(errorMessage, viewModel.state.loginError)
    }
}