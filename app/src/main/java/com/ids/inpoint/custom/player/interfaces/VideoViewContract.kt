package com.ids.inpoint.custom.player.interfaces

import com.ids.inpoint.custom.player.interfaces.MediaPlayer

interface VideoViewContract {

    interface Presenter {

        fun deactivate()

        fun getPlayer(): MediaPlayer

        fun play(url: String)

        fun releasePlayer()

        fun setMediaSessionState(isActive: Boolean)
    }

    interface View

}