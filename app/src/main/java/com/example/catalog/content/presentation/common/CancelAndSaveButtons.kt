package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.ContentUiStates

@Composable
internal fun CancelAndSaveButtons(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    uiState: ContentUiStates,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier.align(Alignment.Center)
        ) {
            OutlinedButton(
                enabled = (uiState is ContentUiStates.Show),
                onClick = { onCancel() }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = modifier.width(48.dp))
            Button(
                enabled = (uiState is ContentUiStates.Show),
                onClick = { onSave() }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
internal fun CancelAndChooseButtons(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onChoose: () -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier.align(Alignment.Center)
        ) {
            OutlinedButton(
                onClick = { onCancel() }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = modifier.width(48.dp))
            Button(
                onClick = { onChoose() }) {
                Text(text = "Choose")
            }
        }
    }
}