package com.ids.inpoint.custom.player

import com.ids.inpoint.custom.player.MediaPlayerImpl
import com.ids.inpoint.custom.player.interfaces.VideoViewContract
import java.lang.ref.WeakReference

class VideoViewPresenter (videoViewView: VideoViewContract.View) : VideoViewContract.Presenter {

    private val view = WeakReference(videoViewView)

    private val mediaPlayer = MediaPlayerImpl()

    override fun deactivate() {
    }

    override fun getPlayer() = mediaPlayer

    override fun play(url: String) = mediaPlayer.play(url)

    override fun releasePlayer() = mediaPlayer.releasePlayer()

    override fun setMediaSessionState(isActive: Boolean) {
        mediaPlayer.setMediaSessionState(isActive)
    }
}