package com.nitant.docmind.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nitant.docmind.domain.models.DocType
import com.nitant.docmind.domain.models.Document
import com.nitant.docmind.presentation.scan.ScanUiState
import com.nitant.docmind.presentation.scan.ScanViewModel
import com.nitant.docmind.ui.screens.camera.CameraScreen
import com.nitant.docmind.ui.screens.detail.DetailScreen
import com.nitant.docmind.ui.screens.home.HomeScreen
import com.nitant.docmind.ui.screens.result.ResultScreen
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scanViewModel: ScanViewModel = koinViewModel()
    val scanState by scanViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = HomeRoute) {

        // 1. HOME SCREEN
        composable<HomeRoute> {
            HomeScreen(
                onFabClicked = { navController.navigate(CameraRoute) },
                onDocumentClicked = { doc ->
                    navController.navigate(
                        DetailRoute(
                            title = doc.title,
                            type = doc.type.name,
                            summary = doc.summary,
                            imagePath = doc.imagePath,
                            timestamp = doc.timestamp,
                            id = doc.id
                        )
                    )
                }
            )
        }

        // 2. CAMERA SCREEN
        composable<CameraRoute> {
            CameraScreen(
                onImageCaptured = { path ->
                    navController.navigate(ResultRoute(imagePath = path))
                }
            )
        }

        // 3. RESULT SCREEN
        composable<ResultRoute> { backStackEntry ->
            val route: ResultRoute = backStackEntry.toRoute()

            LaunchedEffect(scanState) {
                if (scanState is ScanUiState.Success) {
                    scanViewModel.resetState()
                    navController.popBackStack(HomeRoute, inclusive = false)
                }
            }

            ResultScreen(
                imagePath = route.imagePath,
                onRetakeClicked = { navController.popBackStack() },
                onAnalyzeClicked = { text ->
                    scanViewModel.analyzeImage(text, route.imagePath)
                }
            )

            // AI Loading/Error Dialogs
            when (val state = scanState) {
                is ScanUiState.Loading -> {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("AI is thinking...") },
                        text = { CircularProgressIndicator() },
                        confirmButton = {}
                    )
                }
                is ScanUiState.Error -> {
                    AlertDialog(
                        onDismissRequest = { scanViewModel.resetState() },
                        title = { Text("Error") },
                        text = { Text(state.message) },
                        confirmButton = { Button(onClick = { scanViewModel.resetState() }) { Text("Close") } }
                    )
                }
                else -> {}
            }
        }

        // 4. DETAIL SCREEN
        composable<DetailRoute> { backStackEntry ->
            val route: DetailRoute = backStackEntry.toRoute()

            val doc = Document(
                id = route.id,
                title = route.title,
                summary = route.summary,
                type = try { DocType.valueOf(route.type) } catch (_: Exception) { DocType.OTHER },
                timestamp = route.timestamp,
                imagePath = route.imagePath
            )

            DetailScreen(
                doc = doc,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}