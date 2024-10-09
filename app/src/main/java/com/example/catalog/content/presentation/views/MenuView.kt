package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DishListAndSectionViewData
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.presentation.common.DishItem

@Composable
internal fun MenuView(
    modifier: Modifier = Modifier,
    dishListAndSectionViewDataList: List<DishListAndSectionViewData>,
    menuInfoData: MenuInfoData,
    infoImageList: List<InfoImageData>,
    innerPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            MenuInfoView(menuInfoData = menuInfoData)
            Spacer(modifier = modifier.height(24.dp))

            if (infoImageList.isNotEmpty())
                MenuImagesView(infoImageList = infoImageList)

            Spacer(modifier = modifier.height(24.dp))
        }

        items(dishListAndSectionViewDataList.size) { index ->
            SectionAndDishListView(
                sectionData = dishListAndSectionViewDataList[index].sectionData,
                dishList = dishListAndSectionViewDataList[index].dishDataList,
            )
        }
    }
}

@Composable
private fun MenuInfoView(
    modifier: Modifier = Modifier,
    menuInfoData: MenuInfoData,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            menuInfoData.imageModel?.let {
                AsyncImage(
                    modifier = modifier
                        .width(240.dp)
                        .height(240.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop,
                    model = menuInfoData.imageModel,
                    contentDescription = "Menu image",
                )
            }
            Text(text = menuInfoData.name)
        }
        Spacer(modifier = modifier.height(24.dp))
        Text(text = menuInfoData.description)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuImagesView(
    infoImageList: List<InfoImageData>,
) {
    val carouselState = CarouselState(
        currentItem = 0,
        currentItemOffsetFraction = 0f,
        itemCount = { infoImageList.size }
    )

    HorizontalMultiBrowseCarousel(
        state = carouselState,
        preferredItemWidth = 240.dp,
    ) {
        AsyncImage(
            model = infoImageList[it].imageModel,
            contentDescription = "Info image",
        )
    }
}

@Composable
private fun SectionAndDishListView(
    modifier: Modifier = Modifier,
    sectionData: SectionData,
    dishList: List<DishData>,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = sectionData.name,
            fontSize = 20.sp,
        )
        Spacer(modifier = modifier.height(24.dp))
        for (dish in dishList) {
            DishItem(dishData = dish)
        }
    }
}