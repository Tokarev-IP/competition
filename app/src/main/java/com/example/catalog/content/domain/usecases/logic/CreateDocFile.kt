package com.example.catalog.content.domain.usecases.logic

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.example.catalog.content.domain.data.DocDishData
import com.example.catalog.content.domain.functions.CreateDocFileInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CreateDocFile @Inject constructor() : CreateDocFileInterface {

    override suspend fun createMenuDoc(
        folderUri: Uri,
        language: String,
        filename: String,
        dishList: List<DocDishData>,
        context: Context
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
                    run1.setText("${item.name} - ${item.price}")
                    run1.addBreak()
                    val run2 = p1.createRun()
                    run2.setText(item.description)

                    item.imageByteArray?.let { imageData ->
                        val cell2 = table.getRow(index).getCell(1)
                        val p2 = cell2.addParagraph()
                        val run3 = p2.createRun()
                        val width = Units.toEMU(120.0)
                        val height = Units.toEMU(80.0)
                        ByteArrayInputStream(imageData).use { inputStream ->
                            run3.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", width, height)
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

                Log.d("DAVAI", "Document created")
            } catch (e: Exception) {
                Log.d("DAVAI", "exception $e")
                continuation.resumeWithException(e)
            } finally {
                document.close()
            }
        }
    }

}