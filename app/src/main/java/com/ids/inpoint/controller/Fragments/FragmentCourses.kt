package com.ids.inpoint.controller.Fragments


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Video.Thumbnails.VIDEO_ID
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment

import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityYoutube
import com.ids.inpoint.controller.Activities.PlayerActivity
import com.ids.inpoint.controller.Adapters.AdapterFollowers
import com.ids.inpoint.controller.Adapters.AdapterReviews
import com.ids.inpoint.controller.Adapters.AdapterStudents
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication

import com.ids.inpoint.model.Followers
import com.ids.inpoint.model.ResponseVimeo
import com.ids.inpoint.model.Reviews
import com.ids.inpoint.model.Students
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientVimeo
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.fragment_fragment_profile.*
import kotlinx.android.synthetic.main.tab_courses_introduction.*
import kotlinx.android.synthetic.main.tab_courses_review.*
import kotlinx.android.synthetic.main.tab_courses_student.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*


class FragmentCourses : Fragment() ,RVOnItemClickListener , Player.EventListener{
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
       try {
           if (playbackState == Player.STATE_BUFFERING)
               progressBar.visibility = View.VISIBLE
           else if (playbackState == Player.STATE_READY)
               progressBar.visibility = View.INVISIBLE
       }catch (e:Exception){}
    }

    var selectedTab=0


    var isFullScreen=false

    override fun onItemClicked(view: View, position: Int) {
    }
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private lateinit var youTubePlayerFragment :YouTubePlayerSupportFragment
    private lateinit var youtubePlayer :YouTubePlayer
    private var playbackPosition = 0L
     private var dashUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
     //private var dashUrl = "https://gcs-vimeo.akamaized.net/exp=1572851921~acl=%2A%2F352450961.mp4%2A~hmac=10e9ba1cb6089b311e26ea89ac0e8dd93f7ca9d906b62e5627b901db1cef546c/vimeo-prod-skyfire-std-us/01/4778/4/123891946/352450961.mp4"

    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    lateinit var adapterReviews: AdapterReviews
    var arrayReviews= java.util.ArrayList<Reviews>()

    lateinit var adapterStudents: AdapterStudents
    var arrayStudents= java.util.ArrayList<Students>()

    var isShowVideo=true
    private var mFullScreenButton: FrameLayout? = null
    private var mFullScreenIcon: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_courses, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        videoTitle.setOnClickListener {
           // startActivity(Intent(activity,ActivityYoutube::class.java))
            startActivity(Intent(activity,PlayerActivity::class.java))
        }

        // initializeExoplayer()
        //setVimeo()
       //  setVimeoNative()
       setYoutubefragment()
    }

    override fun onDetach() {
        try{releaseExoplayer()}catch (e:Exception){}
        super.onDetach()
    }

    private fun init(){

        btIntro.setOnClickListener{
            selectedTab=0
            resetTabs()
            tab_intro.visibility=View.VISIBLE
            btIntro.setBackgroundResource(R.drawable.left_corners_secondary_filled)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                btIntro.setTextColor(ContextCompat.getColor(this!!.activity!!, com.ids.inpoint.R.color.white_trans));
            else
                btIntro.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.white_trans))

        }

        btOutline.setOnClickListener{
            selectedTab=1
            resetTabs()
            tab_intro.visibility=View.VISIBLE
            btOutline.setBackgroundResource(R.drawable.rectangular_secondary)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                btOutline.setTextColor(ContextCompat.getColor(this!!.activity!!, com.ids.inpoint.R.color.white_trans));
            else
                btOutline.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.white_trans))
        }

        btReview.setOnClickListener{
            selectedTab=2
            resetTabs()
            tab_reviews.visibility=View.VISIBLE
            btReview.setBackgroundResource(R.drawable.rectangular_secondary)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                btReview.setTextColor(ContextCompat.getColor(this!!.activity!!, com.ids.inpoint.R.color.white_trans));
            else
                btReview.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.white_trans))

        }

        btStudent.setOnClickListener{
            selectedTab=3
            resetTabs()
            tab_student.visibility=View.VISIBLE
            btStudent.setBackgroundResource(R.drawable.right_corners_secondary_filled)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                btStudent.setTextColor(ContextCompat.getColor(this!!.activity!!, com.ids.inpoint.R.color.white_trans));
            else
                btStudent.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.white_trans))
        }

        setReviews()
        setStudents()


        linearShowVideo.setOnClickListener{
            if(isShowVideo){
                rlVideo.visibility=View.GONE
                isShowVideo=false
                pausePlayer()

            }else{
                rlVideo.visibility=View.VISIBLE
                isShowVideo=true
                startPlayer()
            }
        }


        linearShare.setOnClickListener{
            AppHelper.share(activity!!,"video title","video description")
        }

        linearToolbar.background.alpha=255
    }

    private fun resetTabs(){
        tab_intro.visibility=View.GONE
        tab_reviews.visibility=View.GONE
        tab_student.visibility=View.GONE

        btIntro.setBackgroundResource(R.drawable.left_corners_secondary)
        btOutline.setBackgroundResource(R.drawable.right_border_secondary)
        btReview.setBackgroundResource(R.drawable.top_buttom_border_secondary)
        btStudent.setBackgroundResource(R.drawable.right_corners_secondary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btIntro.setTextColor(ContextCompat.getColor(this.activity!!, com.ids.inpoint.R.color.black));
            btOutline.setTextColor(ContextCompat.getColor(this.activity!!, com.ids.inpoint.R.color.black));
            btReview.setTextColor(ContextCompat.getColor(this.activity!!, com.ids.inpoint.R.color.black));
            btStudent.setTextColor(ContextCompat.getColor(this.activity!!, com.ids.inpoint.R.color.black));
        }
        else {
            btIntro.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.black))
            btOutline.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.black))
            btReview.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.black))
            btStudent.setTextColor(activity!!.resources.getColor(com.ids.inpoint.R.color.black))

        }
    }


    private fun setReviews(){
/*        arrayReviews.add(Reviews("1","bob","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","lorem ipsum lorem ipsum lorem"))
        arrayReviews.add(Reviews("2","Mohammad","https://content-static.upwork.com/uploads/2014/10/01073427/profilephoto1.jpg","lorem ipsum lorem ipsum lorem ipsum ipsum demo review"))
        arrayReviews.add(Reviews("3","Maarouf","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","lorem ipsum lorem "))
        adapterReviews= AdapterReviews(arrayReviews,this)
         val glm = GridLayoutManager(activity, 1)
        rvReviews.adapter=adapterReviews
        rvReviews.layoutManager=glm*/
    }




    private fun setStudents(){
        arrayStudents.add(Students("1","Hilal karam","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","Beirut,Lebanon","o3"))
        arrayStudents.add(Students("2","Diana Kh","https://content-static.upwork.com/uploads/2014/10/01073427/profilephoto1.jpg","Jordan","04"))
        arrayStudents.add(Students("3","Emad Ha.","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","Pakistan","05"))
        arrayStudents.add(Students("4","Hilal karam","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","Beirut,Lebanon","o3"))
        arrayStudents.add(Students("5","Diana Kh","https://content-static.upwork.com/uploads/2014/10/01073427/profilephoto1.jpg","Jordan","04"))
        arrayStudents.add(Students("6","Emad Ha.","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","Pakistan","05"))

     /*   adapterStudents= AdapterStudents(arrayStudents,this)
        val glm = GridLayoutManager(activity, 3)
        rvStudents.adapter=adapterStudents
        rvStudents.layoutManager=glm*/
    }




    private fun initializeExoplayer() {
        simpleExoplayer = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(activity),
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )

        val controlView = simpleExoPlayerView!!.findViewById<PlaybackControlView>(R.id.exo_controller)

        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon)
        mFullScreenButton = controlView.findViewById<FrameLayout>(R.id.exo_fullscreen_button)
        mFullScreenButton!!.setOnClickListener{
            startActivity(Intent(activity,PlayerActivity::class.java)
                .putExtra(AppConstants.CURRENT_POSITION,simpleExoplayer.currentPosition)
                .putExtra(AppConstants.CURRENT_INDEX,simpleExoplayer.currentWindowIndex)
                .putExtra(AppConstants.VIDEO_URL,dashUrl)
            )
        }

        prepareExoplayer()
        simpleExoPlayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)


    }

    private fun releaseExoplayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }


    private fun prepareExoplayer() {
        val uri = Uri.parse(dashUrl)
        val mediaSource = buildMediaSource(uri)
        simpleExoplayer.prepare(mediaSource)
    }

    private fun pausePlayer() {
        try {
            simpleExoplayer.playWhenReady = false
            simpleExoplayer.playbackState
        }catch (e:Exception){}
    }

    private fun startPlayer() {
        try {
            simpleExoplayer.playWhenReady = true
            simpleExoplayer.playbackState
        }catch (e:Exception){}
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
                DefaultHttpDataSourceFactory("ua", bandwidthMeter))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }
    }



    //vimeo

    private fun setVimeoEmbeded(){
        wvVimeo.visibility=View.VISIBLE
        simpleExoPlayerView.visibility=View.GONE
        progressBar.visibility=View.GONE
        val vimeoVideo =
            "<html><body>" +
           "<div style='padding:56.25% 0 0 0;position:relative;'><iframe src='https://player.vimeo.com/video/6370469?autoplay=1&loop=1' style='position:absolute;top:0;left:0;width:100%;height:100%;' frameborder='0' allow='autoplay; fullscreen' allowfullscreen></iframe></div><script src='https://player.vimeo.com/api/player.js'></script>'"+
        "<p><a href='https://vimeo.com/6370469'>Canon 7D Sample Video</a> from <a href='https://vimeo.com/gizmodo'>Gizmodo</a> on <a href='https://vimeo.com'>Vimeo</a>.</p>'"+
        "<p>1080p@24fps, H.264</p>"+
                    "</body></html>"


        wvVimeo.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(webView: WebView, request: WebResourceRequest): Boolean {

                webView.loadUrl(request.url.toString())
                return true
            }
        }
        val webSettings = wvVimeo.settings
        webSettings.javaScriptEnabled = true
        wvVimeo.loadData(vimeoVideo, "text/html", "utf-8")
    }



    private fun getVideoInfo(){
        MyApplication.videoId="123891946"
        RetrofitClientVimeo.client?.create(RetrofitInterface::class.java)?.getVideoInfo("123891946")?.enqueue(object :
            Callback<ResponseVimeo> {
            override fun onResponse(call: Call<ResponseVimeo>, response: Response<ResponseVimeo>) {
           // Toast.makeText(activity,"aaaaaaaaaa",Toast.LENGTH_LONG).show()
                dashUrl= response.body()!!.request!!.files!!.progressive!![0].url.toString()

               try{ initializeExoplayer()}catch (E:Exception){}

            }
            override fun onFailure(call: Call<ResponseVimeo>, throwable: Throwable) {

            }
        })
    }



    private fun setVimeoNative(){
        getVideoInfo()
    }





    //youtube fragment
    private fun setYoutubefragment(){
        simpleExoPlayerView.visibility=View.GONE
          youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
              var transaction = childFragmentManager.beginTransaction();
           transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(getString(R.string.GOOGLE_API_KEY), object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    youtubePlayer=player
                    youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    youtubePlayer.loadVideo("2Dpd_8n3A5U")
                //    youtubePlayer.setShowFullscreenButton(false)
                    youtubePlayer.setOnFullscreenListener { isFullScreen=true }

                    youtubePlayer.play()
                    try{progressBar.visibility=View.GONE}catch (e:Exception){}
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
                // YouTube error
                val errorMessage = error.toString()
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                Log.d("errorMessage:", errorMessage)
            }
        })


    }

    fun disableFullScreen(){
        if (isFullScreen) {
            // if fullscreen, set fullscreen false
           try {
               youtubePlayer.setFullscreen(false)
               isFullScreen=false
           }catch (e:Exception){}

        }
    }


}