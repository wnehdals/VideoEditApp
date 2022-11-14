package com.jdm.videoeditapp.ui.media

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.jdm.videoeditapp.base.BaseViewModel
import com.jdm.videoeditapp.ui.event.PlayState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(): BaseViewModel() {
    val currentPosition = MutableLiveData<Long>(0)
    val playState = MutableLiveData<PlayState>(PlayState.Uninitialized)
    val isFull = MutableLiveData<Boolean>(false)
}