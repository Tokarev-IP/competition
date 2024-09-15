package com.example.catalog.content.domain.extensions

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.SectionData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun List<DishData>.removeDishItem(dishId: String): List<DishData> {
    return suspendCoroutine { continuation ->
        val newDishList = this.toMutableList()
        newDishList.removeIf { it.id == dishId }
        continuation.resume(newDishList.toList())
    }
}

suspend fun List<DishData>.addDishItem(dishData: DishData): List<DishData> {
    return suspendCoroutine { continuation ->
        val newDishList = mutableMapOf<String, DishData>()
        for (dish in this) {
            newDishList[dish.id] = dish
        }
        newDishList[dishData.id] = dishData
        continuation.resume(newDishList.values.toList())
    }
}

suspend fun List<SectionData>.removeSectionItem(sectionId: String): List<SectionData> {
    return suspendCoroutine { continuation ->
        val newSectionList = this.toMutableList()
        newSectionList.removeIf { it.id == sectionId }
        continuation.resume(newSectionList.toList())
    }
}

suspend fun List<SectionData>.addSectionItem(sectionData: SectionData): List<SectionData> {
    return suspendCoroutine { continuation ->
        val newSectionList = mutableMapOf<String, SectionData>()
        for (section in this) {
            newSectionList[section.id] = section
        }
        newSectionList[sectionData.id] = sectionData
        continuation.resume(newSectionList.values.toList())
    }
}