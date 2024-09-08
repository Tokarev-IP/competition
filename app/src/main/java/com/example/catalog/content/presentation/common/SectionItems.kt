package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catalog.content.domain.data.SectionDataFirebase

@Composable
internal fun SectionItem(
    modifier: Modifier = Modifier,
    sectionData: SectionDataFirebase,
    padding: Dp = 16.dp,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick() },
        enabled = isEnabled,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = sectionData.name,
                fontSize = 20.sp,
            )
        }
    }
}

@Composable
internal fun EditSectionItem(
    modifier: Modifier = Modifier,
    sectionData: SectionDataFirebase,
    onClick: () -> Unit = {},
    onEdit: () -> Unit = {},
    isEnabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        SectionItem(
            sectionData = sectionData,
            padding = 24.dp,
            isEnabled = isEnabled,
        ) { onClick() }

        IconButton(
            onClick = { onEdit() },
            modifier = modifier.align(Alignment.TopEnd),
            enabled = isEnabled,
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit section item")
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EditSectionItemPreview() {
    EditSectionItem(
        sectionData = SectionDataFirebase(
            id = "1234567890",
            name = "Main dishes"
        )
    )
}