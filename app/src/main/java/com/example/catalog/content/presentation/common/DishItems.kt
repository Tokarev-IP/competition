package com.example.catalog.content.presentation.common

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.catalog.content.domain.data.DishData

@Composable
internal fun DishItem(
    modifier: Modifier = Modifier,
    dishData: DishData,
    onCardClick: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { onCardClick() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = modifier.weight(1f),
            ) {
                Text(
                    text = dishData.name,
                    fontSize = 16.sp,
                )
                Spacer(modifier = modifier.height(12.dp))
                Row {
                    Text(
                        text = dishData.price.toString(),
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = modifier.width(4.dp))
                    Text(text = "$")
                }
            }
            Spacer(modifier = modifier.width(8.dp))
            AsyncImage(
                modifier = modifier
                    .height(100.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = dishData.imageModel ?: dishData.updatedImageModel,
                contentScale = ContentScale.FillHeight,
                contentDescription = "Image of the dish",
            )
        }
    }
}

@Composable
internal fun EditDishItem(
    modifier: Modifier = Modifier,
    dishData: DishData,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCardClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        DishItem(
            dishData = dishData,
            onCardClick = { onCardClick() }
        )

        IconButton(
            modifier = modifier.align(Alignment.TopEnd),
            onClick = { onEdit() },
            enabled = isEnabled,
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit information of the dish")
        }

        IconButton(
            modifier = modifier.align(Alignment.BottomEnd),
            onClick = { onDelete() },
            enabled = isEnabled,
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete the dish")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditDishItemPreview() {
    val bitmap = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.RED)

    EditDishItem(
        dishData = DishData(
            name = "Veg soup aksks akak akak hjh jhjhjhj tfgh loiuu fdxsz xdxdx  xdxd fcfc  gcgv hgfyt ddv hghjkg ffyuhj uggyugg hgguigy sldlf sasjd dfkdfk kdkdfkdfdf",
            price = 14.80,
            weight = 350.0,
            updatedImageModel = bitmap,
        ),
        onEdit = {},
        onDelete = {},
        onCardClick = {}
    )
}