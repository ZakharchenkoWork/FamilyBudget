package com.faigenbloom.famillyspandings.comon

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class GalleryPhotoContract : ActivityResultContract<GalleryRequest, GalleryResponse?>() {
    val visualMedia = ActivityResultContracts.PickVisualMedia()
    var lastInput: GalleryRequest? = null
    override fun createIntent(context: Context, input: GalleryRequest): Intent {
        lastInput = input
        return visualMedia.createIntent(
            context,
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): GalleryResponse {
        val galleryResponse = GalleryResponse(
            id = lastInput?.id,
            reason = lastInput?.reason,
            uri = visualMedia.parseResult(resultCode, intent),
        )
        lastInput = null
        return galleryResponse
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
