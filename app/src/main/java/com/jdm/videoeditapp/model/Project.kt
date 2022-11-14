package com.jdm.videoeditapp.model

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.exoplayer2.MediaItem

data class Project(
    var sourceMedia: SourceMedia,
    var thumbnailList: MutableList<Bitmap> = mutableListOf<Bitmap>(),
    var mediaItem: MediaItem? = null
)
