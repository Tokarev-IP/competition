package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.ParametersForGeneratingAiDishName
import com.example.catalog.content.presentation.views.EditDishView
import com.example.catalog.content.presentation.views.GenerateDishNameView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditDishScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishData by contentViewModel.getDishItemDataFlow().collectAsState()

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var imageUri: Uri? by rememberSaveable { mutableStateOf(null) }

    val radioOptionsList: List<ParametersForGeneratingAiDishName> = listOf(
        ParametersForGeneratingAiDishName.Taste,
        ParametersForGeneratingAiDishName.Mood,
        ParametersForGeneratingAiDishName.Season,
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (openDialog)
            BasicAlertDialog(
                modifier = modifier.fillMaxWidth(),
                onDismissRequest = { openDialog = false }
            ) {
                Surface(
                    modifier = modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    GenerateDishNameView(
                        onCancel = { openDialog = false },
                        onChoose = { parametersList: List<String?> ->
                            imageUri?.let { uri ->
                                contentViewModel.setUiEvent(
                                    ContentUiEvents.GenerateTextUsingGemini(
                                        uri = uri,
                                        parametersList
                                    )
                                )
                            }
                            openDialog = false
                        },
                        radioOptionsList = radioOptionsList,
                    )
                }
            }

        EditDishView(
            uiState = uiState,
            eventHandler = { contentUiEvents: ContentUiEvents ->
                contentViewModel.setUiEvent(contentUiEvents)
            },
            dishData = dishData,
            onOpenDishNameDialog = { uri ->
                imageUri = uri
                openDialog = true
            }
        )
    }
}