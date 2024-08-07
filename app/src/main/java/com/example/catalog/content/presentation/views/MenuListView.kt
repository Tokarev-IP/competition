package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.EditDishItem

@Composable
internal fun MenuListView(
    modifier: Modifier = Modifier,
    uiState: ContentUiStates,
    dishList: List<DishData>,
    eventHandler: (ContentUiEvents) -> Unit,
    onCreateMenuDoc: () -> Unit,
    innerPadding: PaddingValues,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            when (uiState) {
                is ContentUiStates.Loading -> {
                    Box(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        LinearProgressIndicator(
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        items(dishList.size) {
            Spacer(modifier = Modifier.height(8.dp))
            EditDishItem(
                dishData = dishList[it],
                onClick = {
                    eventHandler(
                        ContentUiEvents.EditDishItem(
                            dishData = dishList[it]
                        )
                    )
                }
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
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add a new dish")
                Text(text = "Add")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (dishList.isNotEmpty()) {
                Button(onClick = {
                    onCreateMenuDoc()
                }) {
                    Text(text = "Save the menu as PDF file")
                }
            }
        }
    }
}