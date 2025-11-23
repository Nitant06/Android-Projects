package com.example.chitchat.feature_auth.presentation.signup

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.chitchat.core.ui.theme.ChitChatTheme
import com.example.chitchat.feature_auth.HiltTestActivity
import com.example.chitchat.feature_auth.data.FakeAuthRepository
import com.example.chitchat.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SignupScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var fakeRepository: AuthRepository

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.setContent {
            ChitChatTheme {
                SignupScreen(
                    onSignupSuccess = { },
                )
            }
        }
    }

    @Test
    fun signupScreen_displaysAllUIElements() {
        composeTestRule.onNodeWithText("Create Account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
    }

    @Test
    fun whenSignupFails_errorMessageIsShownOnScreen() {
        (fakeRepository as FakeAuthRepository).signupShouldReturnError = true

        val emailField = composeTestRule.onNodeWithText("Email")
        val passwordField = composeTestRule.onNodeWithText("Password")
        val signupButton = composeTestRule.onNodeWithText("Sign Up")

        // 1. Simulate user typing
        emailField.performTextInput("test@example.com")
        passwordField.performTextInput("password123")

        // 2. Simulate user clicking the button
        signupButton.performClick()

        // 3. Check that the error message from our fake repository is now displayed
        composeTestRule.onNodeWithText("Invalid email or password").assertIsDisplayed()
    }
}