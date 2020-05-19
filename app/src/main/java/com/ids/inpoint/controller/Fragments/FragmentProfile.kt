package com.ids.inpoint.controller.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.*
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.*
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*


import kotlinx.android.synthetic.main.fragment_fragment_profile.*
import kotlinx.android.synthetic.main.fragment_fragment_profile.btDelete
import kotlinx.android.synthetic.main.fragment_fragment_profile.btProfileInfo
import kotlinx.android.synthetic.main.fragment_fragment_profile.coverImage
import kotlinx.android.synthetic.main.fragment_fragment_profile.ivUserProfile
import kotlinx.android.synthetic.main.fragment_fragment_profile.linearCreateEvent
import kotlinx.android.synthetic.main.fragment_fragment_profile.linearWritePost
import kotlinx.android.synthetic.main.item_news_feed_comment.view.*
import kotlinx.android.synthetic.main.item_team_member.*
import kotlinx.android.synthetic.main.loading.loading
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentProfile : Fragment(), RVOnItemClickListener, RVOnSubItemClickListener {

    private var myId: Int = 0
    private var userId: Int = 0
    private var pageUserId: Int = 0
    var dateFeeds=""
    private lateinit var user: ResponseUserInfos
    private lateinit var adapterFollower: AdapterFollowers
    private var arrayFollowers = java.util.ArrayList<ResponseFollowers>()
    private var arrayLikers = java.util.ArrayList<ResponseFollowers>()
    private var arrayPoints = java.util.ArrayList<ResponsePoint>()

    private lateinit var adapterTeamMembers: AdapterTeamMembers
    private var teamMembers = java.util.ArrayList<TeamStartupMember>()
    private var isFollower = false
    var skip=0
    var take=10
    private var isLoading = false
    var date=""
    var commentPosition=0
    var arraypostCategories= java.util.ArrayList<ResponseCategory>()
    var rvVerifyCategories:RecyclerView ?= null
    var commentId=""
    var btVerify: LinearLayout ?= null


    private var adapternewsfeed:AdapterNewsFeed?=null

    private var adapterPopLikers:AdapterPopupUsers?=null
    private var adapterPopupFollowers:AdapterPopupUsers?=null
    private var adapterPopupPoints:AdapterPopupPoints?=null


    var adapterCategories: AdapterCategories?=null
    var arrayFeeds = java.util.ArrayList<ResponsePost>()

    var arrayComments = java.util.ArrayList<comments>()
    var percentage = 0.0
    private var followerName=""
    private var followerImage=""
    private var dialog: Dialog? = null

    var rvPopUpUsers: RecyclerView ?= null
    private var showToPublic=false
    var btCancel:TextView ?= null

    var btSaveChanges:LinearLayout ?= null
    var btCloseDialog:LinearLayout ?= null
    var etTeamName:EditText ?= null
    var etTeamBio:EditText ?= null

    private var showFilter=false
    var dateRange=""
    var selectedTypes=""
    var selectedCategories=""
    var selectedByName=""
    var postInfo=1
    var selectedByTitle=""
    var status=2
    private var applyFilter=false

    private lateinit var fromdatelistener: DatePickerDialog.OnDateSetListener
    private lateinit var fromDateCalendar: Calendar
    private var selectedFromDate = ""

    private lateinit var toDateDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var toDateCalendar: Calendar
    private var selectedToDate = ""

    lateinit var adapterSpinnerTypes:AdapterTypes
    var arraySpinnerCategories= java.util.ArrayList<ItemSpinner>()


    override fun onSubItemClicked(view: View, position: Int, parentPosition: Int) {
        //Toast.makeText(activity,"aaaaa",Toast.LENGTH_LONG).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPostType(false)
        getcategories(false,0,0)
        init()
        setPagination()

    }

    fun reloadData(){
        skip=0
        adapternewsfeed=null
        status=2
        resetFilters()
        getAllPosts(pageUserId,userId)
    }

    private fun init() {
        rvProfileFeeds.isNestedScrollingEnabled = false
        setToolbarScrollAnimation()
        setCommonListeners()
        myId = AppHelper.fragmentAvailable!!
        userId=MyApplication.userLoginInfo.id!!
        btBack.setOnClickListener{activity!!.onBackPressed()}
        llFollowers.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityFollowers::class.java)
                .putExtra(AppConstants.PAGE_USER_ID, pageUserId))
        }

        setListeners()

        if((myId==AppConstants.TEAM_PROFILE || myId==AppConstants.STARTUP_PROFILE) && MyApplication.selectedStartupTeam.userName==null)
            getInfoById()
/*        else if((myId==AppConstants.DEFAULT_MY_PROFILE || myId==AppConstants.DEFAULT_OTHER_USER_PROFILE) && MyApplication.selectedPost.userName==null)
           getaaaa*/
        else
            setData()




    }


    private fun setTeamAdminSettings(){
        btMessages.setOnClickListener{
            resetToolbar()
            btChat.performClick()
        }

        btProjects.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityTeamProjects::class.java))
        }

        btFiles.setOnClickListener{
            resetToolbar()
            startActivity(Intent(activity, ActivityTeamFiles::class.java))
        }

        btTasks.setOnClickListener{
            resetToolbar()
            startActivity(Intent(activity, ActivityTeamTasks::class.java))
        }

        btActionPlan.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityActionPlan::class.java))
        }
    }





    private fun setStartupAdminSettings(){
        btProfileInfo.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityStartupInsideProfile::class.java)
                .putExtra(AppConstants.DEFAULT_FRAG,AppConstants.STARTUP_PROFILE_INFO)
                .putExtra(AppConstants.DEFAULT_FRAG_TAG,AppConstants.STARTUP_PROFILE_INFO_FRAG)
            )
        }

        btProductServices.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityStartupInsideProfile::class.java)
                .putExtra(AppConstants.DEFAULT_FRAG,AppConstants.STARTUP_PROFILE_PRODUCT)
                .putExtra(AppConstants.DEFAULT_FRAG_TAG,AppConstants.STARTUP_PROFILE_PRODUCT_FRAG)
            )
        }

        btBranches.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityStartupInsideProfile::class.java)
                .putExtra(AppConstants.DEFAULT_FRAG,AppConstants.STARTUP_PROFILE_BRANCHES)
                .putExtra(AppConstants.DEFAULT_FRAG_TAG,AppConstants.STARTUP_PROFILE_BRANCHES_FRAG)
            )
        }

        btPartners.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityStartupInsideProfile::class.java)
                .putExtra(AppConstants.DEFAULT_FRAG,AppConstants.STARTUP_PROFILE_PARTNERS)
                .putExtra(AppConstants.DEFAULT_FRAG_TAG,AppConstants.STARTUP_PROFILE_PARTNERS_FRAG)
            )
        }

        btReviews.setOnClickListener {
            resetToolbar()
            startActivity(Intent(activity, ActivityStartupInsideProfile::class.java)
                .putExtra(AppConstants.DEFAULT_FRAG,AppConstants.STARTUP_PROFILE_REVIEWS)
                .putExtra(AppConstants.DEFAULT_FRAG_TAG,AppConstants.STARTUP_PROFILE_REVIEWS_FRAG)
            )
        }

    }


    private fun setCommonStartupTeam(){
        btBack.visibility=View.VISIBLE
        pageUserId = MyApplication.selectedStartupTeam.id!!
        getFollowers(pageUserId,false)
        getUserInfo(pageUserId)

        llMembers.visibility = View.VISIBLE
        followerImage=MyApplication.selectedStartupTeam.image!!
        followerName=MyApplication.selectedStartupTeam.userName!!


        if(MyApplication.selectedStartupTeam.admin!!) {

            linearEditProfileImg.visibility=View.VISIBLE


            btDelete.visibility=View.VISIBLE
            btEditProfile.visibility = View.VISIBLE

        }
        else {
            btEditProfile.visibility = View.GONE
            linearEditProfileImg.visibility=View.GONE
        }


        setDatafollowers()
        btChat.visibility=View.GONE

    }



    private fun setDatafollowers(){
      //  if (MyApplication.arrayFollowers.size == 0)
            getFollowers(pageUserId, true)
    /*    else {
            checkFollwer()
            btFollow.setOnClickListener {
                isFollower = !isFollower
                setFollowButton()
                followUser(pageUserId)

            }
        }*/


    }


    private fun checkFollwer() {
        isFollower = false
        for (i in MyApplication.arrayFollowers.indices) {
            if (userId == MyApplication.arrayFollowers[i].userId) {
                isFollower = true
                break
            }
        }
        setFollowButton()
    }

    private fun setPagination() {

        scrollProfile.viewTreeObserver
            .addOnScrollChangedListener {
                try {
                    if (scrollProfile.getChildAt(0).bottom <= scrollProfile.height + scrollProfile.scrollY) {
                        if (!isLoading) {
                            isLoading = true
                            skip += take
                            getAllPosts(pageUserId,userId)
                        }
                    }
                }catch (E:java.lang.Exception){}

            }




    }





    private fun setCommonListeners() {

        linearSearch.setOnClickListener {
            try {
                resetToolbar()
                (activity!! as ActivityHome).openSearch(AppConstants.PROFILE)
            } catch (e: Exception) {
            }
        }

        ivChat.setOnClickListener {
            try {
                resetToolbar()
                (activity!! as ActivityHome).goToUserChat(AppConstants.PROFILE)
            } catch (e: Exception) {
            }
        }

        ivNotification.setOnClickListener {
            try {
                resetToolbar()
                (activity!! as ActivityHome).goNotification(AppConstants.PROFILE)
            } catch (e: Exception) {
            }
        }

        linearWritePost.setOnClickListener{
            resetToolbar()
            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).writePost()}catch (e:Exception){ }
        }

        linearCreateEvent.setOnClickListener{
            resetToolbar()
            MyApplication.isEventEdit=false
            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).createEvent()}catch (e:Exception){ }
        }


        linearPost.setOnClickListener{
            if(showFilter)
                linearFilter.visibility=View.GONE
            else
                linearFilter.visibility=View.VISIBLE

            showFilter=!showFilter

        }

        btClear.setOnClickListener{
            // Toast.makeText(activity,"aaaa",Toast.LENGTH_LONG).show()
            resetToolbar()
            resetFilters()
            getAllPosts(pageUserId,userId)
        }
        btApply.setOnClickListener{
            resetToolbar()
            filterNewsFeeds()}

    }

     fun resetToolbar(){
        try{linearToolbar.background.alpha = 255}catch (e: java.lang.Exception){}
    }


    override fun onResume() {
/*        Handler().postDelayed({
            if (myId == AppConstants.TEAM_PROFILE) {
               getAllPosts(userId, MyApplication.userLoginInfo.id!!)
            } else {
                  getAllPosts(pageUserId,userId)
            }

        }, 100)*/
        super.onResume()
    }

    private fun setToolbarScrollAnimation() {

        scrollProfile.viewTreeObserver.addOnScrollChangedListener(OnScrollChangedListener {
            try {
                var scrollY = scrollProfile.scrollY // For ScrollView
                Log.wtf("scroll_position", scrollY.toString())
                Log.wtf(
                    "scroll_position_dp",
                    convertPixelsToDp(scrollY.toFloat(), activity!!).toString()
                )
                if (convertPixelsToDp(scrollY.toFloat(), activity!!) < 240) {
                    llToolbar.visibility = View.GONE
                } else if (convertPixelsToDp(scrollY.toFloat(), activity!!) in 240.0..300.0) {
                    //animate visibility
                    llToolbar.visibility = View.VISIBLE
                    percentage = (((convertPixelsToDp(
                        scrollY.toFloat(),
                        activity!!
                    ) - 240).toDouble() * 100) / 60) / 100
                    linearToolbar.background.alpha = (percentage * 255).toInt()
                    Log.wtf("percentage", percentage.toString())

                } else {
                    linearToolbar.background.alpha = 255
                    llToolbar.visibility = View.VISIBLE
                }
            } catch (E: Exception) {
                Log.wtf("scroll_exception", E.toString())
            }
        })
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun getUserInfo(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserProfile(
                id
            )?.enqueue(object : Callback<ResponseUserInfos> {
                override fun onResponse(
                    call: Call<ResponseUserInfos>,
                    response: Response<ResponseUserInfos>
                ) {
                    try {
                       // MyApplication.userInfo = response.body()!!
                        try {
                            setInfo(response.body()!!)
                        } catch (E: java.lang.Exception) {
                        }


                    } catch (E: Exception) {
                        Log.wtf("get_post_error", E.toString())

                    }
                }

                override fun onFailure(call: Call<ResponseUserInfos>, throwable: Throwable) {
                     try{ Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()}catch (e:java.lang.Exception){}
                }
            })

    }

    fun resetImageProfile(){
        if (MyApplication.isProfileImageLocal) {
            try {
                AppHelper.setRoundImageResize(activity!!, ivUserProfile, MyApplication.selectedStartupTeam.image!!, MyApplication.isProfileImageLocal)
            } catch (E: Exception) { }

            try {
                AppHelper.setRoundImageResize(activity!!, ivToolbarProfile, MyApplication.selectedStartupTeam.image!!, MyApplication.isProfileImageLocal)
            } catch (E: java.lang.Exception) {
            }
        } else {
            try {
                AppHelper.setRoundImageResize(activity!!, ivUserProfile, AppConstants.IMAGES_URL + "/" + MyApplication.selectedStartupTeam.image!!, MyApplication.isProfileImageLocal)
            } catch (E: Exception) {
            }
            try {
                AppHelper.setRoundImageResize(activity!!, ivToolbarProfile, AppConstants.IMAGES_URL + "/" + MyApplication.selectedStartupTeam.image!!, MyApplication.isProfileImageLocal)
            } catch (E: java.lang.Exception) {
            }
        }
        try {
            AppHelper.setImage(activity!!, coverImage, AppConstants.IMAGES_URL + "/" + MyApplication.selectedStartupTeam.coverImage!!)
        } catch (E: Exception) {
        }

    }

    fun setInfo(userInfo:ResponseUserInfos) {
        Log.wtf("profile_image", userInfo.image)
        if (MyApplication.isProfileImageLocal) {
            try {
                AppHelper.setRoundImageResize(activity!!, ivUserProfile, userInfo.image!!, MyApplication.isProfileImageLocal)
            } catch (E: Exception) { }

            try {
                AppHelper.setRoundImageResize(activity!!, ivToolbarProfile, userInfo.image!!, MyApplication.isProfileImageLocal)
            } catch (E: java.lang.Exception) {
            }
        } else {
            try {
                AppHelper.setRoundImageResize(activity!!, ivUserProfile, AppConstants.IMAGES_URL + "/" + userInfo.image!!, MyApplication.isProfileImageLocal)
            } catch (E: Exception) {
            }
            try {
                AppHelper.setRoundImageResize(activity!!, ivToolbarProfile, AppConstants.IMAGES_URL + "/" + userInfo.image!!, MyApplication.isProfileImageLocal)
            } catch (E: java.lang.Exception) {
            }
        }
        try {
            AppHelper.setImage(activity!!, coverImage, AppConstants.IMAGES_URL + "/" + userInfo.coverImage!!)
        } catch (E: Exception) {
        }

        try {
            tvBadgesValue.text = userInfo.badges.toString()
        } catch (E: Exception) {
        }
        try {
            tvLevelValue.text = userInfo.level.toString()
        } catch (E: Exception) {
        }
        try {
            tvPointsValue.text = userInfo.points.toString()
        } catch (E: Exception) {
        }
        try {
            tvFollowersValue.text = userInfo.Interested.toString()
        } catch (E: Exception) {
        }
        try {
            tvUsername.text = userInfo.userName.toString()
        } catch (E: Exception) {
        }

        try {
            tvInterestedValue.text = userInfo.points.toString()
        } catch (E: Exception) {
        }


    }

    private fun getFollowers(id: Int, isCheckFollowers: Boolean) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getFollowers(id)?.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseFollowers>>,
                    response: Response<ArrayList<ResponseFollowers>>
                ) {
                    try {
                        if (isCheckFollowers) {
                            MyApplication.arrayFollowers.clear()
                            MyApplication.arrayFollowers = response.body()!!
                            checkFollwer()
                            btFollow.setOnClickListener {
                                followUser(pageUserId,-1,AppConstants.TYPE_FOLLOW_PROFILE)
                                isFollower = !isFollower
                                setFollowButton()
                            }
                        } else
                            setFollowers(response.body()!!)
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseFollowers>>,
                    throwable: Throwable
                ) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }
    private fun setFollowers(response: ArrayList<ResponseFollowers>) {
        arrayFollowers = response
        tvActiveFollowers.text =
            "${arrayFollowers.count().toString()} ${getString(com.ids.inpoint.R.string.active)}"
        adapterFollower = AdapterFollowers(arrayFollowers, this)
        // val glm = GridLayoutManager(activity, 5)
        val llm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvFollowers1.adapter = adapterFollower
        rvFollowers1.layoutManager = llm

        try{adapterPopupFollowers!!.notifyDataSetChanged()}catch (e:java.lang.Exception){}
    }

    private fun getAllPosts(pageUserId: Int = 0, userId: Int = 0) {
        if(dateFeeds=="")
            dateFeeds = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllPost(
                //  MyApplication.userLoginInfo.token.toString(),
                JsonParameters(
                    pageUserId,
                    userId,
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
                    AppConstants.TYPE_PARAM_POST
                )

            )?.enqueue(object : Callback<ArrayList<ResponsePost>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponsePost>>,
                    response: Response<ArrayList<ResponsePost>>
                ) {
                    try {

                        onPostRetreived(response)

                    } catch (E: Exception) {
                        Log.wtf("get_post_error", E.toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponsePost>>, throwable: Throwable) {

                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun onPostRetreived(response: Response<ArrayList<ResponsePost>>) {

        try{

            if(adapternewsfeed==null) {
                arrayFeeds=response.body()!!
                adapternewsfeed= AdapterNewsFeed(arrayFeeds,this,this,childFragmentManager)
                val glm = GridLayoutManager(activity, 1)
                rvProfileFeeds.adapter=adapternewsfeed
                rvProfileFeeds.layoutManager=glm
            }else{
                val position = adapternewsfeed!!.newsFeeds.size
                arrayFeeds.addAll(response.body()!!)
                adapternewsfeed!!.notifyItemChanged(position)
            }

        }catch (e:Exception){}
        isLoading=response.body()!!.isEmpty()
        try{ Log.wtf("dateFeeds",dateFeeds) }catch (e:java.lang.Exception){

        }



    }

    private fun setFeeds(array: ArrayList<ResponsePost>) {
        arrayFeeds.clear()
        for (i in array.indices) {
            if (array[i].divType == AppConstants.TYPE_POST)
                arrayFeeds.add(array[i])
        }

    }

    private fun getPostById(position: Int, id: Int) {
        try {
            loading.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostById(
                id,
                1,
                "2019-11-16T12:27:32.52",
                false
            )?.enqueue(object : Callback<ArrayList<ResponseComments>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseComments>>,
                    response: Response<ArrayList<ResponseComments>>
                ) {
                    try {
                        loading.visibility = View.GONE
                        arrayFeeds[position].showComments = true
                        arrayFeeds[position].arrayComments = response.body()
                        adapternewsfeed!!.notifyDataSetChanged()

                    } catch (E: Exception) {
                        Log.wtf("get_post_error", E.toString())

                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseComments>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })

    }




    private fun followUser(id: Int,position:Int,type: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.follow(
                id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    isFollower = response.body()!!

                    if(position==-1){
                    if (isFollower)
                        addFollower()
                    else
                        removeFollower()
                    setFollowButton()
                    }else{

                    }
                }

                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun addFollower() {
        MyApplication.arrayFollowers.add(
            ResponseFollowers(
                0,
                userId,
                MyApplication.userLoginInfo.userName,
                MyApplication.userLoginInfo.image,
                true
            )
        )

       arrayFollowers.add(
            ResponseFollowers(
                0,
                userId,
                MyApplication.userLoginInfo.userName,
                MyApplication.userLoginInfo.image,
                true
            )
        )
        adapterFollower.notifyDataSetChanged()
    }

    private fun removeFollower() {
        for (i in MyApplication.arrayFollowers.indices) {
            if (MyApplication.arrayFollowers[i].userId == userId) {
                MyApplication.arrayFollowers.removeAt(i)
                break
            }

        }

        try {
            for (i in arrayFollowers.indices) {
                if (arrayFollowers[i].userId == userId) {
                    arrayFollowers.removeAt(i)
                    break
                }

            }
            adapterFollower.notifyDataSetChanged()
        }catch (e:java.lang.Exception){}
    }


    private fun like(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.setLike(
                id
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })

    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == com.ids.inpoint.R.id.linearDots) {
            arrayFeeds[position].settingsViewVisible = !arrayFeeds[position].settingsViewVisible!!
            adapternewsfeed!!.notifyDataSetChanged()
        }
        else if(view.id==R.id.ivImagePost){
            try{(activity!! as ActivityHome).showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![0].fileName.toString())}catch (e:Exception){ }
        }else if(view.id==R.id.ivImagePost2){
            try{(activity!! as ActivityHome).showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![1].fileName.toString())}catch (e:Exception){ }
        }else if(view.id==R.id.ivImagePost3){
            try{(activity!! as ActivityHome).showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![2].fileName.toString())}catch (e:Exception){ }
        }else if(view.id==R.id.ivImagePost4){
            try{(activity!! as ActivityHome).showImage(AppConstants.IMAGES_URL+ adapternewsfeed!!.newsFeeds[position].medias!![3].fileName.toString())}catch (e:Exception){ }
        }

        else if (view.id == com.ids.inpoint.R.id.linearLike) {
            like(arrayFeeds[position].id!!)
            arrayFeeds[position].liked = !arrayFeeds[position].liked!!
            if(arrayFeeds[position].liked!!)
                arrayFeeds[position].likeNumber=arrayFeeds[position].likeNumber!!+1
            else
                if(arrayFeeds[position].likeNumber!!>=1)
                    arrayFeeds[position].likeNumber=arrayFeeds[position].likeNumber!!-1
            adapternewsfeed!!.notifyDataSetChanged()
        } else if (view.id == com.ids.inpoint.R.id.linearComment) {
            /*       arrayFeeds[position].showComments=true
                   adapternewsfeed.notifyDataSetChanged()*/

            if (!adapternewsfeed!!.newsFeeds[position].showComments!!)
                getPostById(position, adapternewsfeed!!.newsFeeds[position].id!!)
        } else if (view.id == com.ids.inpoint.R.id.ctvCommentsCount) {
            MyApplication.selectedPost = adapternewsfeed!!.newsFeeds[position]
            (activity!! as ActivityHome).openComments(adapternewsfeed!!.newsFeeds[position].id!!)
        }

        else if(view.id==R.id.ivUser || view.id==R.id.ctvName){
            if(adapternewsfeed!!.newsFeeds[position].userId!=MyApplication.userLoginInfo.id){
             MyApplication.selectedPost = adapternewsfeed!!.newsFeeds[position]
                try{(activity!! as ActivityHome).goToOtherprofile(AppConstants.NEWS,AppConstants.OTHER_USER_PROFILE,AppConstants.OTHER_USER_PROFILE_FRAG)}catch (e:Exception){ }
                // startActivity(Intent(activity, ActivityProfile::class.java))
            }
        }
        else if(view.id==R.id.linearLikers){
            showUserDialog(AppConstants.POPUP_TYPE_LIKERS,adapternewsfeed!!.newsFeeds[position].id!!)
        }
        else if(view.id==R.id.btFollowFollower){
            adapterPopupFollowers!!.arrayUsers[position].isFollowed=!adapterPopupFollowers!!.arrayUsers[position].isFollowed!!
            adapterPopupFollowers!!.notifyDataSetChanged()
            followUser(adapterPopupFollowers!!.arrayUsers[position].userId!!,position,AppConstants.TYPE_FOLLOW_FOLLOWERS)
        }
        else if(view.id==R.id.btFollowLikers){
            adapterPopLikers!!.arrayUsers[position].isFollowed=!adapterPopLikers!!.arrayUsers[position].isFollowed!!
            adapterPopLikers!!.notifyDataSetChanged()
            followUser(adapterPopLikers!!.arrayUsers[position].userId!!,position,AppConstants.POPUP_TYPE_LIKERS)
        }

        else if (view.id == R.id.ivSend) {
            var etComment: EditText = rvProfileFeeds.findViewHolderForAdapterPosition(position)!!.itemView.findViewById(com.ids.inpoint.R.id.etComment) as EditText
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
            }  else{
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
        }

        else if(view.id==R.id.btHidePost){
/*            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            arrayFeeds.removeAt(position)
            adapternewsfeed!!.notifyDataSetChanged()*/

        }else if(view.id==R.id.btEditPost){
            // Toast.makeText(activity,"edit",Toast.LENGTH_LONG).show()
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            MyApplication.selectedPost=adapternewsfeed!!.newsFeeds[position]
            MyApplication.isPostEdit=true
            resetToolbar()
            if(arrayFeeds[position].divType==AppConstants.TYPE_POST){
                try{(activity!! as ActivityHome).writePost()}catch (e:Exception){ }
            }else{
                try{(activity!! as ActivityHome).createEvent()}catch (e:Exception){ }
            }
            adapternewsfeed!!.notifyDataSetChanged()
        }else if(view.id==R.id.btDeletePost){
            deletePostDialog(activity!!,position)
        }


        else if(view.id==R.id.LinearVerified){
            showVerifyPopup(adapternewsfeed!!.newsFeeds[position].id!!,position)
        }

        else if(view.id==R.id.linearShow){
            arrayFeeds[position].isShowMore=!arrayFeeds[position].isShowMore!!
            adapternewsfeed!!.notifyDataSetChanged()
        }

        else if (view.id == com.ids.inpoint.R.id.linearShare) {
            AppHelper.share(
                activity!!,
                arrayFeeds[position].title.toString(),
                arrayFeeds[position].title.toString()
            )
        }  else{
            MyApplication.selectedPost=adapternewsfeed!!.newsFeeds[position]
            (activity!! as ActivityHome).openComments(adapternewsfeed!!.newsFeeds[position].id!!)
        }
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


    private fun resetTypes(){
        for (i in MyApplication.arrayTypes.indices)
            MyApplication.arrayTypes[i].isSelected=false
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
            getAllPosts(pageUserId,userId)
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
        getAllPosts(pageUserId,userId)
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


    private fun setListeners(){
        //linearBadges.setOnClickListener{}
      //  linearLevel.setOnClickListener{showUserDialog(AppConstants.POPUP_TYPE_LIKERS,pageUserId)}
        linearPoints.setOnClickListener{showUserDialog(AppConstants.POPUP_TYPE_POINTS,pageUserId)}
        linearFollowers.setOnClickListener{showUserDialog(AppConstants.POPUP_TYPE_FOLLOWERS,pageUserId)}
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
            AppConstants.POPUP_TYPE_FOLLOWERS -> {
                tvPopupTitle.text=getString(R.string.followers)


                adapterPopupFollowers = AdapterPopupUsers(arrayFollowers, this,AppConstants.TYPE_FOLLOW_FOLLOWERS)
                val glm = GridLayoutManager(activity,1)
                rvPopUpUsers!!.adapter = adapterPopupFollowers
                rvPopUpUsers!!.layoutManager = glm


            }
            AppConstants.POPUP_TYPE_POINTS -> {
                tvPopupTitle.text=getString(R.string.points)
                getPoints(pageUserId)

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
        val glm = GridLayoutManager(activity,1)
        rvPopUpUsers!!.adapter = adapterPopLikers
        rvPopUpUsers!!.layoutManager = glm
    }


    private fun getPoints(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserPoints(id)?.enqueue(object : Callback<ArrayList<ResponsePoint>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponsePoint>>,
                    response: Response<ArrayList<ResponsePoint>>
                ) {
                    try {
                        setPoints(response.body()!!)

                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponsePoint>>,
                    throwable: Throwable
                ) {
                }
            })
    }

    private fun setPoints(response: ArrayList<ResponsePoint>){
        arrayPoints.clear()
        arrayPoints.addAll(response)
        adapterPopupPoints = AdapterPopupPoints(arrayPoints, this,1)
        val glm = GridLayoutManager(activity,1)
        rvPopUpUsers!!.adapter = adapterPopupPoints
        rvPopUpUsers!!.layoutManager = glm
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

    private fun resetCategories(){
        for (i in MyApplication.arrayCategories.indices)
            MyApplication.arrayCategories[i].isVerified=false
    }


    private fun sendVerification(postId:Int,position: Int){
        Log.wtf("adapter_size",adapterCategories!!.categories.size.toString())
        loading.visibility=View.VISIBLE
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




    private fun deleteTeam() {
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteTeam(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    (activity!! as ActivityHome).btTeams.performClick()
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(activity, "error..", Toast.LENGTH_LONG).show()
                }
            })
    }



    private fun deleteStartup() {
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartup(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    (activity!! as ActivityHome).btStartUp.performClick()
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    Toast.makeText(activity, "error..", Toast.LENGTH_LONG).show()
                }
            })
    }



    private fun getTeamMembers(id: Int,action:Boolean) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamMembers(id)?.enqueue(object : Callback<ArrayList<TeamStartupMember>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartupMember>>,
                    response: Response<ArrayList<TeamStartupMember>>
                ) {
                    try {

                        onTeamMembersReceived(response.body()!!,action)

                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<TeamStartupMember>>,
                    throwable: Throwable
                ) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onTeamMembersReceived(response: ArrayList<TeamStartupMember>,action: Boolean) {
        teamMembers = response
        tvActiveTeamMembers.text =
            "${teamMembers.count().toString()} ${getString(com.ids.inpoint.R.string.active)}"
        adapterTeamMembers = AdapterTeamMembers(teamMembers, this)
        val llm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTeamMembers.adapter = adapterTeamMembers
        rvTeamMembers.layoutManager = llm

        if(action){
            var isAdmin=false
            for (i in response.indices){
                if(response[i].userId==MyApplication.userLoginInfo.id && response[i].admin!!){
                    isAdmin=true
                    break
                }
            }
            MyApplication.selectedStartupTeam.admin=isAdmin
            setData()
        }

    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
    }





    private fun getStartUpMembers(id: Int,action: Boolean) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupMembers(id)?.enqueue(object : Callback<ArrayList<TeamStartupMember>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartupMember>>,
                    response: Response<ArrayList<TeamStartupMember>>
                ) {
                    try {

                        onStartupMembersRetrieved(response.body()!!,action)
                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<TeamStartupMember>>,
                    throwable: Throwable
                ) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onStartupMembersRetrieved(response: ArrayList<TeamStartupMember>,action: Boolean) {
        teamMembers = response
        tvActiveTeamMembers.text =
            "${teamMembers.count().toString()} ${getString(com.ids.inpoint.R.string.active)}"
        adapterTeamMembers = AdapterTeamMembers(teamMembers, this)
        val llm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTeamMembers.adapter = adapterTeamMembers
        rvTeamMembers.layoutManager = llm


        if(action){
            var isAdmin=false
            for (i in response.indices){
                if(response[i].userId==MyApplication.userLoginInfo.id && response[i].admin!!){
                    isAdmin=true
                    break
                }
            }
            MyApplication.selectedStartupTeam.admin=isAdmin
            setData()
        }
    }



    private fun setFollowButton() {

        if (isFollower) {
            if(myId==AppConstants.OTHER_USER_PROFILE)
                btChat.visibility = View.VISIBLE
            else
                btChat.visibility=View.GONE
            btFollow.setBackgroundResource(R.drawable.circle_secondary)
        } else {
            btChat.visibility = View.GONE
            btFollow.setBackgroundResource(R.drawable.circle_trans)
        }

    }





    private fun setShowToPublicButton() {

        if (showToPublic) {

            btShowPublic.setBackgroundResource(R.drawable.circle_secondary)
        } else {

            btShowPublic.setBackgroundResource(R.drawable.circle_trans)
        }

    }



    @Subscribe
    fun onMessageEvent(event: events) {

        if(event.message==AppConstants.DELETE_COMMENT){
            deleteCommentDialog(activity!!,event.postPosition,event.commentPosition,0,AppConstants.TYPE_COMMENT)

        }else if(event.message==AppConstants.DELETE_SUB_COMMENT){
            deleteCommentDialog(activity!!,event.postPosition,event.commentPosition,event.commentPosition,AppConstants.TYPE_COMMENT)

        }else if(event.message==AppConstants.REPLY_COMMENT){
            commentId= arrayFeeds[event.postPosition].arrayComments!![event.commentPosition].id.toString()
            commentPosition=event.commentPosition
            rvProfileFeeds.findViewHolderForAdapterPosition(event.postPosition)!!.itemView.etComment.requestFocus();
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


    fun deletePostDialog(c: Activity, position:Int) {

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




    fun comfirmDeleteTeam(c: Activity, position:Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_confirmation))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                deleteTeam()
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }
    

    fun deleteCommentDialog(c: Activity, postPosition:Int, commentPosition:Int, subCommentPosition:Int, type:Int) {

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



    override fun onDestroy() {
        super.onDestroy()
        adapternewsfeed=null
        Log.wtf("adapter_new_feed","null")
    }



    private fun showToPublic() {
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.changePrivacy(MyApplication.selectedStartupTeam.id!!,showToPublic)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try {
                        loading.visibility=View.GONE
                        setShowToPublicButton()
                        MyApplication.selectedStartupTeam.showToPublic=showToPublic
                    }catch (e:java.lang.Exception){

                    }
                }

                override fun onFailure(call: Call<Void>,throwable: Throwable) {

                }
            })
    }



    private fun showTeamInfoEdit() {


        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_team_name_bio)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout
        etTeamName = dialog!!.findViewById<View>(R.id.etTeamName) as EditText
        etTeamBio= dialog!!.findViewById<View>(R.id.etBio) as EditText
        etTeamName!!.setText(MyApplication.selectedStartupTeam.userName)
        etTeamBio!!.setText(MyApplication.selectedStartupTeam.bio)

        btSaveChanges!!.setOnClickListener {
                    updateUsernameBio(etTeamName!!.text.toString(),etTeamBio!!.text.toString())
                    dialog!!.dismiss()
        }

        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()


       }





    private fun updateUsernameBio(name:String,bio:String) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updateUsernameBio(
                name,
                bio,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try {
                        MyApplication.selectedStartupTeam.userName = name
                        tvUsername.text = name
                    } catch (e: Exception) {
                    }
                    try {
                        MyApplication.selectedStartupTeam.bio = bio
                        tvBio.text = bio
                    } catch (e: Exception) {
                    }

                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    try{ loading.visibility=View.GONE}catch (e:java.lang.Exception){}
                }
            })

    }




    private fun getById(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserDetails(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ResponseStartupProfile> {
                override fun onResponse(call: Call<ResponseStartupProfile>, response: Response<ResponseStartupProfile>) {

                    try{
                         tvBio.text = response.body()!!.bio
                         MyApplication.selectedStartupTeam.bio=response.body()!!.bio
                      //  MyApplication.selectedStartupTeam.bio=response.body()!!.bio
                    }catch (E: java.lang.Exception){}

                }
                override fun onFailure(call: Call<ResponseStartupProfile>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }






    private fun getInfoById(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserProfile(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ResponseUserInfos> {
                override fun onResponse(call: Call<ResponseUserInfos>, response: Response<ResponseUserInfos>) {

                    try{
                        MyApplication.selectedStartupTeam.bio=response.body()!!.bio

                        MyApplication.selectedStartupTeam.showToPublic=response.body()!!.showToPublic
                        MyApplication.selectedStartupTeam.image=response.body()!!.image
                        MyApplication.selectedStartupTeam.userName=response.body()!!.userName
                        MyApplication.selectedStartupTeam.coverImage=response.body()!!.coverImage



                         when(myId) {
                             AppConstants.TEAM_PROFILE -> {
                                 pageUserId = MyApplication.selectedStartupTeam.id!!
                                 getTeamMembers(pageUserId,true)
                             }
                             AppConstants.STARTUP_PROFILE -> {
                                 pageUserId = MyApplication.selectedStartupTeam.id!!
                                 getStartUpMembers(pageUserId,true)
                             }
                             AppConstants.OTHER_USER_PROFILE, AppConstants.DEFAULT_OTHER_USER_PROFILE -> {
                                 pageUserId = MyApplication.selectedPost.userId!!
                              }
                             else -> {
                                 pageUserId = MyApplication.userLoginInfo.id!!


                             }
                         }





                        //  MyApplication.selectedStartupTeam.bio=response.body()!!.bio
                    }catch (E: java.lang.Exception){}

                }
                override fun onFailure(call: Call<ResponseUserInfos>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setData(){
        when (myId) {
            AppConstants.TEAM_PROFILE -> {
                linearStartupAdminSettings.visibility=View.GONE
                setCommonStartupTeam()
                getById()
                btShowPublic.visibility=View.VISIBLE
                btEditTeamInfo.visibility=View.VISIBLE
                // try{tvBio.text=MyApplication.selectedStartupTeam.bio!!}catch (e:java.lang.Exception){}
                getTeamMembers(pageUserId,false)
                btSeeAllTeamMembers.setOnClickListener {
                    resetToolbar()
                    startActivity(Intent(activity, ActivityTeamMembers::class.java))
                }
                tvStartupTeamMember.text=getString(R.string.team_members)

                showToPublic=MyApplication.selectedStartupTeam.showToPublic!!
                setShowToPublicButton()


                setTeamAdminSettings()
                linearTeamAdminSettings.visibility = View.VISIBLE
                if(MyApplication.selectedStartupTeam.admin!!) {


                    rlProfileImage.setOnClickListener {
                        try {
                            resetToolbar()
                            (activity!! as ActivityHome).previewFragment = myId
                        } catch (e: Exception) {
                        }
                        MyApplication.isTeamChat=true
                        MyApplication.previews_frag=myId
                        AppHelper.AddFragment(
                            this.fragmentManager!!,
                            AppConstants.EDIT_PROFILE_IMAGE,
                            FragmentUploadImage(),
                            AppConstants.EDIT_PROFILE_IMAGE_FRAG
                        )
                    }
                    btDelete.setOnClickListener{
                        comfirmDeleteTeam(activity!!,0)
                    }

                    btShowPublic.setOnClickListener{
                        showToPublic=!showToPublic
                        showToPublic()
                    }

                    linearNameBio.setOnClickListener{
                        showTeamInfoEdit()
                    }


                    btEditProfile.setOnClickListener{
                        try {
                            resetToolbar()
                            (activity!! as ActivityHome).previewFragment = myId
                        } catch (e: Exception) {
                        }
                        MyApplication.isTeamChat=true
                        MyApplication.previews_frag=myId
                        AppHelper.AddFragment(
                            this.fragmentManager!!,
                            AppConstants.EDIT_COVER_IMAGE,
                            FragmentUploadBg(),
                            AppConstants.EDIT_COVER_IMAGE_FRAG
                        )
                    }
                }


                btChat.setOnClickListener {
                    resetToolbar()
                    MyApplication.pageUserId = pageUserId
                    MyApplication.isTeamChat=true
                    startActivity(Intent(activity, ActivityChat::class.java))
                }

                btChat.visibility=View.GONE

            }



            AppConstants.STARTUP_PROFILE -> {
                linearTeamAdminSettings.visibility=View.GONE
                setCommonStartupTeam()
                getStartUpMembers(pageUserId,false)
                linearInterested.visibility=View.VISIBLE
                btDelete.setOnClickListener{deleteStartup()}
                tvStartupTeamMember.text=getString(R.string.startup_members)
                btSeeAllTeamMembers.setOnClickListener {
                    resetToolbar()
                    startActivity(Intent(activity, ActivityStartupMembers::class.java))
                }


                if(MyApplication.selectedStartupTeam.admin!!) {
                    setStartupAdminSettings()
                    linearStartupAdminSettings.visibility = View.VISIBLE
                    rlProfileImage.setOnClickListener {
                        try {
                            resetToolbar()
                            (activity!! as ActivityHome).previewFragment = myId
                        } catch (e: Exception) {
                        }
                        MyApplication.isTeamChat=true
                        MyApplication.previews_frag=myId
                        AppHelper.AddFragment(
                            this.fragmentManager!!,
                            AppConstants.EDIT_PROFILE_IMAGE,
                            FragmentUploadImage(),
                            AppConstants.EDIT_PROFILE_IMAGE_FRAG
                        )
                    }

                    btEditProfile.setOnClickListener{
                        try {
                            resetToolbar()
                            (activity!! as ActivityHome).previewFragment = myId
                        } catch (e: Exception) {
                        }
                        MyApplication.isTeamChat=true
                        MyApplication.previews_frag=myId
                        AppHelper.AddFragment(
                            this.fragmentManager!!,
                            AppConstants.EDIT_COVER_IMAGE,
                            FragmentUploadBg(),
                            AppConstants.EDIT_COVER_IMAGE_FRAG
                        )
                    }
                }else
                    linearStartupAdminSettings.visibility = View.GONE


                btChat.setOnClickListener {
                    resetToolbar()
                    MyApplication.pageUserId = pageUserId
                    MyApplication.isTeamChat=true
                    startActivity(Intent(activity, ActivityChat::class.java))
                }

                btChat.visibility=View.GONE
            }




            AppConstants.OTHER_USER_PROFILE , AppConstants.DEFAULT_OTHER_USER_PROFILE -> {
                pageUserId=MyApplication.selectedPost.userId!!
                try{followerImage=MyApplication.selectedPost.image!!}catch (e:java.lang.Exception){}
                try{followerName=MyApplication.selectedPost.userName!!}catch (e:java.lang.Exception){}
                getFollowers(pageUserId,false)
                btBack.visibility=View.VISIBLE
                getUserInfo(pageUserId)
                btEditProfile.visibility=View.GONE
                linearEditProfileImg.visibility=View.GONE
                linearTeamAdminSettings.visibility=View.GONE
                linearStartupAdminSettings.visibility=View.GONE
                setDatafollowers()
                btChat.setOnClickListener {
                    resetToolbar()
                    MyApplication.pageUserId = pageUserId
                    MyApplication.isTeamChat=false
                    startActivity(Intent(activity, ActivityChat::class.java))
                }

            }
            else -> {
                btBack.visibility=View.GONE
                linearEditProfileImg.visibility=View.VISIBLE
                btEditProfile.visibility=View.VISIBLE
                btFollow.visibility=View.GONE
                btChat.visibility=View.VISIBLE
                btChat.setOnClickListener {
                    resetToolbar()
                    ivChat.performClick()
                }
                btNotification.visibility=View.VISIBLE
                btNotification.setOnClickListener{
                    resetToolbar()
                    ivNotification.performClick()
                }
                linearTeamAdminSettings.visibility=View.GONE
                linearStartupAdminSettings.visibility=View.GONE
                pageUserId=MyApplication.userLoginInfo.id!!
                getFollowers(pageUserId,false)
                setInfo(MyApplication.userLoginInfo)
                btEditProfile.setOnClickListener {
                    try {
                        resetToolbar()
                        (activity!! as ActivityHome).previewFragment = AppConstants.PROFILE
                    } catch (e: Exception) {
                    }
                    AppHelper.AddFragment(
                        this.fragmentManager!!,
                        AppConstants.EDIT_PROFILE,
                        FragmentEditProfile(),
                        AppConstants.EDIT_PROFILE_FRAG
                    )
                }

                rlProfileImage.setOnClickListener {
                    try {
                        resetToolbar()
                        (activity!! as ActivityHome).previewFragment = myId
                    } catch (e: Exception) {
                    }
                    MyApplication.isTeamChat=false
                    AppHelper.AddFragment(
                        this.fragmentManager!!,
                        AppConstants.EDIT_PROFILE_IMAGE,
                        FragmentUploadImage(),
                        AppConstants.EDIT_PROFILE_IMAGE_FRAG
                    )
                }

            }
        }


        Handler().postDelayed({
            /*            if (myId == AppConstants.TEAM_PROFILE) {
                           getAllPosts(userId, MyApplication.userLoginInfo.id!!)
                        } else {*/
            getAllPosts(pageUserId,userId)
            // }

        }, 100)

        arrayLikers.clear()


    }


}
