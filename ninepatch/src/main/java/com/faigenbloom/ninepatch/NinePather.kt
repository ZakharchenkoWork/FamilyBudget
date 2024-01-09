package com.faigenbloom.ninepatch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.NinePatch
import android.graphics.Paint
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
fun painterResourceNinePath(@DrawableRes id: Int): Painter {
    return BitmapFactory.decodeResource(LocalContext.current.resources, id)?.let { bitmap ->
        NinePathPainter(bitmap)
    } ?: painterResource(id)
}

class NinePathPainter(private val bitmap: Bitmap) : Painter() {
    override val intrinsicSize: Size =
        Size(bitmap.width.toFloat(), bitmap.height.toFloat())

    @RequiresApi(Build.VERSION_CODES.S)
    override fun DrawScope.onDraw() {
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawPatch(
                NinePatch(bitmap, bitmap.ninePatchChunk),
                canvas.nativeCanvas.clipBounds,
                Paint(),
            )
        }
    }
}
