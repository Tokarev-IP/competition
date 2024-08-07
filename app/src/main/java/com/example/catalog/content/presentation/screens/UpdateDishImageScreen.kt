package com.example.catalog.content.presentation.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.views.ChooseColorView
import com.example.catalog.content.presentation.views.UpdateDishImageView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishImageChoiceScreen(
    contentViewModel: ContentViewModel,
    imageUriString: String,
    modifier: Modifier = Modifier,
) {
    var updatedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isColorDialogOpened by remember { mutableStateOf(false) }
    var colorState by remember { mutableStateOf<Color?>(null) }

    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    when (uiIntent) {
        is ContentUiIntents.PutUpdatedBitmapImage -> {
            updatedImageBitmap = (uiIntent as ContentUiIntents.PutUpdatedBitmapImage).bitmap
            contentViewModel.clearUiIntents()
        }
    }

    if (isColorDialogOpened)
        BasicAlertDialog(
            onDismissRequest = {
                isColorDialogOpened = false
            },

            ) {
            Surface {
                ChooseColorView(
                    onColorSelected = { color: Color ->
                        colorState = color
                        isColorDialogOpened = false
                    },
                    onCancelled = {
                        isColorDialogOpened = false
                    }
                )
            }
        }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding: PaddingValues ->

//        UpdateDishImageView(
//            imageUri = Uri.parse(imageUriString),
//            updatedImageBitmap = updatedImageBitmap,
//            onImageUpdate = { imageUri, color ->
//                contentViewModel.setUiEvent(ContentUiEvents.CreateUpdatedImage(imageUri, color))
//            },
//            uiState = uiState,
//            color = colorState,
//            onChooseColor = {
//                isColorDialogOpened = true
//            },
//            onCancelled = {},
//            onUseUpdatedImage = { bitmap ->
//                contentViewModel.setUiEvent(ContentUiEvents.SetUpdatedImage(bitmap))
//            },
//            onUseInitialImage = {
//                contentViewModel.setUiEvent(ContentUiEvents.SetInitialImage(Uri.parse(imageUriString)))
//            },
//            innerPadding = innerPadding,
//        )
    }
}