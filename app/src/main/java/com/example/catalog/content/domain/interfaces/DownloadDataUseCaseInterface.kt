package com.example.catalog.content.domain.interfaces

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuId
import com.example.catalog.content.domain.data.MenuInfo

interface DownloadDataUseCaseInterface {

    fun downloadMenuId(
        collection: String = "id",
        userId: String,
        onSuccess: (menuId: MenuId) -> Unit,
        onNull: () -> Unit,
        onFailure: (msg: String) -> Unit
    )

    fun downloadMenuInfoData(
        collection: String = "menu",
        menuId: String,
        onSuccess: (menuInfo: MenuInfo) -> Unit,
        onNull: () -> Unit,
        onFailure: (msg: String) -> Unit
    )

    fun downloadMenuDishListData(
        collection1: String = "data",
        collection2: String = "menu",
        menuId: String,
        onSuccess: (List<DishData>) -> Unit,
        onFailure: (msg: String) -> Unit
    )

}