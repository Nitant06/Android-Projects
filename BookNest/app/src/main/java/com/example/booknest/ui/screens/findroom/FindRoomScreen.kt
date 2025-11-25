package com.example.booknest.ui.screens.findroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.booknest.data.model.Place
import com.example.booknest.util.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(
    navController: NavController,
    viewModel: FindRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var isCityDropdownExpanded by remember { mutableStateOf(false) }

    var showCheckInDatePicker by remember { mutableStateOf(false) }
    var showCheckOutDatePicker by remember { mutableStateOf(false) }

    val checkInDatePickerState = rememberDatePickerState()
    val checkOutDatePickerState = rememberDatePickerState()


    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { route ->
            navController.navigate(route)
        }
    }

        if (uiState.isLoading && uiState.cities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        ExposedDropdownMenuBox(
                            expanded = isCityDropdownExpanded,
                            onExpandedChange = { isCityDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = uiState.selectedCity ?: "Where you want to go?",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = isCityDropdownExpanded,
                                onDismissRequest = { isCityDropdownExpanded = false }
                            ) {
                                uiState.cities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city) },
                                        onClick = {
                                            viewModel.onCitySelected(city)
                                            isCityDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.checkInDate ?: "Check-in Date",
                            onValueChange = {},
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showCheckInDatePicker = true
                                },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.checkOutDate ?: "Check-out Date", onValueChange = {},
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showCheckOutDatePicker = true
                                },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Number of Rooms", style = MaterialTheme.typography.bodyLarge)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.onRoomCountChanged(uiState.numberOfRooms - 1) }) {
                                    Icon(
                                        Icons.Default.RemoveCircle,
                                        contentDescription = "Decrease rooms"
                                    )
                                }
                                Text(
                                    uiState.numberOfRooms.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.onRoomCountChanged(uiState.numberOfRooms + 1) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase rooms")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = viewModel::onSearchClicked,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SEARCH")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("BEST PLACES", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { navController.navigate(Constants.WHERE2GO_ROUTE) }) {
                        Text("VIEW ALL")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.recommendedPlaces) { place ->
                        PlaceCard(place = place) {
                            val encodedPlaceName =
                                URLEncoder.encode(place.name, StandardCharsets.UTF_8.toString())
                            navController.navigate("${Constants.PLACE_DETAILS_ROUTE}/$encodedPlaceName")
                        }

                    }
                }
            }
        }


    if (showCheckInDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showCheckInDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    checkInDatePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onCheckInDateSelected(millis.toFormattedDateString())
                    }
                    showCheckInDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCheckInDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = checkInDatePickerState)
        }
    }

    if (showCheckOutDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showCheckOutDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    checkOutDatePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onCheckOutDateSelected(millis.toFormattedDateString())
                    }
                    showCheckOutDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCheckOutDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = checkOutDatePickerState)
        }
    }
}

private fun Long.toFormattedDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

@Composable
fun PlaceCard(place: Place, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = place.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
