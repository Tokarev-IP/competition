package com.example.catalog.content.domain.data

import android.net.Uri

fun DishDataFirebase.toDishData(): DishData = DishData(
    id = this.id,
    name = this.name,
    description = this.description,
    price = this.price,
    weight = this.weight,
    imageModel = if (this.image != null) Uri.parse(this.image) else null,
)

fun DishData.toDishDataFirebase(): DishDataFirebase = DishDataFirebase(
    id = this.id,
    name = this.name,
    description = this.description,
    image = if (this.imageModel != null) this.imageModel.toString() else null,
    price = this.price,
    weight = this.weight,
)