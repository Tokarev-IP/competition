package com.example.catalog.login.presentation.screens.phonecode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.catalog.login.presentation.LoginUiEvents
import com.example.catalog.login.presentation.LoginUiStates

@Composable
internal fun PhoneCodeView(
    modifier: Modifier = Modifier,
    eventHandler: (LoginUiEvents) -> Unit,
    uiState: LoginUiStates,
    verificationId: String,
) {
    var codeText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when (uiState) {
            is LoginUiStates.Loading -> {
                LinearProgressIndicator(
                    modifier = modifier
                        .fillMaxWidth()
                )
            }
        }

        Text(text = "SMS code was sent.")

        Spacer(modifier = modifier.height(24.dp))

        OutlinedTextField(
            value = codeText,
            onValueChange = { text: String ->
                if (text.length <= 6)
                    codeText = text
            },
            isError = (uiState is LoginUiStates.Error),
            supportingText = {
                Text(text = "Write down the code from the SMS")
            },
            label = { Text(text = "SMS code") },
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                if (codeText.isNotEmpty())
                    IconButton(onClick = { codeText = "" }) {
                        Icon(Icons.Default.Clear, "Clear sms code")
                    }
            },
            enabled = (uiState is LoginUiStates.Show),
        )

        Spacer(modifier = modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                keyboardController?.hide()
                eventHandler(
                    LoginUiEvents.LoginUsingSmsCode(
                        verificationId = verificationId,
                        code = codeText,
                    )
                )
            },
            enabled = codeText.length == 6 && (uiState is LoginUiStates.Show),
        ) {
            Text(text = "Log In")
        }
    }
}
