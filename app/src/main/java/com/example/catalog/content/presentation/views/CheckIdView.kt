package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Box
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
import com.example.catalog.content.presentation.ContentUiStates

@Composable
internal fun CheckIdView(
    modifier: Modifier = Modifier,
    uiState: ContentUiStates,
    innerPadding: PaddingValues,
    onRefresh:() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize().padding(innerPadding),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            is ContentUiStates.Loading -> {
                CircularProgressIndicator()
            }

            is ContentUiStates.Show -> {}
            is ContentUiStates.Error -> {
                Text(text = "Please, try again")
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = {
                        onRefresh()
                    }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    Text(text = "Try again")
                }
            }
        }
    }
}