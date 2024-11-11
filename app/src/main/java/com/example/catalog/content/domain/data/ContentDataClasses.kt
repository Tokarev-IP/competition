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
    val sectionId: String = "",
)

data class SectionData(
    val id: String = "",
    val name: String = "",
    val position: Int = 0,
)

data class MenuInfoData(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageModel: Uri? = null,
    val updatedImageModel: Uri? = null,
)

data class InfoImageData (
    val id: String = "",
    val imageModel: Uri = Uri.EMPTY,
)

class DocDishData(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val weight: Double = 0.0,
    val imageByteArray: ByteArray? = null,
)

class PdfDishData(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val weight: Double = 0.0,
    val bitmap: Bitmap? = null,
)

class PdfMenuData(
    val sectionData: SectionData = SectionData(),
    val pdfDishData: List<PdfDishData> = emptyList(),
)

class MenuViewData(
    val menuInfoData: MenuInfoData = MenuInfoData(),
    val infoImageList: List<InfoImageData> = emptyList(),
    val dishListAndSectionViewDataList: List<DishListAndSectionViewData> = emptyList(),
)

class DishListAndSectionViewData(
    val dishDataList: List<DishData> = emptyList(),
    val sectionData: SectionData = SectionData(),
)