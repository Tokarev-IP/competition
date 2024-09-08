package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.common.CancelAndAcceptButtons

@Composable
internal fun EditSectionView(
    modifier: Modifier = Modifier,
    name: String = "",
    id: String = "",
    isEnabled: Boolean = true,
    onCancel: () -> Unit = {},
    onAccept: (name: String, id: String) -> Unit,
    innerPadding: PaddingValues,
) {
    var sectionName: String by rememberSaveable { mutableStateOf(name) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxHeight(0.9f)
                .padding(horizontal = 12.dp),
            value = sectionName,
            onValueChange = { text: String ->
                if (text.length <= 60)
                    sectionName = text
            },
            label = { Text(text = "Name") },
            enabled = isEnabled,
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = {
                if (sectionName.isNotEmpty())
                    IconButton(onClick = { sectionName = "" }) {
                        Icon(Icons.Default.Clear, "Clear text")
                    }
            },
        )

        Spacer(modifier = modifier.height(36.dp))

        CancelAndAcceptButtons(
            onCancel = { onCancel() },
            onAccept = {
                onAccept(
                    sectionName.trim(),
                    id,
                )
            },
            cancelText = "Cancel",
            acceptText = "Save",
            isEnable = isEnabled,
            isAcceptButtonEnabled = sectionName.isNotEmpty(),
        )
    }
}