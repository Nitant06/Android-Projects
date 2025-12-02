package com.nitant.docmind.ui.screens.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.nitant.docmind.utils.OcrHelper
import java.io.File

@Composable
fun ResultScreen(
    imagePath: String,
    onAnalyzeClicked: (String) -> Unit,
    onRetakeClicked: () -> Unit
) {
    val context = LocalContext.current
    val ocrHelper = remember { OcrHelper(context) }

    var extractedText by remember { mutableStateOf("Reading text...") }
    var isLoadingOcr by remember { mutableStateOf(true) }

    // 2. Run OCR
    LaunchedEffect(imagePath) {
        isLoadingOcr = true
        try {
            val text = ocrHelper.extractText(File(imagePath).toUri())
            extractedText = text
        } catch (e: Exception) {
            extractedText = "Error reading text: ${e.message}"
        } finally {
            isLoadingOcr = false
        }
    }

    // 3. UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Card
        Card(
            modifier = Modifier.height(250.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(File(imagePath)),
                contentDescription = "Captured Document",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Text Section
        Text(
            text="Extracted Text:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                if (isLoadingOcr) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Text(text = extractedText, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = onRetakeClicked, modifier = Modifier.weight(1f)) {
                Text("Retake")
            }
            Button(
                onClick = { onAnalyzeClicked(extractedText) },
                enabled = !isLoadingOcr && extractedText.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Analyze with AI")
            }
        }
    }
}