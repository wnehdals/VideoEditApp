package com.jdm.videoeditapp.ui.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseViewModel
import com.jdm.videoeditapp.model.MediaType
import com.jdm.videoeditapp.model.Project
import com.jdm.videoeditapp.model.SourceMedia
import com.jdm.videoeditapp.repository.SourceRepository
import com.jdm.videoeditapp.ui.event.UIEvent
import com.jdm.videoeditapp.util.ScreenSizeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository
): BaseViewModel() {
    private val _sourceData = MutableLiveData<MutableList<SourceMedia>>(mutableListOf())
    val sourceData: LiveData<MutableList<SourceMedia>> get() = _sourceData

    private val _thumbnailData = MutableLiveData<MutableList<Bitmap>>(mutableListOf())
    val thumbnailData: LiveData<MutableList<Bitmap>> get() = _thumbnailData

    private val _uiEvent = MutableSharedFlow<UIEvent>(replay = 0)
    val uiEvent: SharedFlow<UIEvent>
        get() = _uiEvent


    val totalSourceList = mutableListOf<SourceMedia>()
    val videoSourceList = mutableListOf<SourceMedia>()
    val photoSourceList = mutableListOf<SourceMedia>()
    val gifSourceList = mutableListOf<SourceMedia>()

    var project: Project? = null
    var cropWidth = 0
    var divideFrameCnt = 0

    fun emitUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
    fun clearProject() {
        project = null
        cropWidth = 0
        divideFrameCnt = 0
    }
    fun getVideoList() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO + ceh ) {
            sourceRepository.getVideoList()
                .zip(sourceRepository.getPhotoList()) { videos, photos ->
                    totalSourceList.addAll(videos)
                    totalSourceList.addAll(photos)
                    totalSourceList
                }
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        Collections.sort(it)
                        sourceFilter(it)
                        totalSourceList.addAll(it)
                        _sourceData.value = it
                    }
                }

        }
    }
    fun sourceFilter(list: MutableList<SourceMedia>) {
        var videos = list.filter { it.type == MediaType.VIDEO }
        var photos = list.filter { it.type == MediaType.PHOTO }
        var gifs = list.filter { it.type == MediaType.GIF }
        videoSourceList.addAll(videos)
        photoSourceList.addAll(photos)
        gifSourceList.addAll(gifs)
    }
    fun setProject(sourceMedia: SourceMedia) {
        project = Project(sourceMedia = sourceMedia, mediaItem = createMediaItem(sourceMedia))
    }

    fun setThumbnailList(context: Context) {
        if (project == null)
            return

        viewModelScope.launch(ceh) {
            _loading.value = true
            val thumbnailList = LinkedList<Bitmap>()
            withContext(Dispatchers.Default) {

                val mediaMetadataRetriever = MediaMetadataRetriever()

                mediaMetadataRetriever.setDataSource(context, project!!.sourceMedia.uri)
                val originWidth: Int =
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                        ?.toInt() ?: 0
                val originHeight: Int =
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                        ?.toInt() ?: 0
                divideFrameCnt = if (originHeight > originWidth) {
                        8
                } else {
                        4
                }
                cropWidth =
                        ScreenSizeUtil.getMetrics(context).widthPixels / divideFrameCnt
                var cropHeight = cropWidth * originHeight / originWidth
                val interval = 1000L

                for (i in 0L until project!!.sourceMedia.duration step interval) {
                    var bitmap = mediaMetadataRetriever.getFrameAtTime(
                        i * 1000L,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    )
                    bitmap?.let {
                        bitmap = Bitmap.createScaledBitmap(bitmap!!, cropWidth, cropHeight, false)
                    }
                    thumbnailList.add(bitmap!!)

                }

                for(i in 0 until divideFrameCnt / 2) {
                    var bitmap = getDummyBitmap(context)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, cropWidth, cropHeight, false)
                    thumbnailList.addFirst(bitmap)
                    thumbnailList.addLast(bitmap)
                }

                mediaMetadataRetriever.release()
                project!!.thumbnailList.addAll(thumbnailList)
            }

            _loading.value = false
            _thumbnailData.value = project!!.thumbnailList
        }
    }

    private fun createMediaItem(video: SourceMedia): MediaItem {
        return MediaItem.Builder()
            .setUri(video.uri)
            .setMediaId("${video.id}")
            .build()
    }

    private fun getDummyBitmap(context: Context): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_dummy)
        val canvas = Canvas()
        if (drawable == null)
            return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }


}
