package com.example.catalog.content.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.EditSectionView

@Composable
internal fun EditSectionScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    name: String = "",
    id: String = "",
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarMsg by remember { mutableStateOf<String?>(null) }

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
    ) { innerPadding ->

        if (uiState is ContentUiStates.Loading)
            LinearProgressIndicator(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            )

        EditSectionView(
            name = name,
            id = id,
            isEnabled = uiState is ContentUiStates.Show,
            innerPadding = innerPadding,
            onAccept = { name, id ->

            },
            onCancel = {},

            )

    }
}