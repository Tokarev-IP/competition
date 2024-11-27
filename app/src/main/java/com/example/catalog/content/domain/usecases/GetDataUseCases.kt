package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.adapters.FirestoreDownloadAdapterInterface
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.data.MenuViewData
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.extensions.toMenuInfoData
import com.example.catalog.content.domain.functions.SortDataInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.filterList
import javax.inject.Inject

class GetDataUseCases @Inject constructor(
    private val firestoreDownloadAdapterInterface: FirestoreDownloadAdapterInterface,
    private val sortDataInterface: SortDataInterface,
) : GetDataUseCasesInterface {

    override suspend fun getCurrentMenuId(
        userId: String,
        onEmptyMenuId: () -> Unit,
        onMenuId: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            val data = withContext(Dispatchers.IO) {
                firestoreDownloadAdapterInterface.downloadMenuIdData(userId = userId)
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
                firestoreDownloadAdapterInterface.downloadMenuDishListData(menuId = menuId)
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
                firestoreDownloadAdapterInterface.downloadMenuSectionListData(menuId = menuId)
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
                firestoreDownloadAdapterInterface.downloadMenuDishListData(menuId = menuId)
            }
            val sectionDataList = withContext(Dispatchers.IO) {
                firestoreDownloadAdapterInterface.downloadMenuSectionListData(menuId = menuId)
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
            val newDishList = dishDataList.filterList { this.sectionId == sectionId }
            onDishList(newDishList)
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
                firestoreDownloadAdapterInterface.downloadMenuInfoData(menuId = menuId)
            }
            menuInfoData?.let { data ->
                onMenuInfoData(data.toMenuInfoData())
            } ?: onEmptyMenuInfoData()
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun getInfoImageDataList(
        menuId: String,
        onInfoImageList: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val infoImageDataList = withContext(Dispatchers.IO) {
                firestoreDownloadAdapterInterface.downloadInfoImageListData(menuId = menuId)
            }
            onInfoImageList(infoImageDataList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun getAllMenuData(
        menuId: String,
        onViewMenuData: (MenuViewData) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val dishDataList = withContext(Dispatchers.IO) {
                firestoreDownloadAdapterInterface.downloadMenuDishListData(menuId = menuId)
            }
            val sectionDataList = withContext(Dispatchers.IO) {
                firestoreDownloadAdapterInterface.downloadMenuSectionListData(menuId = menuId)
            }
            val menuInfoData = withContext(Dispatchers.IO) {
                val response = firestoreDownloadAdapterInterface.downloadMenuInfoData(menuId = menuId)
                response?.toMenuInfoData() ?: MenuInfoData()
            }
            val infoImageDataList = withContext(Dispatchers.IO) {
                firestoreDownloadAdapterInterface.downloadInfoImageListData(menuId = menuId)
            }

            val dishAndSectionListData = withContext(Dispatchers.Default) {
                sortDataInterface.sortDishData(
                    dishDataList = dishDataList,
                    sectionDataList = sectionDataList,
                )
            }

            onViewMenuData(
                MenuViewData(
                    menuInfoData = menuInfoData,
                    infoImageList = infoImageDataList,
                    dishListAndSectionViewDataList = dishAndSectionListData,
                )
            )
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

}

interface GetDataUseCasesInterface {
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

    suspend fun getAllMenuData(
        menuId: String,
        onViewMenuData: (MenuViewData) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}