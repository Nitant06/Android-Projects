# BookNest

BookNest is a modern Hotel Booking Android application built using Jetpack Compose.  
It provides a seamless UI flow starting from onboarding and authentication to browsing hotels, selecting rooms, confirming bookings, completing payments, and checking booking status.

---

## Features

- Splash + Intro navigation
- Signup with phone authentication (OTP verification)
- Home screen with destination entry
- Hotel and room browsing UI
- Room selection with pricing breakdown
- Razorpay secure payment integration
- Transaction summary post-payment
- FAQ / Support section
- Clean Material UI using Jetpack Compose

---

## Tech Stack

| Category | Technology |
|---------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM |
| Auth | Firebase Phone Authentication |
| Payments | Razorpay SDK |
| Navigation | Jetpack Navigation Compose |
| State Management | ViewModel, StateFlow |
| Build | Gradle Kotlin DSL |

---

## App Flow & Screenshots

| Step | Screen | Preview |
|------|--------|---------|
| 1 | Splash Screen | ![Splash](./screenshots/splashscreen.png) |
| 2 | Signup Screen | ![Signup](./screenshots/signup_screen.png) |
| 3 | OTP Verification | ![OTP](./screenshots/otp_screen.png) |
| 4 | Homepage (Search Destination) | ![Homepage](./screenshots/homepage_screen.png) |
| 5 | Hotel Selection Screen | ![Select Hotel](./screenshots/select_hotel_screen.png) |
| 6 | Room Listing Screen | ![Select Room](./screenshots/select_room_screen.png) |
| 7 | Room Details Screen | ![Details](./screenshots/details_screen.png) |
| 8 | Checkout Screen | ![Checkout](./screenshots/checkout_screen.png) |
| 9 | Razorpay Payment UI | ![Razorpay](./screenshots/razorpay_payment_screen.png) |
| 10 | Transaction Page | ![Transaction](./screenshots/transaction_page.png) |
| 11 | Payment Success Screen | ![Success](./screenshots/payment_successful_screen.png) |
| 12 | FAQ / Support Screen | ![FAQ](./screenshots/faq_screen.png) |


## Project Structure

app/
├─ data/
├─ ui/
│ ├─ screens/…
│ ├─ navigation/
├─ util/
├─ MainActivity.kt

## Firebase Requirements
### Enable Phone Authentication

## Razorpay Setup

### Place your test/live key here:

const val RAZORPAY_KEY_ID = "YOUR_KEY"

