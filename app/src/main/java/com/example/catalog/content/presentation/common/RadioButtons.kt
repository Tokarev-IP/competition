package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun RadioButtons(
    modifier: Modifier = Modifier,
    onSelected: (String?) -> Unit,
    radioOptions: List<String>,
    infoText: String,
    responseText: String,
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
    ) {
        HorizontalDivider()
        Spacer(modifier = modifier.height(8.dp))
        Text(text = infoText)
        Spacer(modifier = modifier.height(8.dp))
        Column(modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                Row(
                    modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                if (radioOptions[0] == text)
                                    onSelected(null)
                                else
                                    onSelected(responseText + text)
                            },
                            role = Role.RadioButton
                        )
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null
                    )
                    Text(
                        text = text,
                        modifier = modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenerateAiDishNameBottomSheetPreview() {
    RadioButtons(
        onSelected = {},
        radioOptions = listOf("Norm", "Good", "Bad", "Better", "Worse"),
        infoText = "Please, choose only one parameter which you want to use for generating dish name:",
        responseText = "You selected: ",
    )
}