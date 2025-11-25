package com.example.booknest.ui.screens.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.booknest.ui.screens.faq.FaqScreen
import com.example.booknest.ui.screens.findroom.FindRoomScreen
import com.example.booknest.ui.screens.findroom.FindRoomViewModel
import com.example.booknest.ui.screens.where2go.Where2GoScreen
import com.example.booknest.util.Constants

sealed class BottomNavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : BottomNavItem(Constants.FIND_ROOM_ROUTE, Icons.Default.Home, "Home")
    object Where2Go : BottomNavItem(Constants.WHERE2GO_ROUTE, Icons.Default.Place, "Where2Go")
    object FAQs : BottomNavItem(Constants.FAQ_ROUTE, Icons.Default.Info, "FAQs")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(rootNavController: androidx.navigation.NavController) {
    val navController = rememberNavController()
    val items = listOf(BottomNavItem.Home, BottomNavItem.Where2Go, BottomNavItem.FAQs)
    var topBarTitle by remember { mutableStateOf("Find Room") }

    val findRoomViewModel: FindRoomViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        findRoomViewModel.logoutEvent.collect {
            rootNavController.navigate(Constants.SEND_OTP_ROUTE) {
                popUpTo(rootNavController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                actions = {
                    TextButton(
                        onClick = findRoomViewModel::onLogoutClicked
                    ) {
                        Text("Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true

                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Constants.FIND_ROOM_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Constants.FIND_ROOM_ROUTE) {
                topBarTitle = "Find Room"
                FindRoomScreen(navController = rootNavController, viewModel = findRoomViewModel)
            }
            composable(Constants.WHERE2GO_ROUTE) {
                topBarTitle = "Where2Go"
                Where2GoScreen(navController = rootNavController)
            }
            composable(Constants.FAQ_ROUTE) {
                topBarTitle = "FAQs"
                FaqScreen()
            }
        }
    }
}