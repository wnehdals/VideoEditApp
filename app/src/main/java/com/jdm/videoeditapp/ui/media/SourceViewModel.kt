package com.jdm.videoeditapp.ui.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdm.videoeditapp.model.Video
import com.jdm.videoeditapp.repository.SourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository
): ViewModel() {
    private val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->  }
    private val _videoData = MutableLiveData<MutableList<Video>>(mutableListOf())
    val videoData: LiveData<MutableList<Video>> get() = _videoData



    fun getVideoList() {
        viewModelScope.launch(Dispatchers.IO + ceh ) {
            sourceRepository.getVideoList()
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        _videoData.value = it
                    }
                }

        }
    }

}