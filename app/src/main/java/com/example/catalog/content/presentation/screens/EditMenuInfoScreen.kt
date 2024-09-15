package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.GoBackNavigationButton
import com.example.catalog.content.presentation.common.LoadingStateView
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.EditMenuInfoView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditMenuInfoScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
) {
    val menuInfoData by contentViewModel.getMenuInfoDataFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    var snackBarMsg by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    var updatedImageModel: Uri? by remember { mutableStateOf(menuInfoData.updatedImageModel) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            uri?.let { notNullUri ->
                updatedImageModel = notNullUri
            } ?: run { snackBarMsg = "No image was selected" }
        }

    if (uiIntent is ContentUiIntents.ShowSnackBarMsg) {
        snackBarMsg = (uiIntent as ContentUiIntents.ShowSnackBarMsg).msg
        contentViewModel.clearUiIntents()
    }

    LaunchedEffect(key1 = snackBarMsg) {
        snackBarMsg?.let { msg ->
            snackBarHostState.showSnackbar(
                message = msg,
                actionLabel = "Result of action",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit menu info") },
                navigationIcon = {
                    GoBackNavigationButton {
                        contentViewModel.setUiEvent(ContentUiEvents.GoBack)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData: SnackbarData ->
                Snackbar(modifier = modifier) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        },
    ) { innerPadding ->
        when (uiState) {
            is ContentUiStates.Loading -> {
                LoadingStateView(innerPadding = innerPadding)
            }

            is ContentUiStates.Show -> {
                EditMenuInfoView(
                    innerPadding = innerPadding,
                    menuInfoData = menuInfoData,
                    onEventHandler = { event ->
                        contentViewModel.setUiEvent(event)
                    },
                    onChooseImage = {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    updatedImageModel = updatedImageModel,
                    onClearUpdatedImageModel = {
                        updatedImageModel = null
                    }
                )
            }

            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding) {
                    contentViewModel.setUiEvent(ContentUiEvents.DownloadMenuInfo)
                }
            }
        }
    }
}