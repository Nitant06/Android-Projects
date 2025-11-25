package com.example.booknest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booknest.ui.screens.checkout.CheckOutScreen
import com.example.booknest.ui.screens.faq.FaqScreen
import com.example.booknest.ui.screens.main.MainScreen
import com.example.booknest.ui.screens.placeDetails.PlaceDetailsScreen
import com.example.booknest.ui.screens.selectHotel.SelectHotelScreen
import com.example.booknest.ui.screens.selectRoom.SelectRoomScreen
import com.example.booknest.ui.screens.sendotp.SendOtpScreen
import com.example.booknest.ui.screens.sendotp.SendOtpViewModel
import com.example.booknest.ui.screens.splash.SplashScreen
import com.example.booknest.ui.screens.verifyotp.VerifyOtpScreen
import com.example.booknest.ui.screens.verifyotp.VerifyOtpViewModel
import com.example.booknest.ui.screens.where2go.Where2GoScreen
import com.example.booknest.util.Constants

@Composable
fun AppNavigation(startDestination: String){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){

        composable(route = Constants.SPLASH_ROUTE) {
            SplashScreen(navController = navController)
        }

        composable(route = Constants.SEND_OTP_ROUTE){
            val viewModel = hiltViewModel<SendOtpViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            SendOtpScreen(
                navController = navController,
                viewModel = viewModel,
                onNavigateToVerify = { verificationId ->
                    navController.navigate(
                        "${Constants.VERIFY_OTP_ROUTE}/$verificationId/${uiState.phoneNumber}/${uiState.fullName}/${uiState.email}"
                    )
                    viewModel.onNavigationDone()
                }
            )
        }

        composable(
            route = "${Constants.VERIFY_OTP_ROUTE}/{verificationId}/{phoneNumber}/{name}/{email}",
            arguments = listOf(
                navArgument("verificationId") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) {
            val viewModel = hiltViewModel<VerifyOtpViewModel>()

            VerifyOtpScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = Constants.FIND_ROOM_ROUTE) {
            MainScreen(rootNavController = navController)
        }

        composable(
            route = "${Constants.SELECT_HOTEL_ROUTE}/{city}/{numberOfDays}",
            arguments = listOf(
                navArgument("city") { type = NavType.StringType },
                navArgument("numberOfDays") { type = NavType.IntType }
                )
        ) {
            SelectHotelScreen(navController = navController)
        }

        composable(
            route = "${Constants.SELECT_ROOM_ROUTE}/{city}/{hotelId}/{numberOfDays}",
            arguments = listOf(
                navArgument("city") { type = NavType.StringType },
                navArgument("hotelId") { type = NavType.StringType },
                navArgument("numberOfDays") { type = NavType.IntType }
            )
        ) {
            SelectRoomScreen(navController = navController)
        }

        composable(
            route = "${Constants.CHECKOUT_ROUTE}/{city}/{hotelId}/{roomsJson}/{numberOfDays}",
            arguments = listOf(
                navArgument("city") { type = NavType.StringType },
                navArgument("hotelId") { type = NavType.StringType },
                navArgument("roomsJson") { type = NavType.StringType },
                navArgument("numberOfDays") { type = NavType.IntType }
            )
        ) {
            CheckOutScreen(navController = navController)
        }

        composable(Constants.WHERE2GO_ROUTE) {
            Where2GoScreen(navController = navController)
        }

        composable(
            route = "${Constants.PLACE_DETAILS_ROUTE}/{placeName}",
            arguments = listOf(navArgument("placeName") { type = NavType.StringType })
        ) {
            PlaceDetailsScreen(navController = navController)
        }

        composable(Constants.FAQ_ROUTE) {
            FaqScreen()
        }
    }
}