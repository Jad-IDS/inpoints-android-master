package com.ids.inpoint.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.ids.inpoint.R

const val YOUTUBE_VIDEO_ID = "2Dpd_8n3A5U"
const val YOUTUBE_PLAYLIST_ID = "PLTHOlLMWEwVy2ZNmdrwRlRlVfZ8fiR_ms"

class ActivityYoutube : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val TAG = "ActivityYoutube"
    private val DIALOG_REQUEST_CODE = 1

    private val playerView by lazy { YouTubePlayerView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = layoutInflater.inflate(R.layout.activity_youtube, null) as ConstraintLayout
        setContentView(layout)

        playerView.layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout.addView(playerView)

        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        Log.d(TAG, "onInitializationSuccess: provider: ${provider?.javaClass}, player: ${player?.javaClass}")
        Toast.makeText(this, "Initialized YoutubePlayer successfully", Toast.LENGTH_LONG).show()

        if (!wasRestored) {
            player?.loadVideo(YOUTUBE_VIDEO_ID)
            player?.setPlayerStateChangeListener(playerStateListener)
            player?.setPlaybackEventListener(playerPlaybackListener)
        } else {
            player?.play()
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, result: YouTubeInitializationResult?) {
        if (result?.isUserRecoverableError == true) {
            result.getErrorDialog(this, DIALOG_REQUEST_CODE).show()
        } else {
            Toast.makeText(this, "The YouTubePlayer initialization error $(result)", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == DIALOG_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
        }
    }

    private val playerPlaybackListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onPlaying() {
            Toast.makeText(this@ActivityYoutube, "OnPlaying", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            Toast.makeText(this@ActivityYoutube, "OnPaused", Toast.LENGTH_SHORT).show()
        }

        override fun onSeekTo(p0: Int) {}

        override fun onBuffering(p0: Boolean) {}

        override fun onStopped() {
            Toast.makeText(this@ActivityYoutube, "OnStopped", Toast.LENGTH_SHORT).show()
        }
    }

    private val playerStateListener = object: YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            Toast.makeText(this@ActivityYoutube, "Ad started", Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {}

        override fun onVideoStarted() {
            Toast.makeText(this@ActivityYoutube, "Video started", Toast.LENGTH_SHORT).show()
        }

        override fun onLoaded(p0: String?) {}

        override fun onVideoEnded() {
            Toast.makeText(this@ActivityYoutube, "Video ended", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {}

    }
}
