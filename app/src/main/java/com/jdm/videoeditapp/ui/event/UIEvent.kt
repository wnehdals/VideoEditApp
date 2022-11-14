package com.jdm.videoeditapp.ui.event

sealed class UIEvent {
    object GoToVideoEditFragment: UIEvent()
    object PlayVideo: UIEvent()
    object StopVideo: UIEvent()
}
