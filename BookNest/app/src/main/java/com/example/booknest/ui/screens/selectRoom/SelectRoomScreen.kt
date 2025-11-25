package com.example.booknest.ui.screens.selectRoom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.booknest.data.model.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRoomScreen(
    navController: NavController,
    viewModel: SelectRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hotel = uiState.hotel

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { route ->
            navController.navigate(route)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Room") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = uiState.selectedRooms.isNotEmpty()) {
                CheckoutBottomBar(
                    rooms = uiState.selectedRooms.size,
                    days = uiState.numberOfDays,
                    cost = uiState.totalCost,
                    onCheckout = viewModel::onCheckoutClicked
                )
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (hotel != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding() + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
                )
            ) {
                item {
                    AsyncImage(
                        model = hotel.imageUrl,
                        contentDescription = hotel.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                item {
                    Column(Modifier.padding(16.dp)) {
                        Text(hotel.name, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
                        RatingBar(rating = hotel.rating)
                        Spacer(Modifier.height(16.dp))
                        Text("About the hotel", style = MaterialTheme.typography.titleMedium)
                        Text(hotel.about, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(16.dp))
                        Text("Amenities available", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(16.dp))
                        Text("Property Rules & Information", style = MaterialTheme.typography.titleMedium)
                        hotel.propertyRules.forEach { rule -> Text("• $rule", style = MaterialTheme.typography.bodyMedium) }
                    }
                }

                item {
                    Text(
                        "Select Room(s)",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(hotel.rooms.filter { it.isAvailable }) { room ->
                    RoomItem(
                        room = room,
                        isSelected = uiState.selectedRooms.contains(room),
                        onSelectChanged = { isSelected ->
                            viewModel.onRoomSelectionChanged(room, isSelected)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Double) {
    Row {
        repeat(5) { index ->
            val icon = if (index < rating) Icons.Filled.Star else Icons.Filled.StarBorder
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(text = " $rating", modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun RoomItem(room: Room, isSelected: Boolean, onSelectChanged: (Boolean) -> Unit) {
    Card(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(room.type, style = MaterialTheme.typography.titleMedium)
                Text("Rs. ${room.price}", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                onClick = { onSelectChanged(!isSelected) },
                shape = RoundedCornerShape(8.dp),
                colors = if (isSelected) {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                } else {
                    ButtonDefaults.outlinedButtonColors()
                },
                border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.secondary) else null
            ) {
                if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                }
                Text(if (isSelected) "SELECTED" else "SELECT")
            }
        }
    }
}

@Composable
fun CheckoutBottomBar(rooms: Int, days: Int, cost: Long, onCheckout: () -> Unit) {
    Surface(
        shadowElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
        ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$rooms Rooms | $days Days | Rs. $cost",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Button(onClick = onCheckout) {
                Text("Checkout")
            }
        }
    }
}