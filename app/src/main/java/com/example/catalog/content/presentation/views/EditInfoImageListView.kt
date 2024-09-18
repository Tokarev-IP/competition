package com.example.catalog.content.presentation.views

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.catalog.content.domain.data.InfoImageData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditInfoImageListView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    infoImageList: List<InfoImageData>,
    onAddNewImage: () -> Unit,
    onDeleteImage: (imageId: String) -> Unit,
    onClickImage: (imageUri: Uri) -> Unit,
) {
    val carouselState = CarouselState(
        currentItem = 0,
        currentItemOffsetFraction = 0f,
        itemCount = { infoImageList.size }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = modifier.height(32.dp))
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            preferredItemWidth = 240.dp,
        ) {
            if (infoImageList.isNotEmpty()) {
                Box(modifier = modifier.height(240.dp)) {
                    AsyncImage(
                        modifier = modifier.clickable {
                            onClickImage(infoImageList[it].imageModel)
                        },
                        model = infoImageList[it].imageModel,
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                    )
                    IconButton(
                        onClick = { onDeleteImage(infoImageList[it].id) },
                        modifier = modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Delete image")
                    }
                }
            } else {
                Text(text = "No images yet. Add one!")
            }
        }
        Spacer(modifier = modifier.height(32.dp))
        OutlinedButton(
            onClick = { onAddNewImage() }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add a new image")
            Text(text = "Add a new image")
        }
        Text(text = "Images count: ${infoImageList.size}")
    }
}