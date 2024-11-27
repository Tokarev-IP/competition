package com.example.catalog.content.domain.usecases.logic

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.catalog.content.domain.data.PdfDishData
import com.example.catalog.content.domain.functions.CreateMenuPdfFileInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.OutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SaveMenuPdfFileUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context
) : SaveMenuPdfFileUseCaseInterface, CreateMenuPdfFileInterface {

    override suspend fun saveMenuPdfFileInFolder(
        folderUri: Uri,
        pdfDishList: List<PdfDishData>,
    ) {
        return suspendCancellableCoroutine { continuation ->
            val folder = DocumentFile.fromTreeUri(appContext, folderUri)

            val pdfFile = folder?.createFile("application/pdf", "menu.pdf")

            pdfFile?.uri?.let { fileUri ->
                try {
                    val contentResolver: ContentResolver = appContext.contentResolver
                    val outputStream: OutputStream? = contentResolver.openOutputStream(fileUri)

                    if (outputStream != null) {
                        val pdfDocument = createMenuPdfDocument(pdfMenuList = pdfDishList)

                        pdfDocument.writeTo(outputStream)
                        pdfDocument.close()
                        outputStream.close()
                        continuation.resume(Unit)
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
            if (pdfFile == null)
                continuation.resumeWithException(Exception("File not created"))
        }
    }
}

interface SaveMenuPdfFileUseCaseInterface {
    suspend fun saveMenuPdfFileInFolder(
        folderUri: Uri,
        pdfDishList: List<PdfDishData>,
    )
}