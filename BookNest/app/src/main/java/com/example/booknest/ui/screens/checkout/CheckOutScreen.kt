package com.example.booknest.ui.screens.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.booknest.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    navController: NavController,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hotel = uiState.hotel

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (context is MainActivity) {
                        val name = uiState.userDetails.name ?: "Guest"
                        val email = uiState.userDetails.email ?: "guest@test.com"
                        val phone = uiState.userDetails.phone ?: "9999999999"

                        context.triggerPayment(
                            amount = uiState.totalAmount,
                            email = email,
                            phone = phone,
                            name = name
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                enabled = hotel != null && !uiState.isLoading
            ) {
                Text("Confirm & Pay: Rs. ${uiState.totalAmount.format(2)}")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (hotel != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Booking Details
                item {
                    Text("Booking Details", style = MaterialTheme.typography.titleLarge)
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(hotel.name, style = MaterialTheme.typography.titleMedium)
                            Text("${uiState.numberOfDays} Days")
                        }
                    }
                }

                // Selected Rooms
                item {
                    Text("Selected Rooms", style = MaterialTheme.typography.titleLarge)
                    uiState.selectedRooms.forEach { roomDetails ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(
                                Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(roomDetails.type, fontWeight = FontWeight.Bold)
                                    Text("Rs. ${roomDetails.price}")
                                }
                                TextButton(onClick = { viewModel.onRemoveRoom(roomDetails) }) {
                                    Text("Remove")
                                }
                            }
                        }
                    }
                }

                // Price Breakup
                item {
                    Text("Price Breakup", style = MaterialTheme.typography.titleLarge)
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PriceRow("Room Price", "Rs. ${uiState.roomPrice.format(2)}")
                            PriceRow("GST (18%)", "Rs. ${uiState.gst.format(2)}")
                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )
                            PriceRow("To Pay", "Rs. ${uiState.totalAmount.format(2)}", isTotal = true)
                        }
                    }
                }

                // User Details
                item {
                    Text("User Details", style = MaterialTheme.typography.titleLarge)
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Name: ${uiState.userDetails.name ?: "N/A"}")
                            Text("Email: ${uiState.userDetails.email ?: "N/A"}")
                            Text("Phone: ${uiState.userDetails.phone ?: "N/A"}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PriceRow(label: String, value: String, isTotal: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
    }
}

fun Number.format(digits: Int) = "%.${digits}f".format(this)