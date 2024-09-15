package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.LoadingStateView

@Composable
internal fun CheckIdView(
    modifier: Modifier = Modifier,
    uiState: ContentUiStates,
    innerPadding: PaddingValues = PaddingValues(),
    onEventHandler: (ContentUiEvents) -> Unit,
) {
    when (uiState) {
        is ContentUiStates.Loading -> {
            LoadingStateView(innerPadding = innerPadding)
        }

        is ContentUiStates.Show -> {
            ErrorStateView(innerPadding = innerPadding) {
                onEventHandler(ContentUiEvents.CheckMenuId)
            }
        }

        is ContentUiStates.Error -> {
            ErrorStateView(innerPadding = innerPadding) {
                onEventHandler(ContentUiEvents.CheckMenuId)
            }
        }
    }
}