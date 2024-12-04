package com.example.catalog.content.presentation.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.CreateMenuView

@Composable
internal fun CreateMenuScreen(
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    Scaffold { innerPadding ->
        CreateMenuView(
            eventHandler = { contentUiEvents: ContentUiEvents ->
                contentViewModel.setUiEvent(contentUiEvents)
            },
            uiState = uiState,
            innerPadding = innerPadding,
        )
    }
}