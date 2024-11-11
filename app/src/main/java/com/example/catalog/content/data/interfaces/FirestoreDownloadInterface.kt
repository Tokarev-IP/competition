package com.example.catalog.content.data.interfaces

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.InfoImageFirebase
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.data.SectionDataFirebase

//interface FirestoreDownloadInterface {
//
//    fun downloadMenuIdDataFromFirebase(
//        collection: String = "id",
//        userId: String,
//        onSuccess: (result: MenuIdFirebase?) -> Unit,
//        onFailure: (e: Exception) -> Unit,
//    )
//
//    fun downloadMenuInfoDataFromFirebase(
//        collection: String = "info",
//        menuId: String,
//        onSuccess: (result: MenuInfoFirebase?) -> Unit,
//        onFailure: (e: Exception) -> Unit,
//    )
//
//    fun downloadMenuDishListDataFromFirebase(
//        collection1: String = "data",
//        collection2: String = "menu",
//        menuId: String,
//        onSuccess: (result: List<DishDataFirebase>) -> Unit,
//        onFailure: (e: Exception) -> Unit,
//    )
//
//    fun downloadMenuSectionListDataFromFirebase(
//        collection1: String = "data",
//        collection2: String = "section",
//        menuId: String,
//        onSuccess: (result: List<SectionDataFirebase>) -> Unit,
//        onFailure: (e: Exception) -> Unit,
//    )
//
//    fun downloadInfoImageListDataFromFirebase(
//        collection1: String = "data",
//        collection2: String = "image",
//        menuId: String,
//        onSuccess: (result: List<InfoImageFirebase>) -> Unit,
//        onFailure: (e: Exception) -> Unit,
//    )
//}