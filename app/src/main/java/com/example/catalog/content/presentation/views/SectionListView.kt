package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.SectionDataFirebase
import com.example.catalog.content.presentation.common.EditSectionItem

@Composable
internal fun SectionListView(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    sectionList: List<SectionDataFirebase>,
    innerPadding: PaddingValues,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        items(sectionList.size) {
            Spacer(modifier = modifier.height(16.dp))

            EditSectionItem(
                sectionData = sectionList[it],
                onClick = {},
                onEdit = {},
            )
        }
    }
}