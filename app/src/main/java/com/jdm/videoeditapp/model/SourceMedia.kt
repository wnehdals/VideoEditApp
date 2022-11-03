package com.jdm.videoeditapp.model

import android.graphics.Bitmap
import android.net.Uri

open class SourceMedia(
    var id: Long = 0L,
    var uri: Uri? = Uri.EMPTY,
    var dateAddedSecond: Long = 0L,
    var thumbNail: Bitmap? = null,
    val duration: Int = 0,
    var type: MediaType = MediaType.UNKOWN
): Comparable<SourceMedia> {

    override fun compareTo(other: SourceMedia): Int {
        return (other.dateAddedSecond - this.dateAddedSecond).toInt()
    }
}