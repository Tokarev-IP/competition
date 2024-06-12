package com.example.catalog.content.presentation.views

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.CancelAndSaveButtons
import com.example.catalog.content.presentation.common.ChooseImage

@Composable
internal fun EditDishView(
    modifier: Modifier = Modifier,
    uiState: ContentUiStates,
    eventHandler: (ContentUiEvents) -> Unit,
    dishData: DishData,
    onOpenDishNameDialog: (uri: Uri) -> Unit,
) {
    var imageUriString by rememberSaveable { mutableStateOf(dishData.image) }
    var dishNameText by rememberSaveable { mutableStateOf(dishData.name) }
    var dishDescriptionText by rememberSaveable { mutableStateOf(dishData.description) }
    var dishPriceText by rememberSaveable { mutableStateOf(dishData.price.toString()) }
    var imageUri: Uri? by rememberSaveable { mutableStateOf(null) }

    dishData.apply {
        dishNameText = dishData.name
        dishDescriptionText = dishData.description
    }

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                imageUriString = null
            }
        }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            when (uiState) {
                is ContentUiStates.Loading -> {
                    Box(
                        modifier = modifier.fillMaxSize(),
                    ) {
                        LinearProgressIndicator(
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(24.dp))

            ChooseImage(
                height = 240.dp,
                width = 240.dp,
                corner = 24.dp,
                uri = if (imageUriString != null) Uri.parse(imageUriString) else imageUri,
                uiState = uiState,
                onChoosePicture = {
                    pickPictureLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onClearPicture = {
                    imageUri = null
                    imageUriString = null
                }
            )
            Spacer(modifier = modifier.height(24.dp))

            imageUri?.let { uri ->
                OutlinedButton(
                    onClick = {
                        Log.d("DAVAI", "image uri $uri")
                        onOpenDishNameDialog(uri)
//                        eventHandler(ContentUiEvents.GenerateTextUsingGemini(uri))
                    }
                ) {
                    Text(text = "Generate text using AI")
                }
            }
            Spacer(modifier = modifier.height(24.dp))

            OutlinedTextField(
                value = dishNameText,
                onValueChange = { text: String ->
                    dishNameText = text
                },
                label = { Text(text = "Name") },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (dishNameText.isNotEmpty())
                        IconButton(onClick = { dishNameText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
            )
            Spacer(modifier = modifier.height(24.dp))

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
            Spacer(modifier = modifier.height(24.dp))

            OutlinedTextField(
                value = dishDescriptionText,
                onValueChange = { text: String ->
                    dishDescriptionText = text
                },
                label = { Text(text = "Name") },
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

            CancelAndSaveButtons(
                onCancel = {
                    eventHandler(ContentUiEvents.GoBack)
                },
                onSave = {
                    eventHandler(
                        ContentUiEvents.SaveDishItem(
                            dishData = DishData(
                                id = dishData.id,
                                name = dishNameText,
                                price = dishPriceText.toDouble(),
                                description = dishDescriptionText,
                                image = dishData.image
                            ),
                            imageUri = imageUri,
                        )
                    )
                },
                uiState = uiState,
            )
        }
    }
}