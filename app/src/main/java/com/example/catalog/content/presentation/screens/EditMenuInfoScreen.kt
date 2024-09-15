package com.example.catalog.content.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.EditMenuInfoView

@Composable
internal fun EditMenuInfoScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
){
    val menuInfoData by contentViewModel.getMenuInfoDataFlow().collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        EditMenuInfoView(
            innerPadding = innerPadding
        )
    }
}