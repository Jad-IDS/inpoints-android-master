package com.ids.inpoint.controller.Activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterLessons
import com.ids.inpoint.controller.Adapters.AdapterReviews
import com.ids.inpoint.controller.Adapters.AdapterStudents
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.RequestReview
import com.ids.inpoint.model.ResponseVimeo
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.activity_inside_course.*
import kotlinx.android.synthetic.main.fragment_courses.btIntro
import kotlinx.android.synthetic.main.fragment_courses.btOutline
import kotlinx.android.synthetic.main.fragment_courses.btReview
import kotlinx.android.synthetic.main.fragment_courses.btStudent
import kotlinx.android.synthetic.main.fragment_courses.linearShare
import kotlinx.android.synthetic.main.fragment_courses.linearShowVideo
import kotlinx.android.synthetic.main.fragment_courses.progressBar
import kotlinx.android.synthetic.main.fragment_courses.rlVideo
import kotlinx.android.synthetic.main.fragment_courses.simpleExoPlayerView
import kotlinx.android.synthetic.main.fragment_courses.videoTitle
import kotlinx.android.synthetic.main.fragment_courses.wvVimeo
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.loading.loading
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.tab_courses_introduction.*
import kotlinx.android.synthetic.main.tab_courses_outline.*
import kotlinx.android.synthetic.main.tab_courses_publications.*
import kotlinx.android.synthetic.main.tab_courses_review.*
import kotlinx.android.synthetic.main.tab_courses_student.*
import kotlinx.android.synthetic.main.tab_courses_student.rvStudents
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.linearToolbar
import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityInsideCourse : AppCompactBase(),RVOnItemClickListener  , Player.EventListener{
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
        }catch (e: Exception){}
    }

    var selectedTab=0
    private var skip=0
    private var take=100


    var isFullScreen=false
    private var isRegistered=false

    private var dialog: Dialog? = null
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
    var arrayReviews= java.util.ArrayList<ResponseReview>()

    lateinit var adapterStudents: AdapterStudents
    var arrayStudents= java.util.ArrayList<ResponseCourseRegisteredUsers>()

    lateinit var adapterLessons: AdapterLessons
    var arrayLessons= java.util.ArrayList<ResponseLesson>()

    var isShowVideo=true
    private var mFullScreenButton: FrameLayout? = null
    private var mFullScreenIcon: ImageView? = null
    private var reviewId=0
    private var userHasReview=false

    lateinit var fragmentManager: FragmentManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_course)
        init()
        videoTitle.setOnClickListener {
            // startActivity(Intent(activity,ActivityYoutube::class.java))

            startActivity(Intent(this,PlayerActivity::class.java))
        }

        // initializeExoplayer()
        //setVimeo()
        //  setVimeoNative()
      //  setYoutubefragment()


    }


    private fun init(){

        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager

        btBack.setOnClickListener{super.onBackPressed()}
        getCourseDetails()
        getCourseLessons()
        getCourseReviews()
        getCourseStudents()
        //getPublicationByCourseId()
        checkCourseRegistration()



Log.wtf("userlogin",MyApplication.userLoginInfo.id.toString())

        btIntro.setOnClickListener{
            selectedTab=0
            resetTabs()
            tab_intro.visibility=View.VISIBLE
            btIntro.setBackgroundResource(R.drawable.left_corners_secondary_filled)
            AppHelper.setTextColor(this,btIntro,R.color.white_trans)
        }

        btOutline.setOnClickListener{
            selectedTab=1
            resetTabs()
            tab_outline.visibility=View.VISIBLE
            btOutline.setBackgroundResource(R.drawable.rectangular_secondary)
            AppHelper.setTextColor(this,btOutline,R.color.white_trans)
        }

        btReview.setOnClickListener{
            selectedTab=2
            resetTabs()
            tab_reviews.visibility=View.VISIBLE
            btReview.setBackgroundResource(R.drawable.rectangular_secondary)
            AppHelper.setTextColor(this,btReview,R.color.white_trans)

        }

        btStudent.setOnClickListener{
            selectedTab=3
            resetTabs()
            tab_student.visibility=View.VISIBLE
            btStudent.setBackgroundResource(R.drawable.rectangular_secondary)
            AppHelper.setTextColor(this,btStudent,R.color.white_trans)
        }




        btPublications.setOnClickListener{
            selectedTab=4
            resetTabs()
            tab_publication.visibility=View.VISIBLE
            btPublications.setBackgroundResource(R.drawable.right_corners_secondary_filled)
            AppHelper.setTextColor(this,btPublications,R.color.white_trans)
        }


        // setStudents()


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
            AppHelper.share(this,"video title","video description")
        }

        linearToolbar.background.alpha=255
        btIntro.performClick()

        btWriteReview.setOnClickListener{showReviewDialog(0,"")}

    }




    override fun onItemClicked(view: View, position: Int) {
         if(view.id==R.id.linearItemLessons){
             MyApplication.selectedLesson=adapterLessons.courses[position]
             startActivity(Intent(this, ActivityInsideLesson::class.java).putExtra(AppConstants.ACTION_REGISTER,adapterLessons.courses[position].name))
         }
        else if(view.id==R.id.btEditReview){
            reviewId=adapterReviews.Reviews[position].ReviewId!!
            showReviewDialog(adapterReviews.Reviews[position].rating!!,adapterReviews.Reviews[position].text!!)
        }else if(view.id==R.id.btDeleteReview){
            deleteConfirm(this,adapterReviews.Reviews[position].ReviewId!!)
        }else if(view.id==R.id.itemUser){
            try{
             MyApplication.selectedPost= ResponsePost()
             MyApplication.selectedPost.userId=adapterStudents.Students[position].userId
             MyApplication.selectedPost.image=adapterStudents.Students[position].profileImage
             MyApplication.selectedPost.userName=adapterStudents.Students[position].userName
             startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
         }catch (e:Exception){}

         }



 /*        else if (view.id==R.id.linearReviews){
             if(adapterReviews.Reviews[position].userId==MyApplication.userLoginInfo.id){
                 reviewId=adapterReviews.Reviews[position].ReviewId!!
                try{
                 showReviewDialog(adapterReviews.Reviews[position].rating!!,adapterReviews.Reviews[position].text!!)
             }catch (e:Exception){
                    showReviewDialog(0,"")

                }

             }
         }*/
    }



    fun deleteConfirm(c: Activity, id: Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_confirmation))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                deleteStartupUser(id)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun deleteStartupUser(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteCourseReview(id)?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    loading.visibility=View.GONE
                    try{getCourseReviews()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }



    private fun resetTabs(){
        tab_intro.visibility=View.GONE
        tab_reviews.visibility=View.GONE
        tab_student.visibility=View.GONE
        tab_outline.visibility=View.GONE
        tab_publication.visibility=View.GONE

        if(MyApplication.languageCode==AppConstants.LANG_ENGLISH){
              btIntro.setBackgroundResource(R.drawable.left_corners_secondary)
              btOutline.setBackgroundResource(R.drawable.right_border_secondary)
              btReview.setBackgroundResource(R.drawable.right_border_secondary)
              btPublications.setBackgroundResource(R.drawable.right_corners_secondary)
        }else{
             btIntro.setBackgroundResource(R.drawable.right_corners_secondary)
             btOutline.setBackgroundResource(R.drawable.left_border_secondary)
             btReview.setBackgroundResource(R.drawable.left_border_secondary)
             btPublications.setBackgroundResource(R.drawable.left_corners_secondary)
        }


        btStudent.setBackgroundResource(R.drawable.top_buttom_border_secondary)
        AppHelper.setTextColor(this,btIntro,R.color.black)
        AppHelper.setTextColor(this,btOutline,R.color.black)
        AppHelper.setTextColor(this,btReview,R.color.black)
        AppHelper.setTextColor(this,btStudent,R.color.black)
        AppHelper.setTextColor(this,btPublications,R.color.black)

    }


    private fun setReviews(body: ArrayList<ResponseReview>?) {
        arrayReviews.clear()
        arrayReviews.addAll(body!!)
       // arrayReviews.add(Reviews("1","bob","https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg-760x506.jpg","lorem ipsum lorem ipsum lorem"))
        adapterReviews= AdapterReviews(arrayReviews,this,AppConstants.REVIEW_TYPE_COURSE)
        val glm = GridLayoutManager(this, 1)
        rvReviews.adapter=adapterReviews
        rvReviews.layoutManager=glm

     /*   if(reviewId==0) {
            for (i in body.indices) {
                if (body[i].ReviewId == MyApplication.userLoginInfo.id) {
                    reviewId = body[i].ReviewId!!
                    break
                }
            }
        }*/


        if(arrayReviews.size>0)
            tvReviewCount.text = arrayReviews.size.toString()+" "+getString(R.string.reviews)
        else
            tvReviewCount.visibility=View.GONE


        for (i in arrayReviews.indices){
            if(arrayReviews[i].userId==MyApplication.userLoginInfo.id){
                userHasReview=true
                break
            }
        }
        if(userHasReview)
            btWriteReview.visibility=View.GONE
        else
            btWriteReview.visibility=View.VISIBLE

    }






    fun showReviewDialog(rating:Int,text:String) {

        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_review)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        var rbCourse = dialog!!.findViewById<View>(R.id.rbCourse) as RatingBar
        var etReview = dialog!!.findViewById<View>(R.id.etReview) as EditText
        var btSave = dialog!!.findViewById<View>(R.id.btSave) as LinearLayout
        if(text.isNotEmpty())
            etReview.setText(text)
        rbCourse.rating=rating.toFloat()
        btSave.setOnClickListener{
            saveReview(MyApplication.selectedOnlineCourse.id!!,rbCourse.rating.toInt(),etReview.text.toString())
        }
        var  btCancel = dialog!!.findViewById<View>(R.id.btCancel) as LinearLayout
        btCancel.setOnClickListener{dialog!!.dismiss()}
        dialog!!.show();

    }




    private fun initializeExoplayer() {
        simpleExoplayer = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )

        val controlView = simpleExoPlayerView!!.findViewById<PlaybackControlView>(R.id.exo_controller)

        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon)
        mFullScreenButton = controlView.findViewById<FrameLayout>(R.id.exo_fullscreen_button)
        mFullScreenButton!!.setOnClickListener{
            startActivity(Intent(this,PlayerActivity::class.java)
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
                DefaultHttpDataSourceFactory("ua", bandwidthMeter)
            )
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
        var transaction = supportFragmentManager.beginTransaction();
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
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
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




    private fun getCourseDetails(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getOnlineCoursesDetails(MyApplication.selectedOnlineCourse.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseOnlineCourseDetail>> {
                override fun onResponse(call: Call<ArrayList<ResponseOnlineCourseDetail>>, response: Response<ArrayList<ResponseOnlineCourseDetail>>) {
                    try{
                        onCourseDetailsRetrieved(response.body())
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseOnlineCourseDetail>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })

    }

    private fun onCourseDetailsRetrieved(response: ArrayList<ResponseOnlineCourseDetail>?) {
        try{ tvCourseName.text= response!![0].name}catch (e:Exception){}
        try{ ivUserInstructor.setImageResource(R.drawable.avatar)}catch (e:Exception){}
        try{tvInstructorName.text= response!![0].instructorName}catch (e:Exception){}
        ctvInstructorLevel.visibility=View.GONE
        try{tvCourseCode.text=response!![0].code}catch (e:Exception){}
        tvCourseDuration.visibility=View.GONE
        try{tvInstructorDetails.text=response!![0].categoriesName}catch (e:Exception){}
        try{ wvIntro.loadData(response!![0].courseDescription, "text/html; charset=utf-8", "UTF-8")}catch (e:Exception){}


        try{linearAboutInstructor.setOnClickListener{
/*            MyApplication.selectedPost= ResponsePost()
            MyApplication.selectedPost.userId=response!![0].instructorId
            MyApplication.selectedPost.image=response[0].instructorImage
            MyApplication.selectedPost.userName=response[0].instructorName
            startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))*/
        }}catch (e:Exception){}

    }





    private fun saveReview(courseId:Int,rating:Int,text:String){
        var senddate = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())

       //  var review=RequestReview(reviewId,courseId,MyApplication.userLoginInfo.id,rating,false,text,senddate)
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveCourseReview(
                reviewId,courseId,MyApplication.userLoginInfo.id!!,rating,false,text,senddate
            )?.enqueue(object : Callback<RequestReview> {
                override fun onResponse(call: Call<RequestReview>, response: Response<RequestReview>) {
                    try{
                        dialog!!.dismiss()
                        getCourseReviews()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<RequestReview>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })

    }


    private fun getCourseLessons(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getCourseLessons(MyApplication.selectedOnlineCourse.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseLesson>> {
                override fun onResponse(call: Call<ArrayList<ResponseLesson>>, response: Response<ArrayList<ResponseLesson>>) {
                    try{
                        onCourseLessonsRetreived(response.body()!!)
                        //onResourcesRetrieved(response.body())
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseLesson>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getCourseReviews(){
        userHasReview=false
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getCourseReviews(MyApplication.selectedOnlineCourse.id!!,take,skip
            )?.enqueue(object : Callback<ArrayList<ResponseReview>> {
                override fun onResponse(call: Call<ArrayList<ResponseReview>>, response: Response<ArrayList<ResponseReview>>) {
                    try{
                        setReviews(response.body())
                        //onResourcesRetrieved(response.body())
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseReview>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun onCourseLessonsRetreived(response: ArrayList<ResponseLesson>){
        arrayLessons.clear()
        arrayLessons.addAll(response)
        adapterLessons= AdapterLessons(arrayLessons,this)
        val glm = GridLayoutManager(this, 1)
        rvLessons.adapter=adapterLessons
        rvLessons.layoutManager=glm




    }


    private fun getCourseStudents(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getRegisteredUsers(MyApplication.selectedOnlineCourse.id!!,take,skip
            )?.enqueue(object : Callback<ArrayList<ResponseCourseRegisteredUsers>> {
                override fun onResponse(call: Call<ArrayList<ResponseCourseRegisteredUsers>>, response: Response<ArrayList<ResponseCourseRegisteredUsers>>) {
                    try{
                        setStudents(response.body()!!)
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseCourseRegisteredUsers>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun setStudents(response: ArrayList<ResponseCourseRegisteredUsers>){
        arrayStudents.clear()
        arrayStudents.addAll(response)
        adapterStudents= AdapterStudents(arrayStudents,this)
        val glm = GridLayoutManager(this, 3)
        rvStudents.adapter=adapterStudents
        rvStudents.layoutManager=glm
        try{tvStudentsCount.text = arrayStudents.size.toString()+getString(R.string.std_applied)}catch (e:Exception){}
    }



    private fun getPublicationByCourseId(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPublicationByCourseId(MyApplication.selectedOnlineCourse.id!!
            )?.enqueue(object : Callback<ResponseCoursePublication> {
                override fun onResponse(call: Call<ResponseCoursePublication>, response: Response<ResponseCoursePublication>) {
                    try{

                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ResponseCoursePublication>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }





    private fun saveCourseRegistration(action:String){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveCourseRegistration(MyApplication.selectedOnlineCourse.id!!,action
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    loading.visibility=View.GONE
                    try{
                        if(response.body()!!){
                            if(isRegistered){

                                btRegisterCourse.text=getString(R.string.take_course)

                            }else{
                                btRegisterCourse.text=getString(R.string.remove_course)
                            }
                            isRegistered=!isRegistered
                           try{ scroll.fullScroll(View.FOCUS_UP)}catch (e:Exception){}
                        }
                      //  setStudents(response.body()!!)
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })
    }


    private fun checkCourseRegistration(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.checkCourseRegistered(MyApplication.selectedOnlineCourse.id!!
            )?.enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    try{
                        if(response.body()==1){
                            isRegistered=true
                            btRegisterCourse.text=getString(R.string.remove_course)
                        }else{
                            isRegistered=false
                            btRegisterCourse.text=getString(R.string.take_course)
                        }
                        setRegisterAction()

                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<Int>, throwable: Throwable) {
                }
            })
    }


    private fun setRegisterAction(){
        linearRegisterCourse.setOnClickListener{
            if(isRegistered)
                saveCourseRegistration(AppConstants.ACTION_CANCEL)
            else
                saveCourseRegistration(AppConstants.ACTION_REGISTER)
        }
    }


}






