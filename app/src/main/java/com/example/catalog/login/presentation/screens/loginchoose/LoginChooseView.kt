package com.example.catalog.login.presentation.screens.loginchoose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.catalog.login.presentation.LoginUiEvents
import com.example.catalog.login.presentation.LoginUiStates

@Composable
internal fun LoginChooseView(
    modifier: Modifier = Modifier,
    uiState: LoginUiStates,
    eventHandler: (LoginUiEvents) -> Unit,
) {
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

            is LoginUiStates.Error -> {
                Text(text = "There are some problems. Please, try again")
                Spacer(modifier = modifier.height(24.dp))
                OutlinedButton(
                    onClick = {
                        eventHandler(LoginUiEvents.CheckUser)
                    },
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    Text(text = "Try again")
                }
            }
        }

        Column(
            modifier = modifier
                .padding(horizontal = 40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Please, choose an appropriate option to login")

            Spacer(modifier = modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    eventHandler(LoginUiEvents.LoginUsingPhoneNumber)
                },
                enabled = (uiState is LoginUiStates.Show)
            ) {
                Text(text = "Login with SMS code")
            }

            Spacer(modifier = modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    eventHandler(LoginUiEvents.LoginWithGoogle)
                },
                enabled = (uiState is LoginUiStates.Show)
            ) {
                Text(text = "Login with Google")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginChooseViewPreview() {
    LoginChooseView(uiState = LoginUiStates.Loading) {
        
    }
}