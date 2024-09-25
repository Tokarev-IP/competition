package com.example.catalog.content.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.GoBackNavigationButton
import com.example.catalog.content.presentation.common.LoadingStateView
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.MenuView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val menuDishData by contentViewModel.getMenuViewDataFlow().collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Menu") },
                navigationIcon = {
                    GoBackNavigationButton {
                        contentViewModel.setUiEvent(ContentUiEvents.GoBack)
                    }
                }
            )
        },
        snackbarHost = {

        }
    ) { innerPadding ->
        when (uiState) {
            is ContentUiStates.Show -> {
                MenuView(
                    dishListAndSectionViewDataList = menuDishData.dishListAndSectionViewDataList,
                    menuInfoData = menuDishData.menuInfoData,
                    infoImageList = menuDishData.infoImageList,
                    innerPadding = innerPadding,
                )
            }
            is ContentUiStates.Loading -> {
                LoadingStateView(innerPadding = innerPadding)
            }
            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding){
                    //todo
                }
            }
        }

    }

}