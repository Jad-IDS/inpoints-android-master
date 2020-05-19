package com.ids.inpoint.controller.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.model.comments
import kotlinx.android.synthetic.main.fragment_fragment_news_feed.*
import kotlinx.android.synthetic.main.toolbar.*

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import android.widget.*
import com.google.gson.Gson
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.events
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.item_news_feed_comment.view.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.loading.loading
import kotlinx.android.synthetic.main.loading_trans.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue


class FragmentNewsFeed : Fragment(),RVOnItemClickListener, RVOnSubItemClickListener {
    override fun onSubItemClicked(view: View, position: Int, parentPosition: Int) {
     //   Toast.makeText(activity,"aaaaa",Toast.LENGTH_LONG).show()

    }



    var adapternewsfeed:AdapterNewsFeed?=null
    var adapterCategories:AdapterCategories?=null
    private var reportReson=""
    var arrayFeeds= java.util.ArrayList<ResponsePost>()
    var arraypostCategories= java.util.ArrayList<ResponseCategory>()
    private var arrayLikers = java.util.ArrayList<ResponseFollowers>()


    var arraySpinnerCategories= java.util.ArrayList<ItemSpinner>()


    lateinit var adapterSpinnerTypes:AdapterTypes

    var arrayComments= java.util.ArrayList<comments>()
    lateinit var imageFilePath: String
    var date=""
    var dateFeeds=""
    var commentId=""
    var commentPosition=0
    var rvVerifyCategories:RecyclerView ?= null
    var btVerify: LinearLayout ?= null
    private var showFilter=false


    var skip=0
    var take=10
    private var isLoading = false
    private var dialog: Dialog? = null



    private lateinit var fromdatelistener: DatePickerDialog.OnDateSetListener
    private lateinit var fromDateCalendar: Calendar
    private var selectedFromDate = ""

    private lateinit var toDateDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var toDateCalendar: Calendar
    private var selectedToDate = ""


    private var applyFilter=false
    private var pageUserId=0
    var status=2

    var dateRange=""
    var selectedTypes=""
    var selectedCategories=""
    var selectedByName=""
    var postInfo=1

    var selectedByTitle=""
    var rvPopUpUsers: RecyclerView ?= null
    private var adapterPopLikers:AdapterPopupUsers?=null

    var btCloseDialog:LinearLayout ?= null
    var btCancel:TextView ?= null
    var btSaveChanges:LinearLayout ?= null
    var etReason:EditText ?= null
    var rgProblems:RadioGroup ?= null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_fragment_news_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.wtf("fragment_created","news")
        getPostType(false)
        getcategories(false,0,0)
        init()
        setPagination()


    }

    private fun init(){

        rvNewsFeed.isNestedScrollingEnabled=false

        // getAllPosts()
        linearSearch.setOnClickListener{
            try{(activity!! as ActivityHome).openSearch(AppConstants.NEWS)}catch (e:Exception){ }
        }


        linearToolbar.background.alpha=255

        linearWritePost.setOnClickListener{

            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).writePost()}catch (e:Exception){ }
        }
/*        etWritePost.setOnClickListener{
            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).writePost()}catch (e:Exception){ }
        }*/

        linearCreateEvent.setOnClickListener{

            MyApplication.isEventEdit=false
            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).createEvent()}catch (e:Exception){ }
        }

        ivVideo.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(galleryIntent, 1)
        }


        ivCamera.setOnClickListener{

            val permissions = arrayOf(Manifest.permission.CAMERA)
            //  if (!AppHelper.hasPermissions(activity, permissions.toString()))
            if(!checkPermission()!!)
                requestPermissions(permissions, AppConstants.PERMISSION_CAMERA)
            else {
                launchCamera()
            }


        }

        linearPost.setOnClickListener{
            if(showFilter)
                linearFilter.visibility=View.GONE
            else
                linearFilter.visibility=View.VISIBLE

            showFilter=!showFilter

        }


        if(MyApplication.isProfileImageLocal)
            try{AppHelper.setRoundImage(activity!!,ivToolbarProfile,MyApplication.userLoginInfo.image!!,true) }catch (E:java.lang.Exception){}
        else
            try{AppHelper.setRoundImage(activity!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+MyApplication.userLoginInfo.image!!,false) }catch (E:java.lang.Exception){}



        srlNewsFeed.setOnRefreshListener {
            skip=0
            status=2
            adapternewsfeed=null
            resetFilters()
            getAllPosts()
            srlNewsFeed
        }




        ivToolbarProfile.setOnClickListener(View.OnClickListener { v ->
            try{(activity!! as ActivityHome).goProfile()}catch (e:Exception){ }
        })

        ivChat.setOnClickListener{ try{(activity!! as ActivityHome).goToUserChat(AppConstants.NEWS)}catch (e:Exception){ }}

        ivNotification.setOnClickListener{ try{(activity!! as ActivityHome).goNotification(AppConstants.NEWS)}catch (e:Exception){ }}
/*        Handler().postDelayed({
            showFilter=false
            getAllPosts()
        }, 100)*/
    }
    fun reloadData(){
        skip=0
        adapternewsfeed=null
        status=2
        resetFilters()
        getAllPosts()
    }
    override fun onResume() {

        Handler().postDelayed({
       /*     showFilter=false
            getAllPosts()*/
            reloadData()
        }, 100)
        super.onResume()
    }

    override fun onPause() {
        if (srlNewsFeed != null) {
            srlNewsFeed.isRefreshing = false;
            srlNewsFeed.destroyDrawingCache();
            srlNewsFeed.clearAnimation();
        }
        super.onPause()
    }

    private fun checkPermission():Boolean? {
        val permission = ContextCompat.checkSelfPermission(
            this.activity!!,
            Manifest.permission.CAMERA)

        return permission == PackageManager.PERMISSION_GRANTED
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!storageDir.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }

    fun setScaledBitmap(): Bitmap {
        val imageViewWidth = 300
        val imageViewHeight = 300

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth/imageViewWidth, bitmapHeight/imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)

    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==AppConstants.PERMISSION_CAMERA){
            if (grantResults.isNotEmpty()) {
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    //open camera
                    else
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.permission_not_allowed),
                            Toast.LENGTH_LONG
                        ).show()
                } catch (e: Exception) {
                }

            }
        }
    }


    private fun launchCamera() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 202)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            202 -> {
                if (resultCode == Activity.RESULT_OK) {
                }
            }
            else -> {
                Toast.makeText(activity, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun showImage(url:String){
        try{(activity!! as ActivityHome).previewFragment=AppConstants.NEWS}catch (e: java.lang.Exception){ }
        MyApplication.imageType=AppConstants.BOTTOM_SHEET_IMAGE_TYPE_LINK
        MyApplication.selectedImage=url
        val bottomSheetFragment = FragmentImageBottomSheet()
        this.fragmentManager.let { bottomSheetFragment.show(it, bottomSheetFragment.tag) }
    }


    override fun onItemClicked(view: View, position: Int) {
        if(view.id== R.id.linearDots){
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            adapternewsfeed!!.notifyDataSetChanged()
        }else if(view.id==R.id.ivImagePost){
            try{showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![0].fileName.toString())}catch (e:Exception){ }
        }else if(view.id==R.id.ivImagePost2){
            try{showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![1].fileName.toString())}catch (e:Exception){ }
        }else if(view.id==R.id.ivImagePost3){
            try{showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![2].fileName.toString())}catch (e:Exception){ }
        }else if(view.id==R.id.ivImagePost4){
            try{showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![3].fileName.toString())}catch (e:Exception){ }
        }

        else if(view.id==R.id.linearLikers){
            showUserDialog(AppConstants.POPUP_TYPE_LIKERS,adapternewsfeed!!.newsFeeds[position].id!!)
        }
        else if(view.id==R.id.btFollowLikers){
            adapterPopLikers!!.arrayUsers[position].isFollowed=!adapterPopLikers!!.arrayUsers[position].isFollowed!!
            adapterPopLikers!!.notifyDataSetChanged()
            followUser(adapterPopLikers!!.arrayUsers[position].userId!!,position,AppConstants.POPUP_TYPE_LIKERS)
        }
        else if (view.id==R.id.linearLike){
            like(arrayFeeds[position].id!!)
            arrayFeeds[position].liked=!arrayFeeds[position].liked!!

            if(arrayFeeds[position].liked!!)
                arrayFeeds[position].likeNumber=arrayFeeds[position].likeNumber!!+1
            else
                if(arrayFeeds[position].likeNumber!!>=1)
                    arrayFeeds[position].likeNumber=arrayFeeds[position].likeNumber!!-1

            adapternewsfeed!!.notifyDataSetChanged()
        }else if(view.id==R.id.linearComment){
            /*       arrayFeeds[position].showComments=true
                   adapternewsfeed.notifyDataSetChanged()*/

            if(!adapternewsfeed!!.newsFeeds[position].showComments!!)
                getPostById(position, adapternewsfeed!!.newsFeeds[position].id!!)
        }
        else if(view.id==R.id.ctvCommentsCount){
            MyApplication.selectedPost=adapternewsfeed!!.newsFeeds[position]
            (activity!! as ActivityHome).openComments(adapternewsfeed!!.newsFeeds[position].id!!)
        }
        else if(view.id==R.id.ivUser || view.id==R.id.ctvName){
            if(adapternewsfeed!!.newsFeeds[position].userId==MyApplication.userLoginInfo.id){
                try{(activity!! as ActivityHome).goProfile()}catch (e:Exception){ }
            }else {
                MyApplication.selectedPost = adapternewsfeed!!.newsFeeds[position]
                try{(activity!! as ActivityHome).goToOtherprofile(AppConstants.NEWS,AppConstants.OTHER_USER_PROFILE,AppConstants.OTHER_USER_PROFILE_FRAG)}catch (e:Exception){ }
               // startActivity(Intent(activity, ActivityProfile::class.java))
            }
        }

        else if(view.id==R.id.ivSend){
            var etComment: EditText = rvNewsFeed.findViewHolderForAdapterPosition(position)!!.itemView.findViewById(com.ids.inpoint.R.id.etComment) as EditText
            date = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

            if(commentId=="") {
                arrayFeeds[position].arrayComments!!.add(
                    ResponseComments(
                        -1,
                        arrayFeeds[position].id,
                        0,
                        etComment.text.toString(),
                        0,
                        MyApplication.userLoginInfo.id,
                        MyApplication.userLoginInfo.userName,
                        MyApplication.userLoginInfo.image,
                        date,
                        "now",
                        arrayListOf(),
                        false,
                        false
                    )
                )
            }

            else if(view.id==R.id.IdSpinnerTypes){

            }


            else{
                arrayFeeds[position].arrayComments!![commentPosition].replies!!.add(
                    ResponseSubCommrent(-1,commentId.toInt(),   etComment.text.toString(),
                        MyApplication.userLoginInfo.id,
                        MyApplication.userLoginInfo.userName,
                        MyApplication.userLoginInfo.image,
                        date,
                        "now",
                        false
                    ))
            }


            sendComment(commentId, arrayFeeds[position].id!!,etComment.text.toString())

            adapternewsfeed!!.notifyDataSetChanged()
            etComment.setText("")

            var imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etComment.windowToken, 0);
            imm.hideSoftInputFromWindow(svSearch.windowToken, 0);
            svSearch.isFocusable=false
            commentId=""
            commentPosition=0


            // Toast.makeText(activity,"aaaaaa",Toast.LENGTH_LONG).show()
        }
        else if(view.id==R.id.btHidePost){
            showReportDialog(arrayFeeds[position].id!!,position)
/*            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            arrayFeeds.removeAt(position)
            adapternewsfeed!!.notifyDataSetChanged()*/

        }else if(view.id==R.id.btEditPost){
            // Toast.makeText(activity,"edit",Toast.LENGTH_LONG).show()
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            MyApplication.selectedPost=adapternewsfeed!!.newsFeeds[position]
            MyApplication.isPostEdit=true
            if(arrayFeeds[position].divType==AppConstants.TYPE_POST){
                try{(activity!! as ActivityHome).writePost()}catch (e:Exception){ }
            }else{
                try{(activity!! as ActivityHome).createEvent()}catch (e:Exception){ }
            }
            adapternewsfeed!!.notifyDataSetChanged()
        }else if(view.id==R.id.btDeletePost){
            deletePostDialog(activity!!,position)
      /*      arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            deletePost(arrayFeeds[position].id!!)
            arrayFeeds.removeAt(position)
            adapternewsfeed!!.notifyDataSetChanged()*/
        }


        else if(view.id==R.id.linearShare){
            AppHelper.share(activity!!,arrayFeeds[position].title.toString(),arrayFeeds[position].title.toString())
        }

        else if(view.id==R.id.LinearVerified){
            showVerifyPopup(adapternewsfeed!!.newsFeeds[position].id!!,position)
        }
        else if(view.id==R.id.linearShow){
            //getParticipants(arrayFeeds[position].id!!,position)
            arrayFeeds[position].isShowMore=!arrayFeeds[position].isShowMore!!
            adapternewsfeed!!.notifyDataSetChanged()

        }
        else{
            MyApplication.selectedPost=adapternewsfeed!!.newsFeeds[position]
            (activity!! as ActivityHome).openComments(adapternewsfeed!!.newsFeeds[position].id!!)
        }


    }








    fun getAllPosts(){
        if(dateFeeds=="")
            dateFeeds = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

       try{ loading.visibility=View.VISIBLE}catch (e:Exception){}
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllPost(
                JsonParameters(pageUserId,
                    MyApplication.userLoginInfo.id!!,
                    status,
                    dateRange,
                    selectedTypes,
                    selectedCategories,
                    selectedByName,
                    postInfo,
                    dateFeeds,
                    skip,
                    take,
                    selectedByTitle,
                    applyFilter,
                    AppConstants.TYPE_PARAM_POST)

            )?.enqueue(object : Callback<ArrayList<ResponsePost>> {
                override fun onResponse(call: Call<ArrayList<ResponsePost>>, response: Response<ArrayList<ResponsePost>>) {
                    try{
                        loading.visibility=View.GONE
                        onPostRetreived(response)

                    }catch (E:Exception){
                        Log.wtf("get_post_error", E.toString())
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponsePost>>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun onPostRetreived(response: Response<ArrayList<ResponsePost>>){
        try{srlNewsFeed.isRefreshing = false }catch (e:Exception){}


        try{

            if(adapternewsfeed==null) {
                arrayFeeds=response.body()!!
                adapternewsfeed= AdapterNewsFeed(arrayFeeds,this,this,childFragmentManager)
                val glm = GridLayoutManager(activity, 1)
                rvNewsFeed.adapter=adapternewsfeed
                rvNewsFeed.layoutManager=glm
            }else{
                val position = adapternewsfeed!!.newsFeeds.size
                arrayFeeds.addAll(response.body()!!)
                adapternewsfeed!!.notifyItemChanged(position)
            }

        }catch (e:Exception){}
        isLoading=response.body()!!.isEmpty()
        try{

            Log.wtf("dateFeeds",dateFeeds)


        }catch (e:java.lang.Exception){

        }

        if(showFilter)
            linearFilter.visibility=View.VISIBLE
        else
            linearFilter.visibility=View.GONE


    }


    private fun setFeeds(array:ArrayList<ResponsePost>){
        arrayFeeds.clear()
        for (i in array.indices){
            if(array[i].divType==AppConstants.TYPE_POST)
                arrayFeeds.add(array[i])
        }

    }



    private fun getPostById(position:Int,id:Int){
        var  lastDatedate = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())
        // lastDatedate="2019-12-02T17:01:26"
        Log.wtf("date_now",date)
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostById(id,
                0,
                "2019-12-02T17:01:26",
                false
            )?.enqueue(object : Callback<ArrayList<ResponseComments>> {
                override fun onResponse(call: Call<ArrayList<ResponseComments>>, response: Response<ArrayList<ResponseComments>>) {
                    try{
                        //  date=response.headers().get("lastdate").toString()
                        Log.wtf("array_size",arrayFeeds.size.toString()+"")
                        Log.wtf("array_position",position.toString()+"")
                        Log.wtf("new_date",date)
                        loading.visibility=View.GONE

                        arrayFeeds[position].showComments=true
                        arrayFeeds[position].arrayComments= arrayListOf()
                        arrayFeeds[position].arrayComments!!.clear()
                        arrayFeeds[position].arrayComments!!.addAll(response.body()!!)
                        adapternewsfeed!!.notifyDataSetChanged()

                    }catch (E:Exception){
                        Log.wtf("array_exception",E.toString())
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseComments>>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
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
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

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
             deleteCommentDialog(activity!!,event.postPosition,event.commentPosition,0,AppConstants.TYPE_COMMENT)

        }else if(event.message==AppConstants.DELETE_SUB_COMMENT){
            deleteCommentDialog(activity!!,event.postPosition,event.commentPosition,event.commentPosition,AppConstants.TYPE_COMMENT)

        }
        else if(event.message==AppConstants.REPLY_COMMENT){
            commentId= arrayFeeds[event.postPosition].arrayComments!![event.commentPosition].id.toString()
            commentPosition=event.commentPosition
            rvNewsFeed.findViewHolderForAdapterPosition(event.postPosition)!!.itemView.etComment.requestFocus()

            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            Handler().postDelayed({
                rvNewsFeed.findViewHolderForAdapterPosition(event.postPosition)!!.itemView.rvComments.performClick()
                rvNewsFeed.findViewHolderForAdapterPosition(event.postPosition)!!.itemView.etComment.setText("")
          }, 200)

        }else if(event.message==AppConstants.MORE_REPLY_COMMENT){
            MyApplication.selectedPost=adapternewsfeed!!.newsFeeds[event.postPosition]
            (activity!! as ActivityHome).openComments(adapternewsfeed!!.newsFeeds[event.postPosition].id!!)
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






    private fun deletePost(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deletePost(id
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    Toast.makeText(activity,"error..",Toast.LENGTH_LONG).show()
                }
            })

    }



    private fun sendComment(commentId:String,postId:Int,comment:String){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.sendComment(
                JsonParameters(0,postId,commentId,comment,AppConstants.CONST_TYPE_SEND_COMMENT)
                /*      JsonParameters(
                          0,MyApplication.selectedPost.id!!,180,"sssssss",0,0,"username","image","2019-11-20T07:00:02.370Z","time",
                      SendReplies(0,180,"string",0,"string","string","2019-11-20T07:00:02.370Z","string")
                  )*/

            )?.enqueue(object : Callback<ResponseSaveComment> {
                override fun onResponse(call: Call<ResponseSaveComment>, response: Response<ResponseSaveComment>) {
                    try{
                        onCommentSent(response)
                    }catch (E:Exception){
                        Log.wtf("get_post_error", E.toString())

                    }
                }
                override fun onFailure(call: Call<ResponseSaveComment>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(activity,"failed", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun onCommentSent(responseComments: Response<ResponseSaveComment>){

    }




    private fun setPagination() {

        svScrollNews.viewTreeObserver
            .addOnScrollChangedListener {
                try {
                    if (svScrollNews.getChildAt(0).bottom <= svScrollNews.height + svScrollNews.scrollY) {
                        //scroll view is at bottom
                        if (!isLoading) {
                            isLoading = true
                            skip += take
                            getAllPosts()
                        }
                    }
                }catch (E:java.lang.Exception){}

            }




    }




    private fun getcategories(action:Boolean,postId:Int,position: Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getCategories()?.enqueue(object : Callback<ArrayList<ResponseCategory>> {
                override fun onResponse(call: Call<ArrayList<ResponseCategory>>, response: Response<ArrayList<ResponseCategory>>) {
                    try {
                        MyApplication.arrayCategories.clear()
                        MyApplication.arrayCategories.addAll(response.body()!!)
                        if(action) {
                            if(postId!=0)
                                getcategoriesById(postId,position)
                            else
                                setCategories(arrayListOf())
                        }
                        initFilters()

                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseCategory>>, throwable: Throwable) {
                    Toast.makeText(activity,"error..",Toast.LENGTH_LONG).show()
                }
            })

    }



    fun showVerifyPopup(postId:Int,position: Int) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_verify)
        dialog!!.setCancelable(true)
        rvVerifyCategories = dialog!!.findViewById<View>(R.id.rvVerifyCategories) as RecyclerView
        btVerify = dialog!!.findViewById<View>(R.id.btVerify) as LinearLayout
        arraypostCategories.clear()
        if(MyApplication.arrayCategories.size>0) {
            resetCategories()
            getcategoriesById(postId,position)
        }
        else {
            getcategories(true, postId,position)
        }

        dialog!!.show();

    }



    fun showPopupSpinnerCategory(position: Int) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_verify)
        dialog!!.setCancelable(true)
        rvVerifyCategories = dialog!!.findViewById<View>(R.id.rvVerifyCategories) as RecyclerView
        var tvSubmit = dialog!!.findViewById<View>(R.id.tvSubmit) as TextView
        tvSubmit.text = getString(R.string.dialog_ok)
        btVerify = dialog!!.findViewById<View>(R.id.btVerify) as LinearLayout
        arraypostCategories.clear()

        if(MyApplication.arrayCategories.size>0) {
            //resetCategories()
            setCategories(arrayListOf())
        }
        else {
            getcategories(true, 0,position)
        }
        btVerify!!.setOnClickListener{
            setFilterCategories()
            dialog!!.dismiss()
        }



        dialog!!.show();

    }


    fun showPopupSpinnerTypes() {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_verify)
        dialog!!.setCancelable(true)
        rvVerifyCategories = dialog!!.findViewById<View>(R.id.rvVerifyCategories) as RecyclerView
        var tvSubmit = dialog!!.findViewById<View>(R.id.tvSubmit) as TextView
        var tvSelectTitle = dialog!!.findViewById<View>(R.id.tvSelectTitle) as TextView
        tvSelectTitle.text = getString(R.string.select_types)
        tvSubmit.text = getString(R.string.dialog_ok)
        btVerify = dialog!!.findViewById<View>(R.id.btVerify) as LinearLayout
        arraypostCategories.clear()

        if(MyApplication.arrayTypes.size>0) {
            //resetCategories()
            setSpinnerTypes()
        }
        else {
            getPostType(true)
        }
        btVerify!!.setOnClickListener{
            setFilterTypes()
            dialog!!.dismiss()
        }

        dialog!!.show();

    }


    private fun setFilterCategories() {
        try{tvSelectedCategories.text=getSelectedCategoriesFilter()}catch (e:Exception){}
    }

    private fun setFilterTypes() {
        try{tvSelectedTypes.text=getSelectedSpinnerTypes()}catch (e:Exception){}
    }

    private fun resetCategories(){
        for (i in MyApplication.arrayCategories.indices)
            MyApplication.arrayCategories[i].isVerified=false
    }

    private fun resetTypes(){
        for (i in MyApplication.arrayTypes.indices)
            MyApplication.arrayTypes[i].isSelected=false
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
        val glm = GridLayoutManager(activity, 1)
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


    private fun getcategoriesById(id:Int,position: Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getCategoriesById(id)?.enqueue(object : Callback<ArrayList<ResponseCategory>> {
                override fun onResponse(call: Call<ArrayList<ResponseCategory>>, response: Response<ArrayList<ResponseCategory>>) {
                    try {
                        loading.visibility=View.GONE
                        try{btVerify!!.setOnClickListener{
                            sendVerification(id,position)
                            dialog!!.dismiss()
                        }}catch (e:java.lang.Exception){}
                        setCategories(response.body()!!)
                    }catch (e:java.lang.Exception){
                        Log.wtf("exception",e.toString())
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseCategory>>, throwable: Throwable) {
                    Toast.makeText(activity,"error..",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun sendVerification(postId:Int,position: Int){
        Log.wtf("adapter_size",adapterCategories!!.categories.size.toString())
        loading.visibility=View.VISIBLE
        val gson = Gson()
        val json = gson.toJson(this.getVerifiedCategories(postId)!!)
        Log.wtf("send_verification_id",postId.toString())
        Log.wtf("send_verification_json",json)
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.sendverification(postId, this.getVerifiedCategories(postId)!!)?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        loading.visibility=View.GONE
                        createDialog(activity!!,response.body()!!,position)

                    }catch (e:java.lang.Exception){
                        Log.wtf("exception",e.toString())
                    }
                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(activity,getString(R.string.cannot_update_verified_post),Toast.LENGTH_LONG).show()
                }
            })

    }

    fun createDialog(c: Activity, message: String,position:Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ ->
                arrayFeeds[position].verified=true
                adapternewsfeed!!.notifyItemChanged(position)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun getVerifiedCategories(postId:Int): ArrayList<RequestCategories>? {
        var array:ArrayList<RequestCategories>?= arrayListOf()
        for (i in adapterCategories!!.categories.indices){
            if(adapterCategories!!.categories[i].isVerified!!)
                array!!.add(RequestCategories(postId, adapterCategories!!.categories[i].id))
        }
        return array
    }



    private fun getSelectedCategoriesFilter(): String? {
        var categories=""
        for (i in adapterCategories!!.categories.indices){
            if(adapterCategories!!.categories[i].isVerified!!){
                if(categories=="") {
                    categories=adapterCategories!!.categories[i].valueEn.toString()
                    selectedCategories=adapterCategories!!.categories[i].id.toString()
                }else{
                    categories=categories+","+adapterCategories!!.categories[i].valueEn.toString()
                    selectedCategories=selectedCategories+","+adapterCategories!!.categories[i].id.toString()
                }
            }

        }
        return categories
    }



    private fun getSelectedSpinnerTypes(): String? {
        var types=""
        for (i in adapterSpinnerTypes.itemSpinner.indices){
            if(adapterSpinnerTypes.itemSpinner[i].isSelected!!){
                if(types=="") {
                    types=adapterSpinnerTypes.itemSpinner[i].valueEn.toString()
                    selectedCategories=adapterSpinnerTypes.itemSpinner[i].id.toString()
                }else{
                    types=types+","+adapterSpinnerTypes.itemSpinner[i].valueEn.toString()
                    selectedCategories=selectedCategories+","+adapterSpinnerTypes.itemSpinner[i].id.toString()
                }
            }

        }
        return types
    }


    private fun initFilters(){


        fromDateCalendar = Calendar.getInstance()
        fromdatelistener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            fromDateCalendar.set(Calendar.YEAR, year)
            fromDateCalendar.set(Calendar.MONTH, monthOfYear)
            fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tvFromDate.text = AppHelper.dateFormatProfile.format(fromDateCalendar.time)
            selectedFromDate = AppHelper.dateFormatProfile.format(fromDateCalendar.time)
        }
        tvFromDate.setOnClickListener{
            DatePickerDialog(activity!!, fromdatelistener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }


        toDateCalendar = Calendar.getInstance()
        toDateDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            toDateCalendar.set(Calendar.YEAR, year)
            toDateCalendar.set(Calendar.MONTH, monthOfYear)
            toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tvToDate.text = AppHelper.dateFormatProfile.format(toDateCalendar.time)
            selectedToDate = AppHelper.dateFormatProfile.format(toDateCalendar.time)
        }
        tvToDate.setOnClickListener{
            DatePickerDialog(activity!!, toDateDateListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        arraySpinnerCategories.clear()
        for (i in MyApplication.arrayCategories.indices)
            arraySpinnerCategories.add(ItemSpinner(MyApplication.arrayCategories[i].id,MyApplication.arrayCategories[i].valueEn,MyApplication.arrayCategories[i].iconPath))


        linearSelectedCategories.setOnClickListener{showPopupSpinnerCategory(0)}
        linearSelectedTypes.setOnClickListener{showPopupSpinnerTypes()}
/*        adapterSpinnerCategories= AdapterCustomSpinner(activity!!,arraySpinnerCategories,AppConstants.SPINNER_TEXT_IMAGE)
        spCategories.adapter = adapterSpinnerCategories;
        spCategories.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
              //  text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }*/

        rbAll.isChecked=true
        rbAll.setOnClickListener{
            status=2
        }
        rbVerified.setOnClickListener{
            status=1
        }
        rbNotVerified.setOnClickListener{
            status=0
        }


        btClear.setOnClickListener{
            // Toast.makeText(activity,"aaaa",Toast.LENGTH_LONG).show()
            resetFilters()
            getAllPosts()
        }
        btApply.setOnClickListener{filterNewsFeeds()}


    }


    private fun filterNewsFeeds(){
        skip=0
        dateFeeds=""
        adapternewsfeed=null
        linearFilter.visibility=View.GONE
        showFilter=false
        selectedByTitle=etFilterTitle.text.toString()
        selectedByName=etFilterUsername.text.toString()
        var dateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date())
        if(tvFromDate.text.toString().isEmpty() && tvToDate.text.toString().isEmpty())
            dateRange= "01/01/1991-$dateNow"
        else if(tvFromDate.text.toString().isEmpty() && tvToDate.text.toString().isNotEmpty())
            dateRange="01/01/1991"+"-"+tvToDate.text.toString()
        else if(tvFromDate.text.toString().isEmpty() && tvToDate.text.toString().isEmpty())
            dateRange=tvFromDate.text.toString()+"-"+dateNow
        else
            dateRange=tvFromDate.text.toString()+"-"+tvToDate.text.toString()

        Log.wtf("date_range",dateRange)

        applyFilter=true
        getAllPosts()
    }

    private fun resetFilters(){
        skip=0
        status=2
        dateFeeds=""
        adapternewsfeed=null
        linearFilter.visibility=View.GONE
        showFilter=false
        selectedByTitle=""
        selectedByName=""
        dateRange=""
        applyFilter=false
        etFilterUsername.setText("")
        etFilterTitle.setText("")
        tvFromDate.text = ""
        tvToDate.text=""
        rbAll.isChecked=true
        resetCategories()
        resetTypes()


    }


    private fun getPostType(action:Boolean){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostTypes(AppConstants.TYPE_POST
            )?.enqueue(object : Callback<ArrayList<ResponsePostType>> {
                override fun onResponse(call: Call<ArrayList<ResponsePostType>>, response: Response<ArrayList<ResponsePostType>>) {
                    try{
                        MyApplication.arrayTypes.clear()
                        MyApplication.arrayTypes.addAll(response.body()!!)
                        if(action)
                            setSpinnerTypes()
                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponsePostType>>, throwable: Throwable) {
                }
            })

    }


    private fun setSpinnerTypes(){

        adapterSpinnerTypes= AdapterTypes(MyApplication.arrayTypes,this,AppConstants.SPINNER_POST_PRIVACY)
        val glm = GridLayoutManager(activity, 1)
        rvVerifyCategories!!.adapter=adapterSpinnerTypes
        rvVerifyCategories!!.layoutManager=glm
    }


    fun deletePostDialog(c: Activity,position:Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_post_verification))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
              dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
                deletePost(arrayFeeds[position].id!!)
                arrayFeeds.removeAt(position)
                adapternewsfeed!!.notifyDataSetChanged()
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }



    fun deleteCommentDialog(c: Activity,postPosition:Int,commentPosition:Int,subCommentPosition:Int,type:Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_commnent_verification))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
             if(type==AppConstants.TYPE_COMMENT){
                     deleteComment(arrayFeeds[postPosition].arrayComments!![commentPosition].id!!)
                     arrayFeeds[postPosition].arrayComments!!.removeAt(commentPosition)
                     adapternewsfeed!!.notifyDataSetChanged()

             }else{
                 deleteComment(arrayFeeds[postPosition].arrayComments!![commentPosition].id!!)
                 arrayFeeds[postPosition].arrayComments!![commentPosition].replies!!.removeAt(subCommentPosition)
                 adapternewsfeed!!.notifyDataSetChanged()
             }
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun initSpinnerTypes(){

        /*       adapterSpinnerTypes= AdapterCustomSpinner(activity!!,arraySpinnerTypes,AppConstants.SPINNER_TYPE_TEXT)
               spType.adapter = adapterSpinnerTypes
               spType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                   override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                       //  text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                   }

                   override fun onNothingSelected(parent: AdapterView<*>){
                       // Another interface callback
                   }
               }*/
    }




    private fun getParticipants(id:Int,position: Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getEventParticipant(id
            )?.enqueue(object : Callback<ArrayList<ResponseParticipant>> {
                override fun onResponse(call: Call<ArrayList<ResponseParticipant>>, response: Response<ArrayList<ResponseParticipant>>) {
                      loading.visibility=View.GONE

                    try{onParticipantsRetrieved(response.body(),id,position)}catch (E:Exception){
                        arrayFeeds[position].isShowMore=!arrayFeeds[position].isShowMore!!
                        adapternewsfeed!!.notifyDataSetChanged()
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseParticipant>>, throwable: Throwable) {
                    arrayFeeds[position].isShowMore=!arrayFeeds[position].isShowMore!!
                    adapternewsfeed!!.notifyDataSetChanged()
                  loading.visibility=View.GONE
                }
            })

    }



    private fun onParticipantsRetrieved(response: ArrayList<ResponseParticipant>?,id: Int,position: Int){
        var isParticipant=false
        for (i in response!!.indices){
            if(response[i].userId==MyApplication.userLoginInfo.id){
                isParticipant=true
                break
            }
        }
       // arrayFeeds[position].isParticipant=isParticipant
        arrayFeeds[position].isShowMore=!arrayFeeds[position].isShowMore!!
        adapternewsfeed!!.notifyDataSetChanged()


    }


    fun showUserDialog(type:Int,id:Int) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
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




    private fun showReportDialog(id:Int, position: Int) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_report)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout
        etReason = dialog!!.findViewById<View>(R.id.etReason) as EditText
        rgProblems=dialog!!.findViewById<View>(R.id.rgProblems) as RadioGroup

        if(MyApplication.arrayReports.size==0)
            getReports()
        else
            OnReportsRetrieved(MyApplication.arrayReports)
        btSaveChanges!!.setOnClickListener {
           if(etReason!!.text.toString().isEmpty())
               AppHelper.createDialog(activity!!,getString(R.string.enter_reason))
            else
               sendReport(id,position)

        }

        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.setOnDismissListener {
           try{
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            adapternewsfeed!!.notifyItemChanged(position)}catch (e:java.lang.Exception){}
        }
        dialog!!.show()

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
        val glm = GridLayoutManager(activity,1)
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
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        adapternewsfeed=null
        Log.wtf("adapter_new_feed","null")
    }




    private fun getReports() {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getGeneralLookupParentById(
                AppConstants.LOOKUP_REPORTS
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseLocations>>,
                    response: Response<ArrayList<ResponseLocations>>
                ) {
                    try {
                        MyApplication.arrayReports.addAll(response.body()!!)
                        OnReportsRetrieved(response.body())
                    } catch (e: java.lang.Exception) {

                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseLocations>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }


    private fun OnReportsRetrieved(response: ArrayList<ResponseLocations>?) {

        val rbn = RadioButton(activity)
        rbn.id = 0
        rbn.text = getString(R.string.none)
        rgProblems!!.addView(rbn)

        for(i in response!!.indices){
            val rbn = RadioButton(activity)
            rbn.id = response[i].id!!

            if(MyApplication.languageCode==AppConstants.LANG_ENGLISH)
               rbn.text = response[i].valueEn
            else
               rbn.text=response[i].valueAr

            rgProblems!!.addView(rbn)
        }

/*
        val buttons = 5
        for (i in 1..buttons) {
            val rbn = RadioButton(activity)
            rbn.id = View.generateViewId()
            rbn.text = "RadioButton$i"
            rgProblems!!.addView(rbn)
        }*/

        rgProblems!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { rg, checkedId ->
            for (i in 0 until rg.childCount) {
                val btn = rg.getChildAt(i) as RadioButton
                if (btn.id == checkedId) {
                    reportReson = btn.text.toString()
                    // do something with text
                    return@OnCheckedChangeListener
                }
            }
        })
    }





    private fun sendReport(postId: Int,position: Int) {

        loading.visibility=View.VISIBLE
//        val radio:RadioButton = activity!!.findViewById(rgProblems!!.checkedRadioButtonId)
       // val valueRadio=radio.text.toString()
        Log.wtf("radio_id",rgProblems!!.checkedRadioButtonId.toString())
        Log.wtf("radio_value",reportReson)

        var report=ResponseSendReport(0,MyApplication.userLoginInfo.id,MyApplication.userLoginInfo.userName,MyApplication.userLoginInfo.image,postId,0,"","",false,
           "",
            rgProblems!!.checkedRadioButtonId.toString(),
            reportReson,
            etReason!!.text.toString()
           )

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.postReport(
                report
            )?.enqueue(object : Callback<ResponseSendReport> {
                override fun onResponse(
                    call: Call<ResponseSendReport>,
                    response: Response<ResponseSendReport>
                ) {
                    loading.visibility = View.GONE
                    dialog!!.dismiss()
                    try {

                    } catch (e: java.lang.Exception) {

                    }
                }

                override fun onFailure(
                    call: Call<ResponseSendReport>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }


}
