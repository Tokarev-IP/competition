package com.example.catalog.content.presentation.viewmodel.actions

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.usecases.network.DownloadDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDataActions @Inject constructor(
    private val downloadDataUseCaseInterface: DownloadDataUseCaseInterface
) : GetDataActionsInterface {

    override suspend fun getCurrentMenuId(
        userId: String,
        onEmptyMenuId: () -> Unit,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            val data = withContext(Dispatchers.IO) {
                downloadDataUseCaseInterface.downloadMenuId(userId = userId)
            }
            if (data == null) {
                onEmptyMenuId()
            } else {
                onMenuId(data.id)
            }
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun getDishDataList(
        menuId: String,
        onDishDataList: (List<DishData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
            try {
                val dishDataList = withContext(Dispatchers.IO) {
                    downloadDataUseCaseInterface.downloadMenuDishListData(menuId = menuId)
                }
                onDishDataList(dishDataList)
            } catch (e: Exception) {
                onErrorMessage(e.message.toString())
            }
    }

}

interface GetDataActionsInterface {
    suspend fun getCurrentMenuId(
        userId: String,
        onEmptyMenuId: () -> Unit,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
    suspend fun getDishDataList(
        menuId: String,
        onDishDataList: (List<DishData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}