package com.example.catalog.content.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.LoadingStateView
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.CheckIdView

@Composable
internal fun CheckIdScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
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