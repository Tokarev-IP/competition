package com.example.catalog.content.presentation.used

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.common.ChooseImageItem

@Composable
fun ImageSegmentationScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val bitmapList by contentViewModel.getImageListFlow().collectAsState()

    var imageUri: Uri? by rememberSaveable { mutableStateOf(null) }

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
            }
        }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
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

//            ChooseImageItem(
//                height = 240.dp,
//                width = 240.dp,
//                corner = 24.dp,
//                uri = imageUri,
//                enabled = uiState is ContentUiStates.Show,
//                onChoosePicture = {
//                    pickPictureLauncher.launch(
//                        PickVisualMediaRequest(
//                            ActivityResultContracts.PickVisualMedia.ImageOnly
//                        )
//                    )
//                },
//                onClearPicture = {
//                    imageUri = null
//                }
//            )
            Spacer(modifier = modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    imageUri?.let { uri ->
                        contentViewModel.setUiEvent(ContentUiEvents.SegmentImage(uri))
                    }
                }
            ) {
                Text(text = "Segment image")
            }

        }

        items(bitmapList.size) { index ->
            AsyncImage(
                modifier = modifier,
                model = bitmapList[index],
                contentDescription = "Image bitmap",
                contentScale = ContentScale.Fit,
            )
        }
    }
}