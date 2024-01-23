package com.faigenbloom.famillyspandings.common.coders

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import qrcode.QRCode

fun generateQR(text: String, frontColor: Color, backColor: Color): Bitmap {
    val helloWorld = QRCode.ofSquares()
        .withColor(frontColor.toArgb())
        .withBackgroundColor(backColor.toArgb())
        .withSize(25)
        .build(text)

    return helloWorld.render().nativeImage() as Bitmap
}

fun painterQR(text: String, frontColor: Color, backColor: Color) = BitmapPainter(
    generateQR(
        text = text,
        frontColor = frontColor,
        backColor = backColor,
    ).asImageBitmap(),
)
