package com.example.booknest.util

import android.app.Activity
import android.widget.Toast
import com.razorpay.Checkout
import org.json.JSONObject
import kotlin.math.roundToInt
import com.example.booknest.BuildConfig

class RazorpayManager(private val activity: Activity) {

    init {
        Checkout.preload(activity.applicationContext)
    }

    fun startPayment(
        amount: Double,
        email: String,
        phone: String,
        name: String
    ) {
        val checkout = Checkout()
        checkout.setKeyID(BuildConfig.RAZORPAY_KEY)

        try {
            val options = JSONObject()
            options.put("name", "BookNest")
            options.put("description", "Hotel Booking Payment")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")

            val amountInPaise = (amount * 100).roundToInt()
            options.put("amount", amountInPaise)

            val prefill = JSONObject()
            prefill.put("email", email)
            prefill.put("contact", phone)

            options.put("prefill", prefill)

            // Open Razorpay Checkout
            checkout.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, "Error initializing payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}