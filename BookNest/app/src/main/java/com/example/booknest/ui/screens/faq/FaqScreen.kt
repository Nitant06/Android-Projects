package com.example.booknest.ui.screens.faq

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booknest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.faqimage),
                contentDescription = "FAQ Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            FAQItem(
                question = "1. What happens if I enter the wrong OTP during account verification?",
                answer = "If you enter the wrong OTP, you will be shown an error message and will have to re-enter the correct code. Please mention the correct OTP that you receive on your registered mobile number via SMS to proceed with the verification process successfully."
            )

            Spacer(modifier = Modifier.height(16.dp))

            FAQItem(
                question = "2. Where can I contact if I face any issues while booking a hotel room using the app?",
                answer = "If you face any issues during your booking process you can reach out to our customer support team by at dummy@book_nest.com or call us at 9999999999."
            )

            Spacer(modifier = Modifier.height(16.dp))

            FAQItem(
                question = "3. Is my personal information secure when using this app?",
                answer = "Yes, we value the trust of our users and prioritise the security of their personal information. All your data, including personal details and booking information is secure with us and protected using encryption protocols to ensure confidentiality."
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = question,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = answer,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color.Black
        )
    }
}