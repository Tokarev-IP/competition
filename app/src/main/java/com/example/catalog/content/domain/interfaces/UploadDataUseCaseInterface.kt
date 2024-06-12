package com.example.catalog.content.domain.interfaces

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuId
import com.example.catalog.content.domain.data.MenuInfo

interface UploadDataUseCaseInterface {

    fun uploadMenuId(
        collection: String = "id",
        userId: String,
        menuId: MenuId,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    )

    fun uploadMenuInfoData(
        collection: String = "menu",
        data: MenuInfo,
        menuId: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    )

    fun uploadMenuDishData(
        collection1: String = "data",
        collection2: String = "menu",
        data: DishData,
        menuId: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    )
}