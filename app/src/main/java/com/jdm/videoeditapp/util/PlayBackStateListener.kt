package com.jdm.videoeditapp.util

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.jdm.videoeditapp.ui.event.PlayState
import com.jdm.videoeditapp.ui.media.PlayViewModel

class PlayBackStateListener(private val viewModel: PlayViewModel): Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_IDLE -> {
                viewModel.playState.value = PlayState.Uninitialized
            }
            Player.STATE_BUFFERING -> {
                viewModel.playState.value = PlayState.Loading
            }
            Player.STATE_READY -> {
                viewModel.playState.value = PlayState.Ready
            }
            Player.STATE_ENDED -> {
                viewModel.playState.value = PlayState.Finish
            }
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (playWhenReady) {
            viewModel.playState.value = PlayState.Playing
        } else {
            viewModel.playState.value = PlayState.Pause
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        val cause: Throwable? = error.cause
        if (cause is HttpDataSource.HttpDataSourceException) {

        }
    }


    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
    }

    companion object {
        val TAG1 = "PlayBackStateListener"
        val TAG2 = "onPlayBackStateChanged"
        val TAG3 = "onPlayWhenReadyChanged"
        val TAG4 = "onMediaItemTransition"
    }
}