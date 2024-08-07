package com.example.catalog.content.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.LanguageList
import com.example.catalog.content.presentation.common.CancelAndAcceptButtons
import com.example.catalog.content.presentation.common.LanguageRadioButtons

@Composable
internal fun ChooseLanguageDialogView(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onAccept: (selectedLanguage: String?) -> Unit,
) {
    var selectedLanguage: String? = null

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        LanguageRadioButtons(
            onSelected = { languageList: LanguageList? ->
                selectedLanguage = languageList?.language
            },
            radioOptions = LanguageList.entries.toList(),
            infoText = "Please, choose language to translate:",
        )
        Spacer(modifier = modifier.height(16.dp))
        CancelAndAcceptButtons(
            onCancel = { onCancel() },
            onAccept = { onAccept(selectedLanguage) },
            cancelText = "Cancel",
            acceptText = "Next"
        )
    }
}