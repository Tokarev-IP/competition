package com.example.catalog.content.presentation.viewmodel.actions

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.extensions.toMenuInfoData
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
                downloadDataUseCaseInterface.downloadMenuIdData(userId = userId)
            }
            data?.let {
                onMenuId(it.id)
            } ?: onEmptyMenuId()
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

    override suspend fun getSectionDataList(
        menuId: String,
        onSectionDataList: (List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val sectionDataList = withContext(Dispatchers.IO) {
                downloadDataUseCaseInterface.downloadMenuSectionListData(menuId = menuId)
            }
            onSectionDataList(sectionDataList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun getSectionAndDishDataLists(
        menuId: String,
        onData: (List<DishData>, List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val dishDataList = withContext(Dispatchers.IO) {
                downloadDataUseCaseInterface.downloadMenuDishListData(menuId = menuId)
            }
            val sectionDataList = withContext(Dispatchers.IO) {
                downloadDataUseCaseInterface.downloadMenuSectionListData(menuId = menuId)
            }
            onData(dishDataList, sectionDataList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun getDishDataOfTheSpecificSection(
        dishDataList: List<DishData>,
        sectionId: String,
        onDishList: (List<DishData>) -> Unit
    ) {
        withContext(Dispatchers.Default) {
            val dishList = dishDataList.filter { it.sectionId == sectionId }
            onDishList(dishList)
        }
    }

    override suspend fun getMenuInfoData(
        menuId: String,
        onMenuInfoData: (MenuInfoData) -> Unit,
        onEmptyMenuInfoData: () -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            val menuInfoData = withContext(Dispatchers.IO) {
                downloadDataUseCaseInterface.downloadMenuInfoData(menuId = menuId)
            }
            menuInfoData?.let { data ->
                onMenuInfoData(data.toMenuInfoData())
            } ?: onEmptyMenuInfoData()
        } catch (e: Exception){
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun getInfoImageDataList(
        menuId: String,
        onInfoImageList: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val infoImageDataList = withContext(Dispatchers.IO){
                downloadDataUseCaseInterface.downloadInfoImageListData(menuId = menuId)
            }
            onInfoImageList(infoImageDataList)
        } catch (e: Exception){
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

    suspend fun getSectionDataList(
        menuId: String,
        onSectionDataList: (List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )

    suspend fun getSectionAndDishDataLists(
        menuId: String,
        onData: (List<DishData>, List<SectionData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )

    suspend fun getDishDataOfTheSpecificSection(
        dishDataList: List<DishData>,
        sectionId: String,
        onDishList: (List<DishData>) -> Unit,
    )

    suspend fun getMenuInfoData(
        menuId: String,
        onMenuInfoData: (MenuInfoData) -> Unit,
        onEmptyMenuInfoData: () -> Unit,
        onErrorMessage: (String) -> Unit,
    )

    suspend fun getInfoImageDataList(
        menuId: String,
        onInfoImageList: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}