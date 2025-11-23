package com.example.chitchat.feature_auth

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_auth.domain.use_case.SignupUseCase
import com.example.chitchat.feature_auth.presentation.signup.SignupViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignupViewModelTest {

    private val signupUseCase: SignupUseCase = mockk()

    private lateinit var viewModel: SignupViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignupViewModel(signupUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when signup is successful, state is updated to success`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        coEvery { signupUseCase(email, password) } returns Result.Success(Unit)

        viewModel.onSignupClick(email, password)

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(true, viewModel.state.isSignupSuccess)
        assertNull(viewModel.state.signupError)
    }

    @Test
    fun `when signup fails, state is updated with an error`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "This is a test error"
        coEvery { signupUseCase(email, password) } returns Result.Error(Exception(errorMessage))

        viewModel.onSignupClick(email, password)

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isSignupSuccess)
        assertNotNull(viewModel.state.signupError)
        assertEquals(errorMessage, viewModel.state.signupError)
    }
}