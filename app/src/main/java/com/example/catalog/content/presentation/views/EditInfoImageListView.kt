package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.presentation.ContentUiEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditInfoImageListView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    infoImageList: List<InfoImageData>,
    onEventHandler: (ContentUiEvents) -> Unit,
) {
    val carouselState = CarouselState(
        currentItem = 0,
        currentItemOffsetFraction = 0f,
        itemCount = { infoImageList.size }
    )
    HorizontalMultiBrowseCarousel(
        state = carouselState,
        preferredItemWidth = 64.dp
    ) {
        AsyncImage(model = infoImageList[it].imageModel, contentDescription = "Image")
    }

    OutlinedButton(
        onClick = { onEventHandler(ContentUiEvents.SaveInfoImage) }
    ) {

    }
}