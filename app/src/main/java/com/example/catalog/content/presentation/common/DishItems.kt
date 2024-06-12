package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.catalog.content.domain.data.DishData

@Composable
internal fun DishItem(
    modifier: Modifier = Modifier,
    dishData: DishData,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            //todo
        }
    ) {
        Column(
            modifier = modifier.align(Alignment.Start),
        ) {
            Text(text = dishData.name)
            Spacer(modifier = modifier.height(12.dp))
            Text(text = dishData.price.toString())
            Spacer(modifier = modifier.height(12.dp))
            Text(text = dishData.description)
        }

        dishData.image?.let { image: String ->
            AsyncImage(
                modifier = modifier
                    .height(100.dp)
                    .width(100.dp)
                    .align(Alignment.End),
                model = image,
                contentScale = ContentScale.Fit,
                contentDescription = "Image of the dish",
            )
        }
    }
}

@Composable
internal fun DishItemEdit(
    modifier: Modifier = Modifier,
    dishData: DishData,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        DishItem(dishData = dishData)

        IconButton(
            modifier = modifier.align(Alignment.TopEnd),
            onClick = { onClick() }
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit information of the dish")
        }
    }
}