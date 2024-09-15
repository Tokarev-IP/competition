package com.example.catalog.content.presentation.viewmodel.actions

import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.extensions.addSectionItem
import com.example.catalog.content.domain.extensions.removeSectionItem
import com.example.catalog.content.domain.extensions.toSectionDataFirebase
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditSectionListActions @Inject constructor(
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
    private val deleteDataUseCaseInterface: DeleteDataUseCaseInterface,
) : EditSectionListActionsInterface {

    override suspend fun saveSectionItem(
        data: SectionData,
        menuId: String,
        documentId: String,
        sectionList: List<SectionData>,
        onUpdatedSectionList: (List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            withContext(Dispatchers.IO) {
                uploadDataUseCaseInterface.uploadMenuSectionData(
                    data = data.toSectionDataFirebase(),
                    menuId = menuId,
                    sectionId = documentId,
                )
            }
            val updatedSectionList = sectionList.addSectionItem(data)
            onUpdatedSectionList(updatedSectionList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun deleteSectionItem(
        menuId: String,
        sectionId: String,
        sectionList: List<SectionData>,
        onUpdatedSectionList: (List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            withContext(Dispatchers.IO) {
                deleteDataUseCaseInterface.deleteMenuSectionData(
                    menuId = menuId,
                    sectionId = sectionId,
                )
            }
            val updatedSectionList = sectionList.removeSectionItem(sectionId)
            onUpdatedSectionList(updatedSectionList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

}

interface EditSectionListActionsInterface {
    suspend fun saveSectionItem(
        data: SectionData,
        menuId: String,
        documentId: String,
        sectionList: List<SectionData>,
        onUpdatedSectionList: (List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )

    suspend fun deleteSectionItem(
        menuId: String,
        sectionId: String,
        sectionList: List<SectionData>,
        onUpdatedSectionList: (List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}