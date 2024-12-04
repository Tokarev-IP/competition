package com.example.catalog.content.domain.functions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.catalog.content.domain.data.PdfMenuData

interface CreateMenuPdfFileInterface {

    fun createMenuPdfDocument(
        pdfMenuDataList: List<PdfMenuData>,
        pageWidth: Int = 595,
        pageHeight: Int = 842,
        textWidth: Int = 395,
        margin: Float = 50f,
        marginBetweenItems: Float = 30f,
        marginBetweenText: Float = 5f,
        marginBetweenImageAndText: Float = 15f,
        marginBetweenSectionAndText: Float = 25f,
        sectionTextSize: Float = 18f,
        nameTextSize: Float = 14f,
        priceTextSize: Float = 12f,
        descriptionTextSize: Float = 10f,
        startPageIndex: Int = 1,
    ): PdfDocument {
        val pdfDocument = PdfDocument()

        val paint = Paint()
        val sectionPaint = Paint().apply { textSize = sectionTextSize }
        val namePaint = Paint().apply { textSize = nameTextSize }
        val pricePaint = Paint().apply { textSize = priceTextSize }
        val descriptionPaint = Paint().apply { textSize = descriptionTextSize }
        val descriptionTextPaint = TextPaint(descriptionPaint)

        var currentY = margin
        var currentPageIndex = startPageIndex

        val availableImageWidth =
            pageWidth - textWidth - 2 * margin - marginBetweenImageAndText // Image space

        val pageInfo =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageIndex).create()

        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas

        for (menuSection in pdfMenuDataList) {

            for (i in menuSection.pdfDishData.indices) {
                //Values of texts in the menu
                val sectionText = menuSection.sectionData.name
                val nameText = menuSection.pdfDishData[i].name
                val priceText = "$ ${menuSection.pdfDishData[i].price}"
                val descriptionText = menuSection.pdfDishData[i].description

                // Creating static layout for description text
                val descriptionTextLayout = StaticLayout.Builder
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

                var totalHeight =
                    descriptionTextLayout.height + namePaint.textSize +
                            pricePaint.textSize + marginBetweenText * 2
                if (i == 0) //add section text before first dish name
                    totalHeight += sectionPaint.textSize + marginBetweenText

                // Checking the image size and scale it
                var bitmap: Bitmap? = menuSection.pdfDishData[i].bitmap

                bitmap?.let { it ->
                    bitmap = createScaledBitmap(
                        it,
                        availableImageWidth,
                        totalHeight
                    )
                }

                // If block takes more space than page, create new page
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

                // Creating text and drawing it
                if (i == 0) {
                    canvas.drawText(sectionText, margin + textWidth / 4, currentY, sectionPaint)
                    currentY += sectionPaint.textSize + marginBetweenText
                }
                canvas.drawText(nameText, margin, currentY, namePaint)
                currentY += namePaint.textSize + marginBetweenText
                canvas.drawText(priceText, margin, currentY, pricePaint)
                currentY += pricePaint.textSize + marginBetweenText

                // Creating description text
                canvas.save()
                canvas.translate(margin, currentY)
                descriptionTextLayout.draw(canvas)
                canvas.restore()
                currentY += descriptionTextLayout.height

                // If there is an image, draw it on the right side of the text, centered vertically
                bitmap?.let {
                    val imageX =
                        margin + textWidth + marginBetweenImageAndText
                    val imageY =
                        currentY - totalHeight

                    canvas.drawBitmap(it, imageX, imageY, paint)
                }

                // Update currentY for the next block
                currentY += marginBetweenItems // Add margin between blocks
                if (i == menuSection.pdfDishData.size - 1)
                    currentY += marginBetweenSectionAndText
            }
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