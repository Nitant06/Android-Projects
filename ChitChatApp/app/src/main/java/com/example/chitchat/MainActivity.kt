package com.example.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chitchat.core.ui.theme.ChitChatTheme
import com.example.chitchat.feature_auth.presentation.auth_state.rememberAuthState
import com.example.chitchat.feature_auth.presentation.login.LoginScreen
import com.example.chitchat.feature_auth.presentation.signup.SignupScreen
import com.example.chitchat.feature_chat.presentation.chat.ChatScreen
import com.example.chitchat.feature_conversations.presentation.conversations.ConversationsScreen
import com.example.chitchat.feature_conversations.presentation.new_chat.NewChatScreen
import com.example.chitchat.feature_profile.presentation.profile.ProfileScreen
import com.example.chitchat.feature_profile.presentation.profile.ViewProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChitChatTheme {
                Surface {
                    val navController = rememberNavController()
                    val authState by rememberAuthState()

                    NavHost(
                        navController = navController,
                        startDestination = if (authState != null) "main_graph" else "auth_graph"
                    ) {
                        navigation(startDestination = "login", route = "auth_graph") {
                            composable("login") {
                                LoginScreen(
                                    onLoginSuccess = {
                                        navController.navigate("main_graph") {
                                            popUpTo("auth_graph") {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onNavigateToSignup = { navController.navigate("signup") }
                                )
                            }

                            composable("signup") {
                                SignupScreen(
                                    onSignupSuccess = {
                                        navController.navigate("main_graph") {
                                            popUpTo("auth_graph") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        navigation(startDestination = "conversations", route = "main_graph") {
                            composable("conversations") {
                                ConversationsScreen(
                                    onUserClick = { userId ->
                                        navController.navigate("chat/$userId")
                                    },
                                    onLogout = {
                                        navController.navigate("auth_graph") { popUpTo("main_graph") { inclusive = true } }
                                    },
                                    onNavigateToProfile = {
                                        navController.navigate("profile")
                                    },
                                    onNavigateToNewChat = { navController.navigate("new_chat") }
                                )
                            }

                            composable("new_chat") {
                                NewChatScreen(
                                    onUserClick = { userId ->
                                        navController.navigate("chat/$userId") {
                                            popUpTo("new_chat") { inclusive = true }
                                        }
                                    },
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("profile") {
                                ProfileScreen()
                            }

                            composable(
                                route = "chat/{otherUserId}",
                                arguments = listOf(
                                    navArgument("otherUserId") { type = NavType.StringType }
                                )
                            ) {
                                ChatScreen(
                                    onNavigateBack = { navController.popBackStack() },
                                    onProfileClick = { userId ->
                                        navController.navigate("view_profile/$userId")
                                    }
                                )
                            }

                            composable(
                                route = "view_profile/{userId}",
                                arguments = listOf(navArgument("userId") { type = NavType.StringType })
                            ) {
                                ViewProfileScreen(
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

