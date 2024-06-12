package com.example.catalog.content.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.views.MenuListView

@Composable
internal fun MenuListScreen(
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishList by contentViewModel.getDishListFlow().collectAsState()

    LaunchedEffect(key1 = Unit) {
        contentViewModel.setUiEvent(ContentUiEvents.DownloadMenuList)
    }

    MenuListView(
        uiState = uiState,
        dishList = dishList,
        eventHandler = { contentUiEvents: ContentUiEvents ->
            contentViewModel.setUiEvent(contentUiEvents)
        }
    )
}