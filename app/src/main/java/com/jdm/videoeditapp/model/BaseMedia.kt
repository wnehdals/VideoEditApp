package com.jdm.videoeditapp.model

import android.graphics.Bitmap
import android.net.Uri

open class BaseMedia(
    var id: Long = 0L,
    var uri: Uri? = Uri.EMPTY,
    var dateAddedSecond: Long = 0L,
    var thumbNail: Bitmap? = null
): Comparable<BaseMedia> {

    override fun compareTo(other: BaseMedia): Int {
        return (this.dateAddedSecond - other.dateAddedSecond).toInt()
    }
}