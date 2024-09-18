package com.example.catalog.content.presentation.views.dialogs

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun ImageDialogView(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    onConfirm: () -> Unit,
    roundCornerShape: RoundedCornerShape = RoundedCornerShape(16.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Info image",
            modifier = modifier.clip(roundCornerShape)
        )
        Spacer(modifier = modifier.height(24.dp))
        Button(onClick = { onConfirm() }) {
            Text(text = "Ok")
        }
    }
}