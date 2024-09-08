package com.example.catalog.content.domain.usecases.logic

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.catalog.content.domain.data.DocDishData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CreateDocFile @Inject constructor(
    @ApplicationContext private val context: Context
) : CreateDocFileInterface {

    override suspend fun createMenuDoc(
        folderUri: Uri,
        language: String,
        filename: String,
        dishList: List<DocDishData>,
    ) {
        return suspendCancellableCoroutine { continuation ->

            val document = XWPFDocument()

            try {
                val table = document.createTable(dishList.size, 2)
                table.setWidth("100%")

                for ((index, item) in dishList.withIndex()) {

                    val cell1 = table.getRow(index).getCell(0)
                    val p1 = cell1.addParagraph()

                    val run1 = p1.createRun()
                    run1.isBold = true
                    run1.fontSize = 12
                    run1.setText(item.name)
                    run1.addBreak()

                    val run2 = p1.createRun()
                    run2.isBold = true
                    run2.fontSize = 12
                    run2.setText("${item.price} $")
                    run2.addBreak()

                    val run3 = p1.createRun()
                    run3.setText(item.description)

                    item.imageByteArray?.let { imageData ->
                        val cell2 = table.getRow(index).getCell(1)
                        val p2 = cell2.addParagraph()
                        val run4 = p2.createRun()

                        val originalBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                        val originalWidth = originalBitmap.width
                        val originalHeight = originalBitmap.height

                        val maxWidth = Units.toEMU(120.0)
                        val maxHeight = Units.toEMU(100.0)

                        val aspectRatio = originalWidth.toDouble() / originalHeight.toDouble()
                        var width: Int
                        var height: Int

                        if (aspectRatio > 1) { // Landscape image
                            width = maxWidth
                            height = (maxWidth / aspectRatio).toInt()
                        } else { // Portrait image or Square image
                            height = maxHeight
                            width = (maxHeight * aspectRatio).toInt()
                        }

                        if (width > maxWidth) {
                            width = maxWidth
                            height = (maxWidth / aspectRatio).toInt()
                        }

                        if (height > maxHeight) {
                            height = maxHeight
                            width = (maxHeight * aspectRatio).toInt()
                        }

                        ByteArrayInputStream(imageData).use { inputStream ->
                            run4.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", width, height)
                        }

                        p2.alignment = ParagraphAlignment.CENTER
                    }
                }

                val resolver = context.contentResolver
                val folder = DocumentFile.fromTreeUri(context, folderUri)
                val fileUri = folder?.createFile(
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    filename
                )

                fileUri?.uri?.let { uri ->
                    resolver.openOutputStream(uri).use { outputStream ->
                        document.write(outputStream)
                    }
                }

                continuation.resume(Unit)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            } finally {
                document.close()
            }
        }
    }
}

interface CreateDocFileInterface {
    suspend fun createMenuDoc(
        folderUri: Uri,
        language: String = "",
        filename: String = "menu_doc_${language}.docx",
        dishList: List<DocDishData>,
    )
}