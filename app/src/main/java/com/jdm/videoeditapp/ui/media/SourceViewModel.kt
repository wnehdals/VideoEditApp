package com.jdm.videoeditapp.ui.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdm.videoeditapp.base.BaseViewModel
import com.jdm.videoeditapp.model.MediaType
import com.jdm.videoeditapp.model.SourceMedia
import com.jdm.videoeditapp.repository.SourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository
): BaseViewModel() {
    private val _sourceData = MutableLiveData<MutableList<SourceMedia>>(mutableListOf())
    val sourceData: LiveData<MutableList<SourceMedia>> get() = _sourceData

    val totalSourceList = mutableListOf<SourceMedia>()
    val videoSourceList = mutableListOf<SourceMedia>()
    val photoSourceList = mutableListOf<SourceMedia>()
    val gifSourceList = mutableListOf<SourceMedia>()

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


}