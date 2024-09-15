package com.example.catalog.content.domain.extensions

import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.InfoImageFirebase
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.data.SectionDataFirebase

fun DishDataFirebase.toDishData(): DishData = DishData(
    id = this.id,
    name = this.name,
    description = this.description,
    price = this.price,
    weight = this.weight,
    imageModel = if (this.image != null) Uri.parse(this.image) else null,
    sectionId = this.sectionId
)

fun DishData.toDishDataFirebase(): DishDataFirebase = DishDataFirebase(
    id = this.id,
    name = this.name,
    description = this.description,
    image = if (this.imageModel != null) this.imageModel.toString() else null,
    price = this.price,
    weight = this.weight,
    sectionId = this.sectionId
)

fun SectionDataFirebase.toSectionData(): SectionData = SectionData(
    id = this.id,
    name = this.name,
    position = this.position,
)

fun SectionData.toSectionDataFirebase(): SectionDataFirebase = SectionDataFirebase(
    id = this.id,
    name = this.name,
    position = this.position,
)

fun MenuInfoData.toMenuInfoFirebase(): MenuInfoFirebase = MenuInfoFirebase(
    name = this.name,
    description = this.description,
    image = if (this.imageModel != null) this.imageModel.toString() else null,
)

fun MenuInfoFirebase.toMenuInfoData(): MenuInfoData = MenuInfoData(
    name = this.name,
    description = this.description,
    imageModel = if (this.image != null) Uri.parse(this.image) else null,
)

fun InfoImageData.toInfoImageFirebase(): InfoImageFirebase = InfoImageFirebase(
    id = this.id,
    image = imageModel.toString()
)

fun InfoImageFirebase.toInfoImageData(): InfoImageData = InfoImageData(
    id = this.id,
    imageModel = Uri.parse(this.image)
)