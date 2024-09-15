package com.example.catalog.content.presentation.views

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.common.CancelAndAcceptButtons
import com.example.catalog.content.presentation.common.ChooseImageItem

@Composable
internal fun EditMenuInfoView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    menuInfoData: MenuInfoData,
    onEventHandler: (ContentUiEvents) -> Unit,
    isEnabled: Boolean = true,
    onChooseImage: () -> Unit,
) {
    var nameText by remember { mutableStateOf(menuInfoData.name) }
    var descriptionText by remember { mutableStateOf(menuInfoData.description) }
    var imageModel: Uri? by remember { mutableStateOf(menuInfoData.imageModel) }
    var updatedImageModel: Bitmap? by remember { mutableStateOf(menuInfoData.updatedImageModel) }

    menuInfoData.apply {
        updatedImageModel = menuInfoData.updatedImageModel
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            ChooseImageItem(
                height = 240.dp,
                width = 240.dp,
                corner = 24.dp,
                uri = imageModel ?: updatedImageModel,
                enabled = isEnabled,
                onChoosePicture = { onChooseImage() },
                onClearPicture = {
                    imageModel = null
                    updatedImageModel = null
                },
            )
            Spacer(modifier = modifier.height(24.dp))

            OutlinedTextField(
                modifier = modifier
                    .padding(horizontal = 12.dp),
                value = nameText,
                onValueChange = { text: String ->
                    if (text.length <= 60)
                        nameText = text
                },
                label = { Text(text = "Name") },
                enabled = isEnabled,
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (nameText.isNotEmpty())
                        IconButton(onClick = { nameText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
            )
            Spacer(modifier = modifier.height(24.dp))

            OutlinedTextField(
                modifier = modifier
                    .padding(horizontal = 12.dp),
                value = descriptionText,
                onValueChange = { text: String ->
                    if (text.length <= 60)
                        nameText = text
                },
                label = { Text(text = "Name") },
                enabled = isEnabled,
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (descriptionText.isNotEmpty())
                        IconButton(onClick = { descriptionText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
            )
            Spacer(modifier = modifier.height(24.dp))

            CancelAndAcceptButtons(
                cancelText = "Cancel",
                acceptText = "Save",
                isEnable = isEnabled,
                onCancel = { onEventHandler(ContentUiEvents.GoBack) },
                onAccept = {
                    onEventHandler(
                        ContentUiEvents.SaveMenuInfo(
                            MenuInfoData(
                                id = menuInfoData.id,
                                name = nameText.trim(),
                                description = descriptionText.trim(),
                                imageModel = imageModel,
                                updatedImageModel = updatedImageModel,
                            )
                        )
                    )
                },
                isAcceptButtonEnabled = nameText.isNotEmpty() && descriptionText.isNotEmpty(),
            )
        }
    }
}