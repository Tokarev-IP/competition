package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun GeneratingAiTextView(
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(false) }
    var additionalInfoText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Switch(
            checked = checked,
            onCheckedChange = { isChecked: Boolean ->
                checked =  isChecked
            },
            thumbContent = {
                Icon(Icons.Filled.Edit, "Edit additional info text for generating AI text")
            }
        )

        if (checked) {
            OutlinedTextField(
                value = additionalInfoText,
                onValueChange = { text: String ->
                    additionalInfoText = text
                },
                label = { Text(text = "Additional text for generating AI text") },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (additionalInfoText.isNotEmpty())
                        IconButton(onClick = { additionalInfoText = "" }) {
                            Icon(Icons.Default.Clear, "Clear text")
                        }
                },
            )
        }
    }
}