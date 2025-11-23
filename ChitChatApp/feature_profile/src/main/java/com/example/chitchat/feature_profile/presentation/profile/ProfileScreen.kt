package com.example.chitchat.feature_profile.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chitchat.core.domain.model.User
import com.example.chitchat.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onImageSelected(it) }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.light_blue_bg)),
                actions = {
                    IconButton(onClick = { viewModel.saveProfile() }, enabled = !state.isLoading) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Default.Check, contentDescription = "Save Profile")
                        }
                    }
                }
                )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && state.user == null) {
                CircularProgressIndicator()
            } else if (state.user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileContent(
                        isEditable = true,
                        user = state.user,
                        selectedImageUri = state.selectedImageUri,
                        onDisplayNameChange = { viewModel.onDisplayNameChange(it) },
                        onImageClick = { galleryLauncher.launch("image/*") }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    state.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    isEditable: Boolean,
    user: User,
    selectedImageUri: Uri?,
    onDisplayNameChange: (String) -> Unit,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = selectedImageUri ?: user.photoUrl ?: R.drawable.ic_profile_placeholder,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable(enabled = isEditable, onClick = onImageClick),
            contentScale = ContentScale.Crop
        )

        OutlinedTextField(
            value = user.displayName,
            onValueChange = onDisplayNameChange,
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = !isEditable
        )

        OutlinedTextField(
            value = user.email,
            onValueChange = {},
            readOnly = true,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}