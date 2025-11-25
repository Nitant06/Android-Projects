package com.example.booknest.ui.screens.where2go

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.booknest.util.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Where2GoScreen(
    navController: NavController,
    viewModel: Where2GoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.places) { place ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            val encodedPlaceName = URLEncoder.encode(place.name, StandardCharsets.UTF_8.toString())
                            navController.navigate("${Constants.PLACE_DETAILS_ROUTE}/$encodedPlaceName")
                        },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = place.imageUrl,
                                contentDescription = place.name,
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentScale = ContentScale.Crop)
                            Text(place.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }

}