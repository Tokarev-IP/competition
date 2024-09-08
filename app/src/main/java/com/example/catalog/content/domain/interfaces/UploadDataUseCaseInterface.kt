package com.example.catalog.content.domain.interfaces

import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionDataFirebase

//interface UploadDataUseCaseInterface {
//    suspend fun uploadMenuId(
//        collection: String = "id",
//        userId: String,
//        menuIdFirebase: MenuIdFirebase,
//    )
//
//    suspend fun uploadMenuInfoData(
//        collection: String = "menu",
//        data: MenuInfoFirebase,
//        menuId: String,
//    )
//
//    suspend fun uploadMenuDishData(
//        collection1: String = "data",
//        collection2: String = "menu",
//        data: DishDataFirebase,
//        menuId: String,
//        documentId: String,
//    )
//
//    suspend fun uploadMenuSection(
//        collection1: String = "data",
//        collection2: String = "section",
//        data: SectionDataFirebase,
//        menuId: String,
//        documentId: String,
//    )
//}