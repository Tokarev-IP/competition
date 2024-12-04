package com.example.catalog.content.domain.usecases

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.data.adapters.FirebaseStorageDownloadAdapterInterface
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.PdfDishData
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PdfFileUseCases @Inject constructor(
    private val firebaseStorageDownloadAdapterInterface: FirebaseStorageDownloadAdapterInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val saveMenuPdfFileInterface: SaveMenuPdfFileInterface,
    private val generateAiTextUseCaseInterface: GenerateAiTextUseCaseInterface,
) : PdfFileUseCasesInterface {

    override suspend fun createMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        sectionList: List<SectionData>,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val pdfDishList = mutableListOf<PdfDishData>()

            for (dish in dishList) {
                var imageBitmap: Bitmap? = null

                dish.imageModel?.let {
                    val imageByteArray: ByteArray =
                        withContext(Dispatchers.IO) {
                            firebaseStorageDownloadAdapterInterface.downloadDishImageFile(
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
                        sectionId = dish.sectionId,
                    )
                )
            }
            withContext(Dispatchers.IO) {
                saveMenuPdfFileInterface.saveMenuPdfFileInFolder(
                    folderUri = folderUri,
                    pdfDishList = pdfDishList,
                    sectionList = sectionList,
                )
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
        sectionList: List<SectionData>,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit
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

                dish.imageModel?.let {
                    val imageByteArray: ByteArray =
                        withContext(Dispatchers.IO) {
                            firebaseStorageDownloadAdapterInterface.downloadDishImageFile(
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
                        sectionId = dish.sectionId,
                    )
                )
            }
            withContext(Dispatchers.IO) {
                saveMenuPdfFileInterface.saveMenuPdfFileInFolder(
                    folderUri = folderUri,
                    pdfDishList = pdfDishList,
                    sectionList = sectionList,
                )
            }
            onSuccess("PDF file was created")
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface PdfFileUseCasesInterface {
    suspend fun createMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        sectionList: List<SectionData>,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )

    suspend fun createTranslatedMenuPdfFile(
        menuId: String,
        folderUri: Uri,
        translateLanguage: String,
        sectionList: List<SectionData>,
        dishList: List<DishData>,
        onSuccess: (String) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}