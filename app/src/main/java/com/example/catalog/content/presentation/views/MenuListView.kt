package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.common.EditDishItem

@Composable
internal fun MenuListView(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    dishList: List<DishData>,
    eventHandler: (ContentUiEvents) -> Unit,
    innerPadding: PaddingValues,
    onCardClick: (DishData) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(dishList.size) {
            Spacer(modifier = Modifier.height(8.dp))
            EditDishItem(
                dishData = dishList[it],
                onEdit = {
                    eventHandler(
                        ContentUiEvents.EditDishItem(dishData = dishList[it])
                    )
                },
                onDelete = {
                    eventHandler(
                        ContentUiEvents.DeleteDish(dishData = dishList[it])
                    )
                },
                onCardClick = { onCardClick(dishList[it]) },
                isEnabled = isEnabled,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            if (dishList.isEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "No dishes yet. Add one!")
                Spacer(modifier = Modifier.height(32.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    eventHandler(ContentUiEvents.CreateDishItem)
                },
                enabled = isEnabled,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add a new dish")
                Text(text = "Add")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}