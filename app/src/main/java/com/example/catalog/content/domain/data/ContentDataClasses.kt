package com.example.catalog.content.domain.data

import android.graphics.Bitmap
import android.net.Uri

data class DishData(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val weight: Double = 0.0,
    val imageModel: Uri? = null,
    val updatedImageModel: Bitmap? = null,
)

class DocDishData(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val weight: Double = 0.0,
    val imageByteArray: ByteArray? = null,
)