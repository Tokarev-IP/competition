package com.example.catalog.content.domain.interfaces

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionDataFirebase

//interface DownloadDataUseCaseInterface {
//
//    suspend fun downloadMenuId(
//        collection: String = "id",
//        userId: String,
//    ): MenuIdFirebase?
//
//    suspend fun downloadMenuInfoData(
//        collection: String = "menu",
//        menuId: String,
//    ): MenuInfoFirebase?
//
//    suspend fun downloadMenuDishListData(
//        collection1: String = "data",
//        collection2: String = "menu",
//        menuId: String,
//    ): List<DishData>
//
//    suspend fun downloadMenuSectionListData(
//        collection1: String = "data",
//        collection2: String = "section",
//        menuId: String,
//    ): List<SectionDataFirebase>
//
//}