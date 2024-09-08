package com.example.catalog.content.domain.functions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.catalog.content.domain.data.PdfDishData

interface CreateMenuPdfFileInterface {

    fun createMenuPdfDocument(
        pdfDishList: List<PdfDishData>,
        pageWidth: Int = 595,
        pageHeight: Int = 842,
        textWidth: Int = 395,
        margin: Float = 50f,
        marginBetweenItems: Float = 30f,
        marginBetweenText: Float = 5f,
        marginBetweenImageAndText: Float = 15f,
        nameTextSize: Float = 14f,
        priceTextSize: Float = 12f,
        descriptionTextSize: Float = 10f,
        startPageIndex: Int = 1,
    ): PdfDocument {
        val pdfDocument = PdfDocument()

        val paint = Paint()
        val namePaint = Paint().apply { textSize = nameTextSize }
        val pricePaint = Paint().apply { textSize = priceTextSize }
        val descriptionPaint = Paint().apply { textSize = descriptionTextSize }
        val descriptionTextPaint = TextPaint(descriptionPaint)

        var currentY = margin
        var currentPageIndex = startPageIndex

        val availableImageWidth =
            pageWidth - textWidth - 2 * margin - marginBetweenImageAndText // Пространство для изображения

        val pageInfo =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageIndex).create()

        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas

        for (dish in pdfDishList) {
            val nameText = dish.name
            val priceText = "${dish.price} $"
            val descriptionText = dish.description

            // Создаем StaticLayout для текста
            val descriptionLayout = StaticLayout.Builder
                .obtain(
                    descriptionText,
                    0,
                    descriptionText.length,
                    descriptionTextPaint,
                    textWidth,
                )
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

            val totalHeight =
                descriptionLayout.height + namePaint.textSize + pricePaint.textSize + marginBetweenText * 2 // Включаем в расчет высоту названия и цены

            // Проверяем размер изображения и масштабируем его
            var bitmap: Bitmap? = dish.bitmap

            bitmap?.let { it ->
                bitmap = createScaledBitmap(
                    it,
                    availableImageWidth,
                    totalHeight
                )
            }

            // Если блок не помещается на текущую страницу, создаем новую
            if (currentY + totalHeight > pageHeight - margin) {
                pdfDocument.finishPage(page)
                currentPageIndex++

                val newPageInfo = PdfDocument.PageInfo.Builder(
                    pageWidth,
                    pageHeight,
                    currentPageIndex
                ).create()

                canvas.drawColor(Color.WHITE)
                canvas.save()
                canvas.restore()

                page = pdfDocument.startPage(newPageInfo)
                canvas = page.canvas
                currentY = margin
            }

            // Рисуем текст названия и цены
            canvas.drawText(nameText, margin, currentY, namePaint)
            currentY += namePaint.textSize + marginBetweenText
            canvas.drawText(priceText, margin, currentY, pricePaint)
            currentY += pricePaint.textSize + marginBetweenText

            // Рисуем описание текста
            canvas.save()
            canvas.translate(margin, currentY)
            descriptionLayout.draw(canvas)
            canvas.restore()
            currentY += descriptionLayout.height

            // Если изображение есть, рисуем его справа от текста, центрируя по вертикали
            bitmap?.let {
                val imageX =
                    margin + textWidth + marginBetweenImageAndText // Позиция изображения справа от текста с отступом 20px
                val imageY =
                    currentY - totalHeight // Центрируем изображение по высоте текста

                canvas.drawBitmap(it, imageX, imageY, paint)
            }

            // Обновляем текущую позицию Y для следующего блока
            currentY += marginBetweenItems // Добавляем отступ после блока
        }

        pdfDocument.finishPage(page)

        return pdfDocument
    }

    private fun createScaledBitmap(bitmap: Bitmap, width: Float, height: Float): Bitmap {
        val imageHeight = bitmap.height.toFloat()
        val imageWidth = bitmap.width.toFloat()

        val scaleFactor =
            minOf(width / imageWidth, height / imageHeight)

        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            (imageWidth * scaleFactor).toInt(),
            (imageHeight * scaleFactor).toInt(),
            true
        )

        return scaledBitmap
    }

}