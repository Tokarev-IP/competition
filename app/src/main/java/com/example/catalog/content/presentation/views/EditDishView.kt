package com.example.catalog.content.presentation.views

import android.graphics.Bitmap
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.CancelAndAcceptButtons
import com.example.catalog.content.presentation.common.ChooseImageItem

@Composable
internal fun EditDishView(
    modifier: Modifier = Modifier,
    uiState: ContentUiStates,
    eventHandler: (ContentUiEvents) -> Unit,
    dishData: DishData,
    innerPadding: PaddingValues,
    onChooseNewImage: () -> Unit,
    onEditNewImage: (bitmap: Bitmap) -> Unit,
) {
    var imageModel by remember { mutableStateOf(dishData.imageModel) }
    var dishNameText by remember { mutableStateOf(dishData.name) }
    var dishDescriptionText by remember { mutableStateOf(dishData.description) }
    var dishPriceText by remember { mutableStateOf(dishData.price.toString()) }
    var dishWeightText by remember { mutableStateOf(dishData.weight.toString()) }
    var updatedImageModel: Bitmap? by remember { mutableStateOf(dishData.updatedImageModel) }

    dishData.apply {
        dishNameText = dishData.name
        dishDescriptionText = dishData.description
        updatedImageModel = dishData.updatedImageModel
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            when (uiState) {
                is ContentUiStates.Loading -> {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = modifier.height(24.dp))

            ChooseImageItem(
                height = 240.dp,
                width = 240.dp,
                corner = 24.dp,
                uri = imageModel ?: updatedImageModel,
                enabled = uiState is ContentUiStates.Show,
                editButtonEnabled = updatedImageModel != null,
                onChoosePicture = { onChooseNewImage() },
                onClearPicture = {
                    imageModel = null
                    updatedImageModel = null
                },
                onEditPicture = {
                    eventHandler(ContentUiEvents.SetNamePriceWeightDescription(
                        name = dishNameText,
                        price = dishPriceText,
                        weight = dishWeightText,
                        description = dishDescriptionText,
                    ))
                    updatedImageModel?.let { bitmap ->
                        onEditNewImage(bitmap)
                    }
                },
            )
            Spacer(modifier = modifier.height(24.dp))

            OutlinedTextField(
                value = dishNameText,
                onValueChange = { text: String ->
                    dishNameText = text
                },
                label = { Text(text = "Name") },
                enabled = (uiState is ContentUiStates.Show),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (dishNameText.isNotEmpty())
                        IconButton(onClick = { dishNameText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
            )

            Spacer(modifier = modifier.height(16.dp))

            OutlinedTextField(
                value = dishPriceText,
                onValueChange = { text: String ->
                    dishPriceText = text
                },
                label = { Text(text = "Price") },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (dishPriceText.isNotEmpty())
                        IconButton(onClick = { dishPriceText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
                enabled = (uiState is ContentUiStates.Show)
            )

            if (dishNameText.isNotEmpty()) {
                updatedImageModel?.let { image ->
                    Spacer(modifier = modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            eventHandler(ContentUiEvents.SetNamePriceWeightDescription(
                                name = dishNameText,
                                price = dishPriceText,
                                weight = dishWeightText,
                                description = dishDescriptionText,
                            ))
                            eventHandler(
                                ContentUiEvents.GenerateDescriptionOfDish(
                                    imageBitmap = image,
                                    dishName = dishNameText,
                                )
                            )
                        },
                        enabled = (uiState is ContentUiStates.Show)
                    ) {
                        Text(text = "Generate AI description")
                    }
                }
            }

            Spacer(modifier = modifier.height(16.dp))

            OutlinedTextField(
                value = dishDescriptionText,
                onValueChange = { text: String ->
                    dishDescriptionText = text
                },
                label = { Text(text = "Description") },
                enabled = (uiState is ContentUiStates.Show),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (dishDescriptionText.isNotEmpty())
                        IconButton(onClick = { dishDescriptionText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
            )

            Spacer(modifier = modifier.height(24.dp))

            CancelAndAcceptButtons(
                onCancel = {
                    eventHandler(ContentUiEvents.GoBack)
                },
                onAccept = {
                    eventHandler(
                        ContentUiEvents.SaveDishItem(
                            DishData(
                                id = dishData.id,
                                name = dishNameText,
                                description = dishDescriptionText,
                                price = dishPriceText.toDouble(),
                                weight = dishWeightText.toDouble(),
                                imageModel = imageModel,
                                updatedImageModel = updatedImageModel,
                            )
                        )
                    )
                },
                isEnable = (uiState is ContentUiStates.Show),
                cancelText = "Cancel",
                acceptText = "Save",
            )
        }
    }
}