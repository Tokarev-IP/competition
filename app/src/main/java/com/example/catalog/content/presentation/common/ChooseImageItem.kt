package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun ChooseImageItem(
    modifier: Modifier = Modifier,
    height: Dp,
    width: Dp,
    corner: Dp,
    uri: Any?,
    enabled: Boolean,
    editButtonEnabled: Boolean,
    onChoosePicture: () -> Unit,
    onClearPicture: () -> Unit,
    onEditPicture: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (uri != null)
            Box(
                modifier = modifier
                    .height(height)
                    .width(width),
            ) {
                AsyncImage(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(40.dp)
                        .clip(RoundedCornerShape(corner))
                        .align(Alignment.Center),
                    model = uri,
                    contentDescription = "Image of the dish",
                    contentScale = ContentScale.Crop,
                )

                IconButton(
                    onClick = { onClearPicture() },
                    modifier = modifier.align(Alignment.TopEnd),
                    enabled = enabled,
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear the picture of the dish")
                }

                IconButton(
                    onClick = { onEditPicture() },
                    modifier = modifier.align(Alignment.TopStart),
                    enabled = editButtonEnabled,
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit the picture of the dish")
                }
            }
        else {
            Text(text = "Please, add an image of the dish")

            Spacer(modifier = modifier.height(12.dp))

            OutlinedButton(
                onClick = { onChoosePicture() },
                enabled = enabled,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add an image of the dish")
                Text(text = "Add an image")
            }
        }
    }
}