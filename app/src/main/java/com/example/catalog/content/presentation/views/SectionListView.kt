package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.common.EditSectionItem

@Composable
internal fun SectionListView(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    sectionList: List<SectionData>,
    innerPadding: PaddingValues = PaddingValues(),
    onEventHandler: (ContentUiEvents) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(sectionList.size) {
            Spacer(modifier = modifier.height(16.dp))

            EditSectionItem(
                sectionData = sectionList[it],
                onClick = {
                    onEventHandler(ContentUiEvents.ShowDishListOfSection(sectionList[it]))
                },
                onEdit = {
                    onEventHandler(ContentUiEvents.EditSectionItem(sectionList[it]))
                },
            )
        }
        item {
            if (sectionList.isEmpty()) {
                Spacer(modifier = modifier.height(64.dp))
                Text(text = "No sections. Add a new section.")
            }
            Spacer(modifier = modifier.height(24.dp))
            OutlinedButton(
                onClick = {
                    onEventHandler(ContentUiEvents.CreateSectionItem)
                },
                enabled = isEnabled,
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Add a new section")
                Text(text = "Add a new section")
            }
        }
    }
}