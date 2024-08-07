package com.example.catalog.content.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SwitchItem(
    modifier: Modifier = Modifier,
    isSwitched: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    text: String,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
        )
        Switch(
            checked = isSwitched,
            onCheckedChange = {
                onCheckedChange(!isSwitched)
            },
            enabled = enabled,
            thumbContent = {
                if (isSwitched) {
                    Icon(Icons.Filled.Check, contentDescription = "Switch is on")
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SwitchItemPreview(){
    SwitchItem(
        isSwitched = true,
        onCheckedChange = {},
        enabled = true,
        text = "Background color",
    )
}