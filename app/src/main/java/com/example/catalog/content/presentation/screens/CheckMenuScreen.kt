package com.example.catalog.content.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.views.CheckMenuView

@Composable
internal fun CheckMenuScreen(
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    LaunchedEffect(key1 = Unit) {
        contentViewModel.setUiEvent(ContentUiEvents.CheckMenuId)
    }

    CheckMenuView(
        eventHandler = { contentUiEvents: ContentUiEvents ->
            contentViewModel.setUiEvent(contentUiEvents)
        },
        uiState = uiState,
    )
}