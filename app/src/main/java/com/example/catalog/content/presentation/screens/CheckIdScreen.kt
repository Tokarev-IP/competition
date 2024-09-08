package com.example.catalog.content.presentation.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.CheckIdView

@Composable
internal fun CheckIdScreen(
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    Scaffold { innerPadding ->
        CheckIdView(
            uiState = uiState,
            innerPadding = innerPadding,
        ) {
            contentViewModel.setUiEvent(ContentUiEvents.CheckMenuId)
        }
    }

    LaunchedEffect(key1 = Unit) {
        contentViewModel.setUiEvent(ContentUiEvents.CheckMenuId)
    }
}