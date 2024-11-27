package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.adapters.FirestoreUploadAdapterInterface
import com.example.catalog.content.domain.data.MenuIdFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateCreateMenuUseCases @Inject constructor(
    private val firestoreUploadAdapterInterface: FirestoreUploadAdapterInterface,
) : CreateMenuUseCasesInterface {

    override suspend fun createMenuId(
        menuId: String,
        userId: String,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            withContext(Dispatchers.IO) {
                firestoreUploadAdapterInterface.uploadMenuId(
                    userId = userId,
                    menuIdFirebase = MenuIdFirebase(id = menuId),
                )
            }
            onMenuId(menuId)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface CreateMenuUseCasesInterface {
    suspend fun createMenuId(
        menuId: String,
        userId: String,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}