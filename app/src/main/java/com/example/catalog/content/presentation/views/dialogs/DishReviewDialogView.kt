package com.example.catalog.content.presentation.views.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.catalog.content.domain.data.DishData

@Composable
internal fun DishReviewDialogView(
    modifier: Modifier = Modifier,
    dishData: DishData,
    onAccepted: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                modifier = modifier
                    .width(240.dp)
                    .height(240.dp)
                    .clip(RoundedCornerShape(24.dp)),
                model = dishData.imageModel ?: dishData.updatedImageModel,
                contentDescription = "Image of the dish",
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(text = dishData.name, fontSize = 20.sp)
            Spacer(modifier = modifier.height(16.dp))
            Text(text = dishData.price.toString()+" $", fontSize = 18.sp)
            Spacer(modifier = modifier.height(16.dp))
            Text(text = dishData.description)
            Spacer(modifier = modifier.height(16.dp))

            HorizontalDivider()
            Spacer(modifier = modifier.height(16.dp))

            Box(modifier = modifier.fillMaxWidth()) {
                Button(
                    modifier = modifier.align(Alignment.TopEnd),
                    onClick = { onAccepted() }
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}