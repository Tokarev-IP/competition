package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.LoadingStateView

@Composable
internal fun CreateMenuView(
    modifier: Modifier = Modifier,
    eventHandler: (ContentUiEvents) -> Unit,
    uiState: ContentUiStates,
    innerPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is ContentUiStates.Loading -> {
                LoadingStateView(innerPadding = innerPadding)
            }

            is ContentUiStates.Show -> {
                Text(text = "There is no any menu. Please, push the button to create one")
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = {
                        eventHandler(ContentUiEvents.CreateMenu)
                    }) {
                    Text(text = "Create a menu")
                }
            }

            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding){
                    eventHandler(ContentUiEvents.CheckMenuId)
                }
            }
        }
    }
}