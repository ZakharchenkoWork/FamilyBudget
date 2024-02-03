package com.faigenbloom.familybudget.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.faigenbloom.familybudget.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class GalleryPhotoContract(activity: Activity) :
    ActivityResultContract<GalleryRequest, GalleryResponse?>() {
    val visualMedia = ActivityResultContracts.PickVisualMedia()
    var lastInput: GalleryRequest? = null
    private val outputDirectory: File?
    private val contentResolver = activity.contentResolver

    init {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, activity.getString(R.string.app_name)).apply { mkdirs() }
        }
        outputDirectory = try {
            if (mediaDir != null && mediaDir.exists()) mediaDir else activity.filesDir
        } catch (e: IllegalArgumentException) {
            Log.e("Canceled picking photo", e.toString())
            null
        }
    }

    override fun createIntent(context: Context, input: GalleryRequest): Intent {
        lastInput = input
        return Intent.createChooser(
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            },
            "Please select...",
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): GalleryResponse {
        intent?.data?.let {
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(
                    visualMedia.parseResult(resultCode, intent) ?: "".toUri(),
                ),
            )
            val outputFile = File(
                outputDirectory,
                SimpleDateFormat(
                    "yyyy-MM-dd-HH-mm-ss-SSS",
                    Locale.US,
                ).format(System.currentTimeMillis()) + ".jpg",
            )
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(outputFile))
            val galleryResponse = GalleryResponse(
                id = lastInput?.id,
                reason = lastInput?.reason,
                uri = outputFile.toUri(),
            )
            lastInput = null
            return galleryResponse
        }
        return GalleryResponse(null, null, null)
    }
}

data class GalleryRequest(
    val id: String?,
    val reason: String?,
)

data class GalleryResponse(
    val id: String?,
    val reason: String?,
    val uri: Uri?,
)
