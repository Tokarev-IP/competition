package com.example.catalog.content.presentation.views

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.CancelAndAcceptButtons
import com.example.catalog.content.presentation.common.InitialAndUpdatedImageItem
import com.example.catalog.content.presentation.common.SwitchItem

@Composable
fun UpdateDishImageView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    imageUri: Uri,
    updatedImageBitmap: Bitmap?,
    onImageUpdate: (imageUri: Uri, color: Color?) -> Unit,
    uiState: ContentUiStates,
    color: Color?,
    onChooseColor: () -> Unit,
    onCancelled: () -> Unit,
    onUseUpdatedImage: (imageBitmap: Bitmap) -> Unit,
    onUseInitialImage: () -> Unit,
) {
    var isColorChosen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (uiState is ContentUiStates.Loading)
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth(),
            )

        Spacer(modifier = modifier.height(16.dp))

        InitialAndUpdatedImageItem(
            initialImageModel = imageUri,
            updatedImageModel = updatedImageBitmap
        )

        Spacer(modifier = modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                onImageUpdate(imageUri, color)
            },
            enabled = uiState is ContentUiStates.Show
        ) {
            Text(text = "Make an updated image")
        }

        if (updatedImageBitmap != null)
            OutlinedButton(
                onClick = {
                    onUseUpdatedImage(updatedImageBitmap)
                },
                enabled = uiState is ContentUiStates.Show
            ) {
                Text(text = "Use updated image")
            }

        Spacer(modifier = modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = modifier.height(8.dp))

//        SwitchItem(
//            isSwitched = isColorChosen,
//            onCheckedChange = { result: Boolean ->
//                isColorChosen = result
//            },
//            enabled = uiState is ContentUiStates.Show,
//            text = "Background color",
//        )
//        Spacer(modifier = modifier.height(24.dp))

//        if (isColorChosen)
//            Row(
//                modifier = modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceAround,
//            ) {
//                OutlinedButton(
//                    onClick = {
//                        onChooseColor()
//                    },
//                    enabled = uiState is ContentUiStates.Show,
//                ) {
//                    Text(text = "Choose a color")
//                }
//
//                color?.let { boxColor ->
//                    Box(
//                        modifier = modifier
//                            .width(60.dp)
//                            .height(60.dp)
//                            .background(color = boxColor, shape = CircleShape),
//                    )
//                }
//            }

        Spacer(modifier = modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = modifier.height(36.dp))

        CancelAndAcceptButtons(
            onCancel = { onCancelled() },
            onAccept = { onUseInitialImage() },
            isEnable = (uiState is ContentUiStates.Show),
            cancelText = "Cancel",
            acceptText = "Use initial image",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateDishImageViewPreview() {
    UpdateDishImageView(
        imageUri = Uri.EMPTY,
        innerPadding = PaddingValues(),
        updatedImageBitmap = null,
        onImageUpdate = { a, b -> },
        uiState = ContentUiStates.Show,
        color = Color.DarkGray,
        onChooseColor = {},
        onCancelled = {},
        onUseUpdatedImage = {},
        onUseInitialImage = {},
    )
}