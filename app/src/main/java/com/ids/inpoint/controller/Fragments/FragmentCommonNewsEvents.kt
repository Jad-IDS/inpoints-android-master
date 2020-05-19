package com.ids.inpoint.controller.Fragments

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
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import android.widget.*
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Activities.ActivityProfile
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.events
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.item_news_feed_comment.view.*
import kotlinx.android.synthetic.main.loading_trans.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentCommonNewsEvents : Fragment(),RVOnItemClickListener, RVOnSubItemClickListener {
    override fun onSubItemClicked(view: View, position: Int, parentPosition: Int) {

    }

    var adapternewsfeed:AdapterNewsFeed?=null
    var adapterCategories:AdapterCategories?=null
    var arrayFeeds= java.util.ArrayList<ResponsePost>()
    var arraypostCategories= java.util.ArrayList<ResponseCategory>()
    var arrayComments= java.util.ArrayList<comments>()
    var date=""
    var dateFeeds=""
    var commentId=""
    var commentPosition=0
    var rvVerifyCategories:RecyclerView ?= null
    var btVerify: LinearLayout ?= null



    var skip=0
    var take=10
     var isLoading = false
    private var dialog: Dialog? = null

    var applyFilter=false
    var pageUserId=0
    var status=2
    var postInfo=1

    lateinit var parentFragment:FragmentNewsFeed_

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_common_news_events, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()



    }

    private fun init(){
       try{  parentFragment = this@FragmentCommonNewsEvents.getParentFragment() as FragmentNewsFeed_}catch (e:Exception){}

        rvNewsFeed.isNestedScrollingEnabled=false
        Handler().postDelayed({
            getPostType(false)
            getcategories(false,0)
        }, 200)
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

        else if (view.id==R.id.linearLike){
            like(arrayFeeds[position].id!!)
            arrayFeeds[position].liked=!arrayFeeds[position].liked!!
            adapternewsfeed!!.notifyDataSetChanged()
        }else if(view.id==R.id.linearComment){
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
                startActivity(Intent(activity, ActivityProfile::class.java))
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
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            arrayFeeds.removeAt(position)
            adapternewsfeed!!.notifyDataSetChanged()

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
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            deletePost(arrayFeeds[position].id!!)
            arrayFeeds.removeAt(position)
            adapternewsfeed!!.notifyDataSetChanged()
        }


        else if(view.id==R.id.linearShare){
            AppHelper.share(activity!!,arrayFeeds[position].title.toString(),arrayFeeds[position].title.toString())
        }

        else if(view.id==R.id.LinearVerified){
            showVerifyPopup(adapternewsfeed!!.newsFeeds[position].id!!)
        }
        else if(view.id==R.id.linearShow){
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

        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllPost(
                //  MyApplication.userLoginInfo.token.toString(),
                JsonParameters(pageUserId,
                    MyApplication.userLoginInfo.id!!,
                    status,
                    parentFragment.dateRange,
                    parentFragment.selectedTypes,
                    parentFragment.selectedCategories,
                    parentFragment.selectedByName,
                    postInfo,
                    dateFeeds,
                    skip,
                    take,
                    parentFragment.selectedByTitle,
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

       //related_to_newsfeed
        if(parentFragment.showFilter)
            linearFilter.visibility=View.VISIBLE
        else
            linearFilter.visibility=View.GONE
       //related_to_news_need

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
            deleteComment(arrayFeeds[event.postPosition].arrayComments!![event.commentPosition].id!!)
            arrayFeeds[event.postPosition].arrayComments!!.removeAt(event.commentPosition)
            adapternewsfeed!!.notifyDataSetChanged()

        }else if(event.message==AppConstants.DELETE_SUB_COMMENT){
            deleteComment(arrayFeeds[event.postPosition].arrayComments!![event.commentPosition].id!!)
            arrayFeeds[event.postPosition].arrayComments!![event.commentPosition].replies!!.removeAt(event.subCommentPosition)
            adapternewsfeed!!.notifyDataSetChanged()
        }else if(event.message==AppConstants.REPLY_COMMENT){
            commentId= arrayFeeds[event.postPosition].arrayComments!![event.commentPosition].id.toString()
            commentPosition=event.commentPosition
            rvNewsFeed.findViewHolderForAdapterPosition(event.postPosition)!!.itemView.etComment.requestFocus();
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

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







     fun getcategories(action:Boolean,postId:Int){
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

                        //related_to_news_feed
                       // initFilters()

                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseCategory>>, throwable: Throwable) {
                    Toast.makeText(activity,"error..",Toast.LENGTH_LONG).show()
                }
            })

    }



    fun showVerifyPopup(postId:Int) {

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
            getcategoriesById(postId)
        }
        else {
            getcategories(true, postId)
        }

        dialog!!.show();

    }



    fun resetCategories(){
        for (i in MyApplication.arrayCategories.indices)
            MyApplication.arrayCategories[i].isVerified=false
    }


     fun setCategories(response: ArrayList<ResponseCategory>) {

        adapterCategories= AdapterCategories(MyApplication.arrayCategories,this,arraypostCategories)
        val glm = GridLayoutManager(activity, 1)
        rvVerifyCategories!!.adapter=adapterCategories
        rvVerifyCategories!!.layoutManager=glm


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


    private fun sendVerification(postId:Int){
        Log.wtf("adapter_size",adapterCategories!!.categories.size.toString())
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.sendverification(postId, this.getVerifiedCategories(postId)!!)?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        loading.visibility=View.GONE
                        AppHelper.createDialog(activity!!,response.body()!!)
                    }catch (e:java.lang.Exception){
                        Log.wtf("exception",e.toString())
                    }
                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    Toast.makeText(activity,"error..",Toast.LENGTH_LONG).show()
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



    fun testToast(){
        Toast.makeText(activity,"aaaa",Toast.LENGTH_LONG).show()
    }


     fun getPostType(action:Boolean){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostTypes(AppConstants.TYPE_POST
            )?.enqueue(object : Callback<ArrayList<ResponsePostType>> {
                override fun onResponse(call: Call<ArrayList<ResponsePostType>>, response: Response<ArrayList<ResponsePostType>>) {
                    try{
                        MyApplication.arrayTypes.clear()
                        MyApplication.arrayTypes.addAll(response.body()!!)
                        if(action) {
                            //related_to_newsfeed
                           // setSpinnerTypes()
                        }
                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponsePostType>>, throwable: Throwable) {
                }
            })

    }







}
