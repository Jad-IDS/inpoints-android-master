package com.ids.inpoint.controller.Activities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.Fragments.FragmentImageBottomSheet
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.events
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.activity_inside_comment.*



import kotlinx.android.synthetic.main.fragment_comment_bottomsheet.linearWriteComment
import kotlinx.android.synthetic.main.fragment_comment_bottomsheet.rvComments
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.item_events_body.*
import kotlinx.android.synthetic.main.item_news_feed.*
import kotlinx.android.synthetic.main.item_news_feed_body.*
import kotlinx.android.synthetic.main.item_news_feed_body.ctvTextPost
import kotlinx.android.synthetic.main.item_news_feed_body.youtube_layout
import kotlinx.android.synthetic.main.item_news_feed_body_new.*
import kotlinx.android.synthetic.main.item_news_feed_comment.*
import kotlinx.android.synthetic.main.item_news_feed_info.*
import kotlinx.android.synthetic.main.item_news_feed_info.ctvLevel
import kotlinx.android.synthetic.main.item_news_feed_info.ctvName
import kotlinx.android.synthetic.main.item_news_feed_info.ctvTimePost
import kotlinx.android.synthetic.main.item_news_feed_info.ivUser
import kotlinx.android.synthetic.main.item_subcomment.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar_general.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityInsideComment : AppCompactBase(),RVOnItemClickListener {

    var adapterComments: AdapterComments?=null
    var adapterResources: AdapterResources?=null
    var adapterFund: AdapterResources?=null
    lateinit var fragmentManager: FragmentManager
    var arrayComments= java.util.ArrayList<ResponseComments>()
    var comment1:ResponseComments?=null
    var PostId=0
    var date=""
    var skip=0
    var pageSize=10
    var commentId=""
    private var isLoading = false
    var commentPosition=0
    private var dialog: Dialog? = null
    private lateinit var youTubePlayerFragment :YouTubePlayerSupportFragment
    private lateinit var youtubePlayer :YouTubePlayer
    var rvVerifyCategories:RecyclerView ?= null
    var btVerify: LinearLayout ?= null
    var arraypostCategories= java.util.ArrayList<ResponseCategory>()
    var adapterCategories: AdapterCategories?=null
    var isParticipant=false
    var rvPopUpUsers: RecyclerView ?= null
    private var adapterPopLikers:AdapterPopupUsers?=null
    private var arrayLikers = java.util.ArrayList<ResponseFollowers>()

    var arrayMedia= java.util.ArrayList<Media>()
    var adapterMedia: AdapterNewsFeedMedia?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_comment)
        initialize()
        setPagination()

        if(MyApplication.selectedPost.id!=null) {
            getPostById()
            setPost()
       }
       // setComments()
    }


    private fun initialize(){

        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager



        tvToolbarTitle.text = ""

        if(MyApplication.selectedPost.id!=null)
           PostId=MyApplication.selectedPost.id!!
        else{
            try{PostId=intent.getStringExtra(AppConstants.POST_ID).toInt()}catch (e:Exception){}
            getSelectedPost()
        }



        btBack.setOnClickListener{
          super.onBackPressed()
        }


        ivSend.setOnClickListener {


            if (etComment.text.toString().isNotEmpty()) {
                var senddate = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

                if (commentId == "") {
                    arrayComments.add(
                        ResponseComments(
                            -1,
                            PostId,
                            0,
                            etComment.text.toString(),
                            0,
                            MyApplication.userLoginInfo.id,
                            MyApplication.userLoginInfo.userName,
                            MyApplication.userLoginInfo.image,
                            senddate,
                            "now",
                            arrayListOf(),
                            false,
                            false
                        )
                    )
                } else {
                    arrayComments[commentPosition].replies!!.add(
                        ResponseSubCommrent(
                            -1, commentId.toInt(),
                            etComment.text.toString(),
                            MyApplication.userLoginInfo.id,
                            MyApplication.userLoginInfo.userName,
                            MyApplication.userLoginInfo.image,
                            senddate,
                            "now",
                            false
                        )
                    )
                }

                sendComment(commentId, PostId!!, etComment.text.toString())
                adapterComments!!.notifyDataSetChanged()
                etComment.setText("")
                var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etComment.windowToken, 0);
                commentId = ""
                commentPosition = 0


            }
        }


        linearLikers.setOnClickListener{
            showUserDialog(AppConstants.POPUP_TYPE_LIKERS,PostId)
        }
    }



    override fun onItemClicked(view: View, position: Int) {
         if(view.id==R.id.btSponsorResources){

             if(adapterResources!!.resources[position].Sponsored==1) {
                 adapterResources!!.resources[position].Sponsored = 0
                 deleteResourcesSponsor(adapterResources!!.resources[position].id!!)
             }
             else {
                 saveResourcesSponsor(adapterResources!!.resources[position].id!!)
                 adapterResources!!.resources[position].Sponsored = 1
             }
             adapterResources!!.notifyDataSetChanged()

         }else if(view.id==R.id.btSponsorFunding){

             if(adapterFund!!.resources[position].Sponsored==1) {
                 adapterFund!!.resources[position].Sponsored = 0
                 deleteFundingSponsor(adapterFund!!.resources[position].id!!)
             }
             else {
                 saveFundingSponsor(adapterFund!!.resources[position].id!!)
                 adapterFund!!.resources[position].Sponsored = 1
             }
             adapterFund!!.notifyDataSetChanged()

         }  else if(view.id==R.id.btFollowLikers){
             adapterPopLikers!!.arrayUsers[position].isFollowed=!adapterPopLikers!!.arrayUsers[position].isFollowed!!
             adapterPopLikers!!.notifyDataSetChanged()
             followUser(adapterPopLikers!!.arrayUsers[position].userId!!,position,AppConstants.POPUP_TYPE_LIKERS)
         }else if(view.id==R.id.linearMedia){
             if(MyApplication.selectedPost.medias!![position].fileType==AppConstants.MEDIA_TYPE_IMAGE)
                  showImage(AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![position].fileName.toString())
         }

    }



    private fun getPostById(){
       if(adapterComments==null)
          loading.visibility=View.VISIBLE
        if(date=="")
            date = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostById(PostId,
                skip,
                date,
                true
            )?.enqueue(object : Callback<ArrayList<ResponseComments>> {
                override fun onResponse(call: Call<ArrayList<ResponseComments>>, response: Response<ArrayList<ResponseComments>>) {
                    try{
                        onPostByIdRetrieved(response)
                    }catch (E:Exception){
                        Log.wtf("get_post_error", E.toString())

                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseComments>>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun onPostByIdRetrieved(responseComments: Response<ArrayList<ResponseComments>>){
        try {
            date = responseComments.headers().get("lastdate").toString()
        }catch (e:java.lang.Exception){}

        loading.visibility=View.GONE



        try{
            if(adapterComments==null) {
                arrayComments=responseComments.body()!!
                adapterComments = AdapterComments(arrayComments, this, 0,true)
                val glm = GridLayoutManager(this, 1)
                rvComments.adapter = adapterComments
                rvComments.layoutManager = glm
            }else{

                val position = adapterComments!!.comments.size
                arrayComments.addAll(responseComments.body()!!)
                adapterComments!!.notifyItemInserted(position)
            }

        }catch (e:Exception){}



        try {
            date = responseComments.headers().get("lastdate").toString()
            if(date=="null")
                isLoading=true
        }catch (e:java.lang.Exception){
            isLoading=true
        }
    }





    private fun sendComment(commentId:String,postId:Int,comment:String){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.sendComment(
                JsonParameters(0,postId,commentId,comment,AppConstants.CONST_TYPE_SEND_COMMENT)

            )?.enqueue(object : Callback<ResponseSaveComment> {
                override fun onResponse(call: Call<ResponseSaveComment>, response: Response<ResponseSaveComment>) {
                    try{
                        onCommentSent(response)
                    }catch (E:Exception){


                    }
                }
                override fun onFailure(call: Call<ResponseSaveComment>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun onCommentSent(responseComments: Response<ResponseSaveComment>){

    }
    
    
    
    private fun setPost() {
        MyApplication.selectedPost.settingsViewVisible=false
        linearWriteComment.visibility=View.VISIBLE
        rvComments.visibility=View.VISIBLE
        try{
            AppHelper.setRoundImageResize(this,ivUser, AppConstants.IMAGES_URL+MyApplication.selectedPost.image!!,false)}catch (e: java.lang.Exception){}

/*
        try{
            if(MyApplication.selectedPost.medias!![0].fileName.toString().isNotEmpty()) {
                ivImagePost.visibility=View.VISIBLE
                AppHelper.setImage(this, ivImagePost, AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![0].fileName!!)
            }else{
                ivImagePost.visibility=View.GONE
            }
        }catch (e: java.lang.Exception){
            ivImagePost.visibility=View.GONE
        }
*/


        if(MyApplication.userLoginInfo.subType==AppConstants.VERIFICATION_TYPE_VERIFIER)
            LinearVerified.visibility=View.VISIBLE
        else
            LinearVerified.visibility=View.GONE

        LinearVerified.setOnClickListener{
            showVerifyPopup(PostId)
        }

        linearNewsFeedBody.visibility=View.GONE


        if(MyApplication.selectedPost.divType==AppConstants.TYPE_POST) {

            linearEventsBody.visibility=View.GONE
            linearShow.visibility=View.GONE
            if(MyApplication.selectedPost.medias!!.isNotEmpty()){
                linearNewsFeedBodyNew.visibility=View.VISIBLE
                var grid=1
                if(MyApplication.selectedPost.medias!!.size==1)
                    grid=1
                else
                    grid=2
                arrayMedia.clear()
                arrayMedia.addAll(MyApplication.selectedPost.medias!!)
                adapterMedia= AdapterNewsFeedMedia(arrayMedia,this,1)
                val glm = GridLayoutManager(this, grid)
                rvNewsFeedMedia!!.adapter=adapterMedia
                rvNewsFeedMedia!!.layoutManager=glm


            }else{
                linearNewsFeedBodyNew.visibility=View.GONE
            }




 /*           try {
                when {
                    MyApplication.selectedPost.medias!!.size >= 4 -> {
                        linearImages.visibility = View.VISIBLE
                        linearImageTop.visibility = View.VISIBLE
                        linearImageBottom.visibility = View.VISIBLE
                        rlImagePost.visibility = View.VISIBLE
                        rlImagePost2.visibility = View.VISIBLE
                        rlImagePost3.visibility = View.VISIBLE
                        rlImagePost4.visibility = View.VISIBLE


                        if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost.visibility=View.VISIBLE
                            youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![0].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost.visibility=View.GONE
                                youtube_layout.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout,MyApplication.selectedPost.medias!![0].fileName!!)
                            }


                        if(MyApplication.selectedPost.medias!![1].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost2.visibility=View.VISIBLE
                            youtube_layout2.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost2, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![1].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![1].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost2.visibility=View.GONE
                                youtube_layout2.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout2,MyApplication.selectedPost.medias!![1].fileName!!)
                            }


                        if(MyApplication.selectedPost.medias!![2].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost3.visibility=View.VISIBLE
                            youtube_layout3.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost3, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![2].fileName!!)
                        }else if(MyApplication.selectedPost.medias!![2].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                            ivImagePost3.visibility=View.GONE
                            youtube_layout3.visibility=View.VISIBLE
                            setYoutube(this,R.id.youtube_layout3,MyApplication.selectedPost.medias!![2].fileName!!)
                        }

                        if(MyApplication.selectedPost.medias!![3].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost4.visibility=View.VISIBLE
                            youtube_layout4.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost4, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![3].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![3].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost4.visibility=View.GONE
                                youtube_layout4.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout4,MyApplication.selectedPost.medias!![3].fileName!!)
                            }
                    }
                    MyApplication.selectedPost.medias!!.size == 3 -> {
                        linearImages.visibility = View.VISIBLE
                        linearImageTop.visibility = View.VISIBLE
                        linearImageBottom.visibility = View.VISIBLE
                        rlImagePost.visibility = View.VISIBLE
                        rlImagePost2.visibility = View.VISIBLE
                        rlImagePost3.visibility = View.VISIBLE
                        rlImagePost4.visibility = View.GONE
                        // ivImagePost4.visibility = View.GONE

                        if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost.visibility=View.VISIBLE
                            youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![0].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost.visibility=View.GONE
                                youtube_layout.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout,MyApplication.selectedPost.medias!![0].fileName!!)
                            }


                        if(MyApplication.selectedPost.medias!![1].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost2.visibility=View.VISIBLE
                            youtube_layout2.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost2, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![1].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![1].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost2.visibility=View.GONE
                                youtube_layout2.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout2,MyApplication.selectedPost.medias!![1].fileName!!)
                            }


                        if(MyApplication.selectedPost.medias!![2].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost3.visibility=View.VISIBLE
                            youtube_layout3.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost3, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![2].fileName!!)
                        }else if(MyApplication.selectedPost.medias!![2].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                            ivImagePost3.visibility=View.GONE
                            youtube_layout3.visibility=View.VISIBLE
                            setYoutube(this,R.id.youtube_layout3,MyApplication.selectedPost.medias!![2].fileName!!)
                        }
                    }
                    MyApplication.selectedPost.medias!!.size == 2 -> {
                        linearImages.visibility = View.VISIBLE
                        linearImageTop.visibility = View.VISIBLE
                        linearImageBottom.visibility = View.GONE
                        rlImagePost.visibility = View.VISIBLE
                        rlImagePost2.visibility = View.VISIBLE


                        if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost.visibility=View.VISIBLE
                            youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![0].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost.visibility=View.GONE
                                youtube_layout.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout,MyApplication.selectedPost.medias!![0].fileName!!)
                            }


                        if(MyApplication.selectedPost.medias!![1].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost2.visibility=View.VISIBLE
                            youtube_layout2.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost2, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![1].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![1].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost2.visibility=View.GONE
                                youtube_layout2.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout2,MyApplication.selectedPost.medias!![1].fileName!!)
                            }


                    }
                    MyApplication.selectedPost.medias!!.size == 1 -> {
                        linearImages.visibility = View.VISIBLE
                        linearImageTop.visibility = View.VISIBLE
                        linearImageBottom.visibility = View.GONE
                        rlImagePost.visibility = View.VISIBLE
                        rlImagePost2.visibility = View.GONE


                        if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            ivImagePost.visibility=View.VISIBLE
                            youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(this, ivImagePost, AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![0].fileName!!)
                        }else
                            if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                ivImagePost.visibility=View.GONE
                                youtube_layout.visibility=View.VISIBLE
                                setYoutube(this,R.id.youtube_layout,MyApplication.selectedPost.medias!![0].fileName!!)



                                //    (this as ActivityHome).window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



                            }

                        //  AppHelper.setImage(this, ivImagePost2, AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![1].fileName!!)
                    }
                    else -> {
                        linearImages.visibility = View.GONE
                        linearImageTop.visibility = View.GONE
                        linearImageBottom.visibility = View.GONE
                        rlImagePost.visibility = View.GONE
                        rlImagePost2.visibility = View.GONE

                    }
                }


            } catch (E: java.lang.Exception) {
            }
            */




        }else{
            linearNewsFeedBodyNew.visibility=View.GONE
            linearEventsBody.visibility=View.VISIBLE
            linearShow.visibility=View.VISIBLE

            try{ tvEventTitle.text=MyApplication.selectedPost.title }catch (e: java.lang.Exception){}
            try{ tvEventTime.text="Duration: "+ MyApplication.selectedPost.eventDates!![0].fromTime }catch (e: java.lang.Exception){}
            try{ tvEventDate.text=MyApplication.selectedPost.eventDates!![0].fromDate }catch (e: java.lang.Exception){}
            try{ tvEventLocation.text=MyApplication.selectedPost.location }catch (e: java.lang.Exception){}


            linearShowMoreData.visibility=View.VISIBLE
            linearShow.visibility=View.GONE


            try {
                if(MyApplication.selectedPost.medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                    ivImagePostEvents.visibility=View.VISIBLE
                    youtube_layout_event.visibility=View.GONE

                    AppHelper.setImageResizePost(
                        this,
                        ivImagePostEvents,
                        AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![0].fileName!!
                    )
                }else{
                    ivImagePostEvents.visibility=View.GONE
                    youtube_layout_event.visibility=View.VISIBLE
                    setYoutube(this,R.id.youtube_layout_event,MyApplication.selectedPost.medias!![0].fileName!!)

                }
            } catch (E: Exception) {
                rlImageEvent.visibility=View.GONE
            }
            try {
                ctvTextPostEvents.text = MyApplication.selectedPost.details
            } catch (e: java.lang.Exception) {
            }
            tvResourcesTitle.visibility = View.GONE
            rvResources.visibility = View.GONE
            tvFundingTitle.visibility = View.GONE
            rvFunding.visibility = View.GONE

           if(MyApplication.selectedPost.type==AppConstants.EVENT_TYPE_NEEDED) {
               linearFundingResources.visibility=View.VISIBLE
               getEventResources()
               getEventFunds()
           }else
               linearFundingResources.visibility=View.GONE


            //getParticipants()

            isParticipant=true
            checkParticipateButton()
            linearParticipate.setOnClickListener{
                participateToEvent()
            }

        }



        linearLike.setOnClickListener{
            like(PostId)
            MyApplication.selectedPost.liked=!MyApplication.selectedPost.liked!!
            setLikeImage()

            if(MyApplication.selectedPost.liked!!)
                MyApplication.selectedPost.likeNumber=MyApplication.selectedPost.likeNumber!!+1
            else
                if(MyApplication.selectedPost.likeNumber!!>=1)
                    MyApplication.selectedPost.likeNumber=MyApplication.selectedPost.likeNumber!!-1

          try{  ctvLikesCount.text = MyApplication.selectedPost.likeNumber.toString()}catch (e:Exception){}
        }

        linearComment.setOnClickListener{
            commentId=""
            etComment.requestFocus();
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }



        linearDots.visibility=View.VISIBLE
        try{ ctvName.text = MyApplication.selectedPost.userName }catch (e: java.lang.Exception){}
        try{ ctvLevel.text = MyApplication.selectedPost.level.toString() }catch (e: java.lang.Exception){}
        try{ ctvTextPost.text = MyApplication.selectedPost.title }catch (e: java.lang.Exception){}
        //  try{ ctvUni.text = MyApplication.selectedPost.university }catch (e:Exception){}
        try{ ctvTimePost.text = MyApplication.selectedPost.publishDate }catch (e: java.lang.Exception){}
        try{ ctvLikesCount.text = MyApplication.selectedPost.likeNumber.toString() }catch (e: java.lang.Exception){}
        try{ ctvCommentsCount.text = MyApplication.selectedPost.commentNumber.toString()+" "+getString(R.string.comments) }catch (e: java.lang.Exception){}


        setLikeImage()

        linearDots.visibility=View.GONE
        linearCorner.visibility=View.INVISIBLE


        if(MyApplication.selectedPost.arrayComments!=null){
            try{
                var adapterComments= AdapterComments(MyApplication.selectedPost.arrayComments!!,this,0,true)
                val glm = GridLayoutManager(this, 1)
                rvComments.adapter=adapterComments
                rvComments.layoutManager=glm}catch (e: java.lang.Exception){}
        }


        if(MyApplication.selectedPost.settingsViewVisible!!)
            linearPostSettings.visibility=View.VISIBLE
        else
            linearPostSettings.visibility=View.GONE



        ivImagePost.setOnClickListener{showImage(AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![0].fileName.toString())}
        ivImagePost2.setOnClickListener{showImage(AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![1].fileName.toString())}
        ivImagePost3.setOnClickListener{showImage(AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![2].fileName.toString())}
        ivImagePost4.setOnClickListener{showImage(AppConstants.IMAGES_URL+ MyApplication.selectedPost.medias!![3].fileName.toString())}

    }


    private fun showImage(url:String){
        MyApplication.imageType=AppConstants.BOTTOM_SHEET_IMAGE_TYPE_LINK
          MyApplication.selectedImage=url
          val bottomSheetFragment = FragmentImageBottomSheet()
         this.fragmentManager.let { bottomSheetFragment.show(it, bottomSheetFragment.tag) }
    }


    private fun setLikeImage(){
        if(MyApplication.selectedPost.liked!!)
            ivLike.setImageResource(R.drawable.like_on)
        else
            ivLike.setImageResource(R.drawable.like)
    }

    private fun setPagination() {


        svScroll.viewTreeObserver
            .addOnScrollChangedListener {
                if (svScroll.getChildAt(0).bottom <= svScroll.height + svScroll.scrollY) {
                    //scroll view is at bottom
                    if (!isLoading ) {
                        isLoading = true
                        skip+=pageSize
                        getPostById()
                    }
                }
            }


/*        svScroll.onscro.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (!isLoading &&   rvComments.layoutManager!!.childCount + (rvComments.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() >= rvComments.layoutManager!!.itemCount) {
                        isLoading = true
                        skip+=pageSize
                        getPostById()
                    }
                }
            }
        })*/

    }




    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }



    @Subscribe
    fun onMessageEvent(event: events) {

        if(event.message==AppConstants.DELETE_COMMENT){
            deleteComment(arrayComments[event.commentPosition].id!!)
            arrayComments.removeAt(event.commentPosition)
            adapterComments!!.notifyDataSetChanged()

        }else if(event.message==AppConstants.DELETE_SUB_COMMENT){
            deleteComment(arrayComments[event.commentPosition].id!!)
            arrayComments[event.commentPosition].replies!!.removeAt(event.subCommentPosition)
            adapterComments!!.notifyDataSetChanged()
        }else if(event.message==AppConstants.REPLY_COMMENT){
            commentId= arrayComments[event.commentPosition].id.toString()
            commentPosition=event.commentPosition
            etComment.requestFocus();

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }else if(event.message==AppConstants.MORE_REPLY_COMMENT){
            getAllReplies(adapterComments!!.comments[event.commentPosition].id!!,event.commentPosition)
        }

    }



    private fun deleteComment(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteComment(id
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{
                        loading.visibility=View.GONE
                    }catch (E:Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }



    private fun like(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.setLike(id
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun getAllReplies(commentId:Int,position: Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllReplies(commentId )?.enqueue(object : Callback<ArrayList<ResponseSubCommrent>> {
                override fun onResponse(call: Call<ArrayList<ResponseSubCommrent>>, response: Response<ArrayList<ResponseSubCommrent>>) {
                    try{
                        arrayComments[position].replies!!.clear()
                        arrayComments[position].replies!!.addAll(response.body()!!)
                        adapterComments!!.notifyDataSetChanged()
                    }catch (E:Exception){
                   }
                }
                override fun onFailure(call: Call<ArrayList<ResponseSubCommrent>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setYoutube(context:Context, youtube_layout_id:Int,videoId:String){

        val youtubeId = videoId.substring(videoId.lastIndexOf("/")+1)
       Log.wtf("videoid",youtubeId)

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        var transaction =     fragmentManager.beginTransaction();
        transaction.add(youtube_layout_id, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(getString(R.string.GOOGLE_API_KEY), object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    youtubePlayer=player
                    youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                   // youtubePlayer.loadVideo(youtubeId)
                    youtubePlayer.loadVideo(youtubeId)

                    //    youtubePlayer.setShowFullscreenButton(false)
                    youtubePlayer.setOnFullscreenListener {

                    }

                    youtubePlayer.play()
                    try{progressBar.visibility=View.GONE}catch (e: java.lang.Exception){}
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





    private fun getEventResources(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getEventResources(PostId
            )?.enqueue(object : Callback<ArrayList<ResponseEventFundRes>> {
                override fun onResponse(call: Call<ArrayList<ResponseEventFundRes>>, response: Response<ArrayList<ResponseEventFundRes>>) {
                    try{onResourcesRetrieved(response.body())}catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseEventFundRes>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun onResourcesRetrieved(response: ArrayList<ResponseEventFundRes>?){
        if(response!!.size>0){
            adapterResources= AdapterResources(response,this,AppConstants.TYPE_RESOURCES)
            val glm = GridLayoutManager(this, 1)
            rvResources.adapter=adapterResources
            rvResources.layoutManager=glm
            rvResources.visibility=View.VISIBLE
            tvResourcesTitle.visibility=View.VISIBLE
        }
    }


    private fun getEventFunds(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getEventFunds(PostId
            )?.enqueue(object : Callback<ArrayList<ResponseEventFundRes>> {
                override fun onResponse(call: Call<ArrayList<ResponseEventFundRes>>, response: Response<ArrayList<ResponseEventFundRes>>) {
                    try{onFundsRetrieved(response.body())}catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseEventFundRes>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun onFundsRetrieved(response: ArrayList<ResponseEventFundRes>?){
        if(response!!.size>0){
            adapterFund= AdapterResources(response,this,AppConstants.TYPE_FUNDING)
            val glm = GridLayoutManager(this, 1)
            rvFunding.adapter=adapterFund
            rvFunding.layoutManager=glm
            rvFunding.visibility=View.VISIBLE
            tvFundingTitle.visibility=View.VISIBLE
        }
    }

    private fun getParticipants(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getEventParticipant(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseParticipant>> {
                override fun onResponse(call: Call<ArrayList<ResponseParticipant>>, response: Response<ArrayList<ResponseParticipant>>) {
                    try{onParticipantsRetrieved(response.body())}catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseParticipant>>, throwable: Throwable) {

                }
            })

    }


    private fun onParticipantsRetrieved(response: ArrayList<ResponseParticipant>?){
        for (i in response!!.indices){
            if(response[i].userId==MyApplication.userLoginInfo.id){
                isParticipant=true
                break
            }
        }
        checkParticipateButton()
        linearParticipate.setOnClickListener{
          participateToEvent()
        }
    }



    private fun participateToEvent(){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.participateToEvent(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    loading.visibility=View.GONE
                     MyApplication.selectedPost.CanQuit=response.body()
                     checkParticipateButton()

                }
                override fun onFailure(call: Call<Int>, throwable: Throwable) {

                }
            })

    }



    private fun saveResourcesSponsor(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveResourcesSponsor(id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{}catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun saveFundingSponsor(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveFundingSponsor(id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{}catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })

    }


    private fun deleteResourcesSponsor(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteResourcesSponsor(id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{}catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun deleteFundingSponsor(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteFundingSponsor(id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{}catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })

    }

private fun checkParticipateButton(){


    if(MyApplication.selectedPost.CanQuit==AppConstants.EVENT_PARTICIPATE_PARTICIPATE) {
        linearParticipate.visibility=View.VISIBLE
        tvParticipate.text =getString(com.ids.inpoint.R.string.participate)
    }
    else  if(MyApplication.selectedPost.CanQuit==AppConstants.EVENT_PARTICIPATE_QUITE) {
        linearParticipate.visibility=View.VISIBLE
        tvParticipate.text =getString(com.ids.inpoint.R.string.quite)
    }
    else  if(MyApplication.selectedPost.CanQuit==AppConstants.EVENT_PARTICIPATE_PENDING){
        linearParticipate.visibility=View.VISIBLE
        tvParticipate.text =getString(com.ids.inpoint.R.string.pending)
    }
    else  if(MyApplication.selectedPost.CanQuit==AppConstants.EVENT_PARTICIPATE_HIDE) {
        linearParticipate.visibility=View.GONE
    }


}


    private fun getSelectedPost(){
Log.wtf("post_id_intent",intent.getStringExtra(AppConstants.POST_ID))
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostById(PostId

            )?.enqueue(object : Callback<ResponsePost> {
                override fun onResponse(call: Call<ResponsePost>, response: Response<ResponsePost>) {
                    try{
                       MyApplication.selectedPost=response.body()!!
                        getPostById()
                        setPost()
                        no_data.visibility=View.GONE
                    }catch (E:Exception){
                        no_data.visibility=View.VISIBLE
                        Log.wtf("get_post_error", E.toString())

                    }
                }
                override fun onFailure(call: Call<ResponsePost>, throwable: Throwable) {
                    no_data.visibility=View.VISIBLE
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            })

    }



    fun showVerifyPopup(postId:Int) {

        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_verify)
        dialog!!.setCancelable(true)
        rvVerifyCategories = dialog!!.findViewById<View>(R.id.rvVerifyCategories) as RecyclerView
        btVerify = dialog!!.findViewById<View>(R.id.btVerify) as LinearLayout
        arraypostCategories.clear()
        if(MyApplication.arrayCategories.size>0) {
            resetCategories()
            getcategoriesById(postId)
        }
        else {
            getcategories(true, postId)
        }

        dialog!!.show();

    }


    private fun resetCategories(){
        for (i in MyApplication.arrayCategories.indices)
            MyApplication.arrayCategories[i].isVerified=false
    }

    private fun getcategoriesById(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getCategoriesById(id)?.enqueue(object : Callback<ArrayList<ResponseCategory>> {
                override fun onResponse(call: Call<ArrayList<ResponseCategory>>, response: Response<ArrayList<ResponseCategory>>) {
                    try {
                        loading.visibility=View.GONE
                        try{btVerify!!.setOnClickListener{
                            sendVerification(id)
                            dialog!!.dismiss()
                        }}catch (e:java.lang.Exception){}
                        setCategories(response.body()!!)
                    }catch (e:java.lang.Exception){
                        Log.wtf("exception",e.toString())
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseCategory>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"error..",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun setCategories(response: ArrayList<ResponseCategory>) {
        resetCategories()
        if(response.size>0){
            for (i in MyApplication.arrayCategories.indices){
                if(this.inResponse(response, MyApplication.arrayCategories[i].id!!)!!){
                    MyApplication.arrayCategories[i].isVerified=true
                }
            }
        }

        adapterCategories= AdapterCategories(MyApplication.arrayCategories,this,arraypostCategories)
        val glm = GridLayoutManager(this, 1)
        rvVerifyCategories!!.adapter=adapterCategories
        rvVerifyCategories!!.layoutManager=glm


    }

    private fun inResponse(response: ArrayList<ResponseCategory>,id: Int):Boolean?{
        var inside=false
        for (i in response.indices){
            if(response[i].CategoryId==id) {
                inside = true
                break
            }

        }
        return inside

    }

    private fun getcategories(action:Boolean,postId:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getCategories()?.enqueue(object : Callback<ArrayList<ResponseCategory>> {
                override fun onResponse(call: Call<ArrayList<ResponseCategory>>, response: Response<ArrayList<ResponseCategory>>) {
                    try {
                        MyApplication.arrayCategories.clear()
                        MyApplication.arrayCategories.addAll(response.body()!!)
                        if(action) {
                            if(postId!=0)
                                getcategoriesById(postId)
                            else
                                setCategories(arrayListOf())
                        }
                       // initFilters()

                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseCategory>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"error..",Toast.LENGTH_LONG).show()
                }
            })

    }



    private fun sendVerification(postId:Int){
        Log.wtf("adapter_size",adapterCategories!!.categories.size.toString())
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.sendverification(postId, this.getVerifiedCategories(postId)!!)?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        loading.visibility=View.GONE
                        AppHelper.createDialog(this@ActivityInsideComment,response.body()!!)
                    }catch (e:java.lang.Exception){
                        Toast.makeText(applicationContext,"error..",Toast.LENGTH_LONG).show()
                        try{dialog!!.dismiss()}catch (e:Exception){}
                        Log.wtf("exception",e.toString())
                    }
                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"error..",Toast.LENGTH_LONG).show()
                    try{dialog!!.dismiss()}catch (e:Exception){}
                }
            })

    }


    private fun getVerifiedCategories(postId:Int): ArrayList<RequestCategories>? {
        var array:ArrayList<RequestCategories>?= arrayListOf()
        for (i in adapterCategories!!.categories.indices){
            if(adapterCategories!!.categories[i].isVerified!!)
                array!!.add(RequestCategories(postId, adapterCategories!!.categories[i].id))
        }
        return array
    }





    fun showUserDialog(type:Int,id:Int) {

        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_users)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)



        var tvPopupTitle = dialog!!.findViewById<View>(R.id.tvPopupTitle) as TextView
        var  btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        rvPopUpUsers= dialog!!.findViewById<View>(R.id.rvUsers) as RecyclerView
        when (type) {
            AppConstants.POPUP_TYPE_LIKERS -> {
                tvPopupTitle.text=getString(R.string.likes)
                getLikers(id)

            }


        }

        btCancel.setOnClickListener{dialog!!.dismiss()}



        dialog!!.show();

    }


    private fun getLikers(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getLikers(id)?.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseFollowers>>,
                    response: Response<ArrayList<ResponseFollowers>>
                ) {
                    try {
                        setLikers(response.body()!!)

                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseFollowers>>,
                    throwable: Throwable
                ) {
                }
            })
    }

    private fun setLikers(response: ArrayList<ResponseFollowers>){
        arrayLikers.clear()
        arrayLikers.addAll(response)
        adapterPopLikers = AdapterPopupUsers(arrayLikers, this,AppConstants.POPUP_TYPE_LIKERS)
        val glm = GridLayoutManager(this,1)
        rvPopUpUsers!!.adapter = adapterPopLikers
        rvPopUpUsers!!.layoutManager = glm
    }

    private fun followUser(id: Int,position:Int,type: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.follow(
                id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
             }

                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })
    }

}






