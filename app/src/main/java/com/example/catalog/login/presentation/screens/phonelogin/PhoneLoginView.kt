package com.example.catalog.login.presentation.screens.phonelogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
internal fun PhoneLoginView(
    modifier: Modifier = Modifier,
    onPhoneLogin: (phoneNumber: String) -> Unit,
    eventHandler: (LoginUiEvents) -> Unit,
    uiState: LoginUiStates,
) {
    var phoneNumberText by rememberSaveable { mutableStateOf("") }

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

        Column(
            modifier = modifier
                .padding(40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Please, write a valid phone number to get a SMS code to login")

            Spacer(modifier = modifier.height(24.dp))

            OutlinedTextField(
                value = phoneNumberText,
                onValueChange = { text: String ->
                    if (text.length in 1..19)
                        phoneNumberText = text
                },
                supportingText = {
                    Text(text = "Write a valid phone number")
                },
                label = { Text(text = "Phone Number") },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (phoneNumberText.isNotEmpty())
                        IconButton(onClick = { phoneNumberText = "" }) {
                            Icon(Icons.Default.Clear, "Clear phone number")
                        }
                },
                enabled = (uiState is LoginUiStates.Show),
                maxLines = 1,
                isError = (uiState is LoginUiStates.Error),
            )

            Spacer(modifier = modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    keyboardController?.hide()
//                    eventHandler(LoginUiEvents.SendLoginSmsCode(("+$phoneNumberText")))
                    onPhoneLogin(("+$phoneNumberText"))
                },
                enabled = (uiState is LoginUiStates.Show) && phoneNumberText.length in 2..19
            ) {
                Text(text = "Get a SMS code")
            }
        }

    }
}