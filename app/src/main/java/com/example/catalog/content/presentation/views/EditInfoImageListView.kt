package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
) {
    val carouselState = CarouselState(
        currentItem = 0,
        currentItemOffsetFraction = 0f,
        itemCount = { infoImageList.size }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            preferredItemWidth = 160.dp,
        ) {
            AsyncImage(model = infoImageList[it].imageModel, contentDescription = "Image")
        }

        OutlinedButton(
            onClick = { onAddNewImage() }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add a new image")
            Text(text = "Add a new image")
        }
    }
}