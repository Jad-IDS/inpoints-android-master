/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.ids.inpoint.controller.Activities

import android.annotation.SuppressLint
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoRendererEventListener
import com.google.android.youtube.player.internal.m
import com.ids.inpoint.R
import com.ids.inpoint.utils.AppConstants
import kotlinx.android.synthetic.main.exo_simple_player_view.*
import java.lang.Exception


/**
 * A fullscreen activity to play audio or video streams.
 */
class PlayerActivity : AppCompatActivity() {

    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null

    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady = true
    private var videoUrl=""
    private var mFullScreenIcon: ImageView? = null
    private var componentListener: ComponentListener? = null
    private var mFullScreenButton: FrameLayout? = null
    private var mExoPlayerFullscreen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.video_view)

        componentListener = ComponentListener()
    }

    public override fun onStart() {
        super.onStart()

       try{ videoUrl=intent.getStringExtra(AppConstants.VIDEO_URL)}catch (e:Exception){videoUrl=""}
        try{currentWindow=intent.getIntExtra(AppConstants.CURRENT_INDEX,0)}catch (e:Exception){currentWindow=0}
        try{playbackPosition=intent.getLongExtra(AppConstants.CURRENT_POSITION,0)}catch (E:Exception){}

        if (Util.SDK_INT > 23) {
            initializePlayer()
        }

       mFullScreenIcon!!.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_fullscreen_skrink))
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            // a factory to create an AdaptiveVideoTrackSelection
            val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(BANDWIDTH_METER)

            player = ExoPlayerFactory.newSimpleInstance(
                    DefaultRenderersFactory(this),
                    DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    DefaultLoadControl())


            val controlView = playerView!!.findViewById<PlaybackControlView>(R.id.exo_controller)

            mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon)
            mFullScreenButton = controlView.findViewById<FrameLayout>(R.id.exo_fullscreen_button)
            mFullScreenButton!!.setOnClickListener{
                    closeFullscreenDialog()
            }

            player!!.addListener(componentListener)
            player!!.addVideoDebugListener(componentListener as VideoRendererEventListener?)
            player!!.addAudioDebugListener(componentListener as AudioRendererEventListener?)

        }

        playerView!!.player = player

        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)

        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri)
        player!!.prepare(mediaSource, true, false)
    }

    private fun openFullscreenDialog(){
        Toast.makeText(applicationContext,"open",Toast.LENGTH_LONG).show()
    }

    private fun closeFullscreenDialog(){
        Toast.makeText(applicationContext,"close",Toast.LENGTH_LONG).show()
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.removeListener(componentListener)
            player!!.setVideoListener(null)
            player!!.removeVideoDebugListener(componentListener as VideoRendererEventListener?)
            player!!.removeAudioDebugListener(componentListener as AudioRendererEventListener?)
            player!!.release()
            player = null
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    companion object {

        private val BANDWIDTH_METER = DefaultBandwidthMeter()
    }



    private fun buildMediaSource(uri: Uri): MediaSource {

        val userAgent = "exoplayer-codelab"

        if (uri.lastPathSegment.contains("mp3") || uri.lastPathSegment.contains("mp4")) {
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.lastPathSegment.contains("m3u8")) {
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }
    }


}
