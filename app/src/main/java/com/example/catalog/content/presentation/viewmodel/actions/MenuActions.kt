package com.example.catalog.content.presentation.viewmodel.actions

import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuActions @Inject constructor(
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
) : MenuActionsInterface {

    override suspend fun createMenuId(
        menuId: String,
        userId: String,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            withContext(Dispatchers.IO) {
                uploadDataUseCaseInterface.uploadMenuId(
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

interface MenuActionsInterface {
    suspend fun createMenuId(
        menuId: String,
        userId: String,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}