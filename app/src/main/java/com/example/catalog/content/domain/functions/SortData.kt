package com.example.catalog.content.domain.functions

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DishListAndSectionViewData
import com.example.catalog.content.domain.data.SectionData
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SortData @Inject constructor(): SortDataInterface {
    override suspend fun sortDishData(
        dishDataList: List<DishData>,
        sectionDataList: List<SectionData>
    ): List<DishListAndSectionViewData> {
        return suspendCoroutine { continuation ->
            val dishListAndSectionViewDataList = mutableListOf<DishListAndSectionViewData>()
            for (section in sectionDataList) {
                dishListAndSectionViewDataList.add(
                    DishListAndSectionViewData(
                        dishDataList = dishDataList.filter { it.sectionId == section.id },
                        sectionData = section
                    )
                )
            }
            continuation.resume(dishListAndSectionViewDataList)
        }
    }

}

interface SortDataInterface{
    suspend fun sortDishData(
        dishDataList: List<DishData>,
        sectionDataList: List<SectionData>,
    ): List<DishListAndSectionViewData>
}