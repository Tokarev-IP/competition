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

@Composable
internal fun CancelAndAcceptButtons(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onAccept: () -> Unit,
    isEnable: Boolean = true,
    cancelText: String,
    acceptText: String,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier.align(Alignment.Center)
        ) {
            OutlinedButton(
                enabled = isEnable,
                onClick = { onCancel() }) {
                Text(text = cancelText)
            }
            Spacer(modifier = modifier.width(48.dp))
            Button(
                enabled = isEnable,
                onClick = { onAccept() }) {
                Text(text = acceptText)
            }
        }
    }
}