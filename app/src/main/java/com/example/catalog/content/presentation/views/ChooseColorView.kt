package com.example.catalog.content.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.common.CancelAndAcceptButtons
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ChooseColorView(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
    onCancelled: () -> Unit,
) {
    val controller = rememberColorPickerController()
    var color: Color? by rememberSaveable { mutableStateOf(null) }
    var colorHexCode: String? by rememberSaveable { mutableStateOf(null) }

    controller.setDebounceDuration(50L)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        HsvColorPicker(
            modifier = modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(10.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                color = colorEnvelope.color
                colorHexCode = colorEnvelope.hexCode
            }
        )

//        AlphaSlider(controller = controller)
//        AlphaSlider(controller = controller)

        Box(
            modifier = modifier
                .width(60.dp)
                .height(60.dp)
                .background(color = color ?: Color.Transparent, shape = CircleShape),
        )

        Text(text = colorHexCode ?: "")

        CancelAndAcceptButtons(
            onCancel = { onCancelled() },
            onAccept = { onColorSelected(color ?: Color.Transparent) },
            acceptText = "Choose this color",
            cancelText = "Cancel",
        )

    }

}