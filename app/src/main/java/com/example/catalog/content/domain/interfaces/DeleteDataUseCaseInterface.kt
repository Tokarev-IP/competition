package com.example.catalog.content.domain.interfaces

interface DeleteDataUseCaseInterface {

    suspend fun deleteMenuDish(
        collection1: String = "data",
        collection2: String = "menu",
        menuId: String,
        dishId: String,
    )
}