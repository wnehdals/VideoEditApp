package com.jdm.videoeditapp.model

import android.graphics.Bitmap
import android.net.Uri

class Photo(
    id: Long = 0L,
    uri: Uri? = Uri.EMPTY,
    dateAddedSecond: Long = 0L,
    thumbNail: Bitmap? = null,
    val isGif: Boolean = false
): BaseMedia(id, uri, dateAddedSecond, thumbNail) {
}