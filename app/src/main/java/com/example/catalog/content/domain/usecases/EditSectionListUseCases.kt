package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.adapters.FirestoreDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreUploadAdapterInterface
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.extensions.addSectionItem
import com.example.catalog.content.domain.extensions.removeSectionItem
import com.example.catalog.content.domain.extensions.toSectionDataFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditSectionListUseCases @Inject constructor(
    private val firestoreUploadAdapterInterface: FirestoreUploadAdapterInterface,
    private val firestoreDeleteAdapterInterface: FirestoreDeleteAdapterInterface,
) : EditSectionListUseCasesInterface {

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
                firestoreUploadAdapterInterface.uploadMenuSectionData(
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
                firestoreDeleteAdapterInterface.deleteMenuSectionData(
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

interface EditSectionListUseCasesInterface {
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