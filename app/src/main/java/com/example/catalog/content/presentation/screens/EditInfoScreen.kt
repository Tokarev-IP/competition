package com.example.catalog.content.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.views.EditInfoView

@Composable
internal fun EditInfoScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
){
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    EditInfoView()
}