package com.jdm.videoeditapp.ui.event

sealed class PlayState {
    object Uninitialized: PlayState()

    object Loading: PlayState()

    object Ready: PlayState()

    object Playing: PlayState()

    object Pause: PlayState()

    object Finish: PlayState()
}