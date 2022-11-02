package com.jdm.videoeditapp.model

import android.graphics.Bitmap
import android.net.Uri

class Video(
    id: Long = 0L,
    uri: Uri? = Uri.EMPTY,
    dateAddedSecond: Long = 0L,
    thumbNail: Bitmap? = null,
    val duration: Int
): BaseMedia(id, uri, dateAddedSecond, thumbNail) {
}