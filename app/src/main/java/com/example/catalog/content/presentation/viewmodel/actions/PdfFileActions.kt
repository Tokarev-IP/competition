package com.example.catalog.content.presentation.viewmodel.actions

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.PdfDishData
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.GenerateAiTextUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PdfFileActions @Inject constructor(
    private val downloadFileUseCaseInterface: DownloadFileUseCaseInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val saveMenuPdfFileUseCaseInterface: SaveMenuPdfFileUseCaseInterface,
    private val generateAiTextUseCaseInterface: GenerateAiTextUseCaseInterface,
) : PdfFileActionsInterface {

    override suspend fun createMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            val pdfDishList = mutableListOf<PdfDishData>()

            for (dish in dishList) {
                var imageBitmap: Bitmap? = null

                if (dish.imageModel != null) {
                    val imageByteArray: ByteArray =
                        withContext(Dispatchers.IO) {
                            downloadFileUseCaseInterface.downloadDishImageFile(
                                menuId = menuId,
                                dishId = dish.id,
                            )
                        }

                    imageBitmap = withContext(Dispatchers.IO) {
                        transformImageUseCaseInterface.getBitmapFromByteArray(imageByteArray)
                    }
                }

                pdfDishList.add(
                    PdfDishData(
                        name = dish.name,
                        price = dish.price,
                        weight = dish.weight,
                        description = dish.description,
                        bitmap = imageBitmap,
                    )
                )
            }
            withContext(Dispatchers.IO) {
                saveMenuPdfFileUseCaseInterface.saveMenuPdfFileInFolder(
                    folderUri = folderUri,
                    pdfDishList = pdfDishList,
                )
//                    createDicFileInterface.createMenuDoc(
//                        folderUri = folderUri,
//                        dishList = docDishList,
//                    )
            }
            onSuccess("PDF file was created")
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun createTranslatedMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        translateLanguage: String,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            val pdfDishList = mutableListOf<PdfDishData>()

            for (dish in dishList) {
                val translatedName = withContext(Dispatchers.IO) {
                    generateAiTextUseCaseInterface.translateText(
                        text = dish.name,
                        language = translateLanguage,
                    )
                }

                val translatedDescription = withContext(Dispatchers.IO) {
                    generateAiTextUseCaseInterface.translateText(
                        text = dish.description,
                        language = translateLanguage,
                    )
                }

                var imageBitmap: Bitmap? = null

                if (dish.imageModel != null) {
                    val imageByteArray: ByteArray =
                        withContext(Dispatchers.IO) {
                            downloadFileUseCaseInterface.downloadDishImageFile(
                                menuId = menuId,
                                dishId = dish.id,
                            )
                        }

                    imageBitmap = withContext(Dispatchers.IO) {
                        transformImageUseCaseInterface.getBitmapFromByteArray(imageByteArray)
                    }
                }

                pdfDishList.add(
                    PdfDishData(
                        name = translatedName,
                        price = dish.price,
                        weight = dish.weight,
                        description = translatedDescription,
                        bitmap = imageBitmap,
                    )
                )
            }
            withContext(Dispatchers.IO) {
                saveMenuPdfFileUseCaseInterface.saveMenuPdfFileInFolder(
                    folderUri = folderUri,
                    pdfDishList = pdfDishList,
                )
            }
//            withContext(Dispatchers.IO) {
//                createDicFileInterface.createMenuDoc(
//                    folderUri = folderUri,
//                    dishList = docDishList,
//                    language = translateLanguage,
//                )
//            }
            onSuccess("PDF file was created")
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface PdfFileActionsInterface {
    suspend fun createMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )

    suspend fun createTranslatedMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        translateLanguage: String,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}