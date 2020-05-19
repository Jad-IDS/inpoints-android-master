package com.ids.inpoint.custom.player

import android.content.Context
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.ids.inpoint.custom.player.interfaces.MediaPlayer
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class MediaPlayerImpl: MediaPlayer {

    companion object {
        private const val TAG = "MediaPlayerTag"
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    private fun initializePlayer() {

        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)


        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            renderersFactory, trackSelector, loadControl)


    }

    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(context, TAG)
        mediaSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        mediaSession.setMediaButtonReceiver(null)

        stateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_FAST_FORWARD or
                        PlaybackStateCompat.ACTION_REWIND
            )

        mediaSession.setPlaybackState(stateBuilder.build())

        mediaSession.setCallback(SessionCallback())

        mediaSession.isActive = true
    }

    private inner class SessionCallback : MediaSessionCompat.Callback() {

        private val SEEK_WINDOW_MILLIS = 10000

        override fun onPlay() {
            exoPlayer.playWhenReady = true
        }

        override fun onPause() {
            exoPlayer.playWhenReady = false
        }

        override fun onRewind() {
            exoPlayer.seekTo(exoPlayer.currentPosition - SEEK_WINDOW_MILLIS)
        }

        override fun onFastForward() {
            exoPlayer.seekTo(exoPlayer.currentPosition + SEEK_WINDOW_MILLIS)
        }
    }

    override fun play(url: String) {


        //1
        val userAgent = Util.getUserAgent(context, context.getString(com.ids.inpoint.R.string.app_name))

        // Default parameters, except allowCrossProtocolRedirects is true
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            userAgent,
            null /* listener */,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true /* allowCrossProtocolRedirects */
        )

        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            null /* listener */,
            httpDataSourceFactory
        )

        //2
        val mediaSource = ExtractorMediaSource
            .Factory(dataSourceFactory)
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(url))
        //3
        exoPlayer.prepare(mediaSource)
        //4
        exoPlayer.playWhenReady = true
    }

    override fun getPlayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        initializeMediaSession()
        return exoPlayer
    }

    override fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    override fun setMediaSessionState(isActive: Boolean) {
        mediaSession.isActive = isActive
    }
}