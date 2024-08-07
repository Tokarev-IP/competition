package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.views.EditDishView

@Composable
internal fun EditDishScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishData by contentViewModel.getDishItemDataFlow().collectAsState()

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                contentViewModel.setUiEvent(ContentUiEvents.SetInitialImage(uri))
            }
        }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        EditDishView(
            uiState = uiState,
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