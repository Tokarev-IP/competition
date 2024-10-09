package com.example.catalog.content.domain.functions

import com.example.catalog.content.domain.data.DishData
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DishListFunctions @Inject constructor() : DishListFunctionsInterface {

     override suspend fun deleteDishItemFromDishList(
        dishId: String,
        dishList: List<DishData>
    ): List<DishData> {
        return suspendCoroutine { continuation ->
            val newDishList = dishList.toMutableList()
            newDishList.removeIf { it.id == dishId }
            continuation.resume(newDishList.toList())
        }
    }

     override suspend fun saveDishItemInDishList(
        dishData: DishData,
        dishList: List<DishData>
    ): List<DishData> {
        return suspendCoroutine { continuation ->
            val newDishList = mutableMapOf<String, DishData>()
            for (dish in dishList) {
                newDishList[dish.id] = dish
            }
            newDishList[dishData.id] = dishData
            continuation.resume(newDishList.values.toList())
        }
    }
}

interface DishListFunctionsInterface {
    suspend fun deleteDishItemFromDishList(
        dishId: String,
        dishList: List<DishData>
    ): List<DishData>

    suspend fun saveDishItemInDishList(
        dishData: DishData,
        dishList: List<DishData>
    ): List<DishData>
}