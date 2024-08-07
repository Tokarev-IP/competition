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
import com.example.catalog.content.presentation.LanguageList

@Composable
internal fun LanguageRadioButtons(
    modifier: Modifier = Modifier,
    onSelected: (LanguageList?) -> Unit,
    radioOptions: List<LanguageList>,
    infoText: String,
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth(),
    ) {
        Spacer(modifier = modifier.height(8.dp))
        Text(text = infoText)
        Spacer(modifier = modifier.height(8.dp))
        Column(modifier.selectableGroup()) {
            radioOptions.forEach { element ->
                Row(
                    modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (element == selectedOption),
                            onClick = {
                                onOptionSelected(element)
                                if (radioOptions[0] == element)
                                    onSelected(null)
                                else
                                    onSelected(element)
                            },
                            role = Role.RadioButton
                        )
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (element == selectedOption),
                        onClick = null
                    )
                    Text(
                        text = element.language,
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
    LanguageRadioButtons(
        onSelected = {},
        radioOptions = LanguageList.entries.toList(),
        infoText = "Please, choose language to translate:",
    )
}