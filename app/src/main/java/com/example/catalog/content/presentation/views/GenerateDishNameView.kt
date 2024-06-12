package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.ParametersForGeneratingAiDishName
import com.example.catalog.content.presentation.common.CancelAndChooseButtons
import com.example.catalog.content.presentation.common.RadioButtons

@Composable
internal fun GenerateDishNameView(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onChoose: (List<String?>) -> Unit,
    radioOptionsList: List<ParametersForGeneratingAiDishName>,
) {
    val chosenParametersList = mutableListOf<String?>()

    radioOptionsList.forEach { _ ->
        chosenParametersList.add(null)
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            Text(
                modifier = modifier.padding(horizontal = 16.dp),
                text = "Please, choose parameters, which you want to use for generating the dish name",
            )
            HorizontalDivider()
        }
        items(radioOptionsList.size) {
            RadioButtons(
                onSelected = { chosenText ->
                    chosenParametersList[it] = chosenText
                },
                radioOptions = radioOptionsList[it].parameters,
                infoText = radioOptionsList[it].description,
                responseText = radioOptionsList[it].responseText,
            )
        }
        item {
            HorizontalDivider()
            CancelAndChooseButtons(
                onChoose = { onChoose(chosenParametersList) },
                onCancel = { onCancel() },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GeneratingDishNameViewPreview() {
    GenerateDishNameView(
        onCancel = {},
        onChoose = {},
        radioOptionsList = listOf(
            ParametersForGeneratingAiDishName.Taste,
            ParametersForGeneratingAiDishName.Mood,
            ParametersForGeneratingAiDishName.Season,
        ),
    )
}