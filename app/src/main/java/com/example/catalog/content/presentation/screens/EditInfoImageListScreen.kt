package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.EditInfoImageListView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditInfoImageListScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
){
    val infoImageList by contentViewModel.getInfoImageDataFlow().collectAsState()

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let { notNullUri ->
                contentViewModel.setUiEvent(ContentUiEvents.SetUpdatedDishImage(notNullUri))
            } ?: run { snackBarMsg = "No image was selected" }
        }

    val carouselState = CarouselState(
        currentItem = 0,
        currentItemOffsetFraction = 0f,
        itemCount = { imageModel.size }
    )

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        EditInfoImageListView(
            innerPadding = innerPadding,
            infoImageList = infoImageList,
        )
    }

}