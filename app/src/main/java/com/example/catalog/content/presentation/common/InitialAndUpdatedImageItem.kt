package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun InitialAndUpdatedImageItem(
    modifier: Modifier = Modifier,
    initialImageModel: Any?,
    updatedImageModel: Any?,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Initial image")
            Spacer(modifier = modifier.height(16.dp))
            AsyncImage(
                modifier = modifier
                    .clip(RoundedCornerShape(24.dp))
                    .width(140.dp)
                    .height(140.dp),
                model = initialImageModel,
                contentDescription = "Initial image",
                contentScale = ContentScale.Crop,
            )
        }
        if (updatedImageModel != null)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Updated image")
                Spacer(modifier = modifier.height(16.dp))
                AsyncImage(
                    modifier = modifier
                        .clip(RoundedCornerShape(24.dp))
                        .width(140.dp)
                        .height(140.dp),
                    model = updatedImageModel,
                    contentDescription = "Updated image",
                    contentScale = ContentScale.Crop,
                )
            }
    }
}

@Composable
@Preview(showBackground = true)
private fun InitialAndUpdatedImageItemPreview(){
    InitialAndUpdatedImageItem(
        initialImageModel = "",
        updatedImageModel = "",
    )
}