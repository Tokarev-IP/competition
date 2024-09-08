package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.EditDishView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditDishScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishData by contentViewModel.getDishItemDataFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarMsg by remember { mutableStateOf<String?>(null) }

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                contentViewModel.setUiEvent(ContentUiEvents.SetInitialImage(uri))
            }
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
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData: SnackbarData ->
                Snackbar(modifier = modifier) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Dish") },
                navigationIcon = {
                    IconButton(
                        onClick = { contentViewModel.setUiEvent(ContentUiEvents.GoBack) }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
    ) { innerPadding ->

        when (uiState) {
            is ContentUiStates.Loading -> {
                LinearProgressIndicator(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                )
            }
        }

        EditDishView(
            isEnabled = uiState is ContentUiStates.Show,
            eventHandler = { contentUiEvents: ContentUiEvents ->
                contentViewModel.setUiEvent(contentUiEvents)
            },
            dishData = dishData,
            onChooseNewImage = {
                pickPictureLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            innerPadding = innerPadding,
            onEditNewImage = { bitmap ->
                contentViewModel.setUiEvent(ContentUiEvents.CreateUpdatedImage(bitmap))
            },
        )
    }
}