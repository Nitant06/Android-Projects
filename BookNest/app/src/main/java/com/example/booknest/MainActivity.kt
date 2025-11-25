package com.example.booknest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.booknest.ui.navigation.AppNavigation
import com.example.booknest.ui.theme.BookNestTheme
import com.example.booknest.util.Constants
import com.example.booknest.util.RazorpayManager
import com.google.firebase.FirebaseApp
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener {

    private lateinit var razorpayManager: RazorpayManager

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        FirebaseApp.initializeApp(this)

        razorpayManager = RazorpayManager(this)

        setContent {
            BookNestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(startDestination = Constants.SPLASH_ROUTE)
                }
            }
        }
    }

    fun triggerPayment(amount: Double, email: String, phone: String, name: String) {
        razorpayManager.startPayment(amount, email, phone, name)
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful! ID: $razorpayPaymentID", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }
}



