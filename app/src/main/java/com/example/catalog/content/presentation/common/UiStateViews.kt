package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadingStateView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        CircularProgressIndicator(
            modifier = modifier.align(Alignment.Center)
        )
    }
}

@Composable
internal fun ErrorStateView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    onRefreshClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Something went wrong")
        Spacer(modifier = Modifier.height(36.dp))
        OutlinedButton(
            onClick = { onRefreshClick() }
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = "Refresh loading")
            Text(text = "Try again")
        }
    }
}