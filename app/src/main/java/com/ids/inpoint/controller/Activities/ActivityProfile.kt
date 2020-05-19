package com.ids.inpoint.controller.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterFollowers
import com.ids.inpoint.controller.Adapters.AdapterNewsFeed
import com.ids.inpoint.controller.Adapters.AdapterTeamMembers
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.Fragments.FragmentUploadImage
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.News_feed
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.model.comments
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.fragment_fragment_profile.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityProfile : AppCompactBase(), RVOnItemClickListener, RVOnSubItemClickListener {

    private lateinit var navigationType: String
    override fun onSubItemClicked(view: View, position: Int, parentPosition: Int) {


    }

    private lateinit var adapterFollower: AdapterFollowers
    private var arrayFollowers = java.util.ArrayList<ResponseFollowers>()

    private lateinit var adapterTeamMembers: AdapterTeamMembers
    private var arrayTeamMembers = java.util.ArrayList<TeamStartupMember>()


    lateinit var adapternewsfeed: AdapterNewsFeed


    var arrayFeeds = java.util.ArrayList<ResponsePost>()


    var arrayComments = java.util.ArrayList<comments>()
    var arraySubComments1 = java.util.ArrayList<comments>()
    var arraySubComments2 = java.util.ArrayList<comments>()
    var arrayComments2 = java.util.ArrayList<comments>()
    var arrayComments3 = java.util.ArrayList<comments>()
    var newsFeed: News_feed? = null
    var newsFeed2: News_feed? = null
    var newsFeed3: News_feed? = null
    lateinit var imageFilePath: String
    var comment1: comments? = null
    var comment2: comments? = null

    var subComment1: comments? = null
    var subComment2: comments? = null
    var percentage = 0.0
    private var isFollower = false
    lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_fragment_profile)
        init()
    }

    private fun init() {
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
        btBack.visibility = View.VISIBLE
        btBack.setOnClickListener { super.onBackPressed() }

        // Check if this is a TeamStartup navigation
        navigationType = try {
            intent.getStringExtra("AppConstants.NAVIGATION_TYPE")
        } catch (ex: Exception) {
            ""
        }

        if (navigationType == "AppConstants.NAVIGATION_TYPE_TEAM") {
            setTeamDataAndAppearance()
        } else {
            if (MyApplication.selectedPost.userId != null)
                setData()
            else {
                /*   MyApplication.selectedPost.userId==intent.getIntExtra(AppConstants.USER_ID,0)
                   MyApplication.selectedPost.image==intent.getStringExtra(AppConstants.USER_IMAGE)
                   setData()*/
            }

            linearEditProfileImg.visibility = View.GONE
            btEditProfile.visibility = View.GONE

            rvProfileFeeds.isNestedScrollingEnabled = false
            // setNewsFeed()
            setToolbarScrollAnimation()

            rlProfileImage.setOnClickListener {
                AppHelper.AddFragment(
                    this.fragmentManager!!,
                    AppConstants.EDIT_PROFILE_IMAGE,
                    FragmentUploadImage(),
                    AppConstants.EDIT_PROFILE_IMAGE_FRAG
                )
            }
        }
    }

    private fun setTeamDataAndAppearance() {
        llFollowers.visibility = View.GONE
        llMembers.visibility = View.VISIBLE

        if (MyApplication.selectedStartupTeam.admin != true) {
            btEditProfile.visibility = View.GONE
            linearEditProfileImg.visibility = View.GONE
        } else {
            rlProfileImage.setOnClickListener {
                try {
                    AppHelper.AddFragment(
                        this.fragmentManager!!,
                        AppConstants.EDIT_PROFILE_IMAGE,
                        FragmentUploadImage(),
                        AppConstants.EDIT_PROFILE_IMAGE_FRAG
                    )
                } catch (ex: Exception) {
                    var t = ex
                }
            }
        }

        getUserInfo(MyApplication.selectedStartupTeam.id!!)
        getTeamMembers(MyApplication.selectedStartupTeam.id!!)
    }

    private fun setData() {
        getUserInfo(MyApplication.selectedPost.userId!!)
        getFollowers(MyApplication.selectedPost.userId!!, false)

        if (MyApplication.arrayFollowers.size == 0)
            getFollowers(MyApplication.userLoginInfo.id!!, true)
        else {
            checkFollwer()
            btFollow.setOnClickListener {
                isFollower = !isFollower
                setFollowButton()
                followUser(MyApplication.selectedPost.userId!!)

            }
        }

        btChat.setOnClickListener {
            MyApplication.pageUserId = MyApplication.selectedPost.userId!!
            startActivity(Intent(this, ActivityChat::class.java))
        }
    }

    private fun setFollowButton() {
        if (isFollower) {
            btChat.visibility = View.VISIBLE
            btFollow.setBackgroundResource(R.drawable.circle_secondary)
        } else {
            btChat.visibility = View.GONE
            btFollow.setBackgroundResource(R.drawable.circle_trans)
        }
    }

    override fun onResume() {
        Handler().postDelayed({
            if (navigationType == "AppConstants.NAVIGATION_TYPE_TEAM") {
            } else if (MyApplication.selectedPost.userId != null)
                getAllPosts()
            else {
                MyApplication.selectedPost.userId == intent.getIntExtra(AppConstants.USER_ID, 0)
                MyApplication.selectedPost.image == intent.getStringExtra(AppConstants.USER_IMAGE)
                getAllPosts()
            }
        }, 200)
        super.onResume()
    }

    private fun setToolbarScrollAnimation() {

        scrollProfile.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
            try {
                var scrollY = scrollProfile.scrollY // For ScrollView
                Log.wtf("scroll_position", scrollY.toString())
                Log.wtf(
                    "scroll_position_dp",
                    convertPixelsToDp(scrollY.toFloat(), applicationContext!!).toString()
                )
                if (convertPixelsToDp(scrollY.toFloat(), applicationContext!!) < 240) {
                    llToolbar.visibility = View.GONE
                } else if (convertPixelsToDp(
                        scrollY.toFloat(),
                        applicationContext!!
                    ) in 240.0..300.0
                ) {
                    //animate visibility
                    llToolbar.visibility = View.VISIBLE
                    percentage =
                        (((convertPixelsToDp(
                            scrollY.toFloat(),
                            applicationContext!!
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
                        response.body()!!
                        try {
                            setInfo(response.body()!!)
                        } catch (E: java.lang.Exception) {
                        }
                    } catch (E: Exception) {
                        Log.wtf("get_post_error", E.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseUserInfos>, throwable: Throwable) {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun setInfo(body: ResponseUserInfos) {
        if (navigationType == "AppConstants.NAVIGATION_TYPE_TEAM") {

            try {
                AppHelper.setRoundImageResize(
                    applicationContext!!,
                    ivUserProfile,
                    AppConstants.IMAGES_URL + "/" + body.image!!,
                    MyApplication.isProfileImageLocal
                )
            } catch (E: Exception) {
            }

        } else {
            Log.wtf("profile_image", MyApplication.selectedPost.image)

            try {
                AppHelper.setRoundImageResize(
                    applicationContext!!,
                    ivUserProfile,
                    AppConstants.IMAGES_URL + "/" + MyApplication.selectedPost.image!!,
                    MyApplication.isProfileImageLocal
                )
            } catch (E: Exception) {
            }
            try {
                AppHelper.setRoundImageResize(
                    applicationContext!!,
                    ivToolbarProfile,
                    AppConstants.IMAGES_URL + "/" + MyApplication.selectedPost.image!!,
                    MyApplication.isProfileImageLocal
                )
            } catch (E: java.lang.Exception) {
            }
        }

        try {
            AppHelper.setImage(
                applicationContext!!,
                coverImage,
                AppConstants.IMAGES_URL + "/" + body.coverImage!!
            )
        } catch (E: Exception) {
        }

        try {
            tvBadgesValue.text = body.badges.toString()
        } catch (E: Exception) {
        }

        try {
            tvLevelValue.text = body.level.toString()
        } catch (E: Exception) {
        }

        try {
            tvPointsValue.text = body.points.toString()
        } catch (E: Exception) {
        }

        try {
            tvFollowersValue.text = body.numberOfFollowers.toString()
        } catch (E: Exception) {
        }

        try {
            tvUsername.text = body.userName.toString()
        } catch (E: Exception) {
        }
    }

    private fun getFollowers(id: Int, isMyAccount: Boolean) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getFollowers(id)?.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseFollowers>>,
                    response: Response<ArrayList<ResponseFollowers>>
                ) {
                    try {
                        if (isMyAccount) {
                            MyApplication.arrayFollowers.clear()
                            MyApplication.arrayFollowers = response.body()!!
                            checkFollwer()
                            btFollow.setOnClickListener {
                                followUser(MyApplication.selectedPost.userId!!)
                                isFollower = !isFollower
                                setFollowButton()
                            }
                        } else
                            setFollowers(response.body()!!)
                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseFollowers>>,
                    throwable: Throwable
                ) {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun checkFollwer() {
        isFollower = false
        for (i in MyApplication.arrayFollowers.indices) {
            if (MyApplication.selectedPost.userId == MyApplication.arrayFollowers[i].userId) {
                isFollower = true
                break
            }
        }
        setFollowButton()
    }

    private fun setFollowers(response: ArrayList<ResponseFollowers>) {
        arrayFollowers = response
        adapterFollower = AdapterFollowers(arrayFollowers, this)
        // val glm = GridLayoutManager(applicationContext, 5)
        val llm = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rvFollowers1.adapter = adapterFollower
        rvFollowers1.layoutManager = llm
    }

    private fun getAllPosts() {
        var dateFeeds = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllPost(
                //  MyApplication.userLoginInfo.token.toString(),
                JsonParameters(
                    MyApplication.selectedPost.userId!!,
                    MyApplication.userLoginInfo.id!!,
                    2,
                    "",
                    "",
                    "",
                    "",
                    1,
                    dateFeeds,
                    0,
                    10,
                    "",
                    false,
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

                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onPostRetreived(response: Response<ArrayList<ResponsePost>>) {
        setFeeds(response.body()!!)
        adapternewsfeed = AdapterNewsFeed(arrayFeeds, this, this, supportFragmentManager)
        val glm = GridLayoutManager(applicationContext, 1)
        rvProfileFeeds.adapter = adapternewsfeed
        rvProfileFeeds.layoutManager = glm
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
                        adapternewsfeed.notifyDataSetChanged()

                    } catch (E: Exception) {
                        Log.wtf("get_post_error", E.toString())

                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseComments>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun like(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.setLike(
                id
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == com.ids.inpoint.R.id.linearDots) {
            arrayFeeds[position].settingsViewVisible = !arrayFeeds[position].settingsViewVisible!!
            adapternewsfeed.notifyDataSetChanged()
        } else if (view.id == com.ids.inpoint.R.id.ivImagePost) {
            try {
                (applicationContext!! as ActivityHome).showImage(AppConstants.IMAGES_URL + adapternewsfeed.newsFeeds[position].medias!![0].fileName.toString())
            } catch (e: Exception) {
            }
        } else if (view.id == com.ids.inpoint.R.id.linearLike) {
            like(arrayFeeds[position].id!!)
            arrayFeeds[position].liked = !arrayFeeds[position].liked!!
            adapternewsfeed.notifyDataSetChanged()
        } else if (view.id == com.ids.inpoint.R.id.linearComment) {
            /*       arrayFeeds[position].showComments=true
                   adapternewsfeed.notifyDataSetChanged()*/

            if (!adapternewsfeed.newsFeeds[position].showComments!!)
                getPostById(position, adapternewsfeed.newsFeeds[position].id!!)
        } else if (view.id == com.ids.inpoint.R.id.ctvCommentsCount) {
            MyApplication.selectedPost = adapternewsfeed.newsFeeds[position]
            (applicationContext!! as ActivityHome).openComments(adapternewsfeed.newsFeeds[position].id!!)
        } else if (view.id == com.ids.inpoint.R.id.ivSend) {
            var etComment: EditText =
                rvProfileFeeds.findViewHolderForAdapterPosition(position)!!.itemView.findViewById(
                    com.ids.inpoint.R.id.etComment
                ) as EditText
            comment1 = comments(
                "1",
                "Dany Soleiman",
                "https://www.eharmony.co.uk/dating-advice/wp-content/uploads/2018/06/datingprofile2-900x600.jpg",
                etComment.text.toString(),
                "now", 0, false, false, arraySubComments1
            )

            if (position == 0) {
                arrayComments.add(comment1!!)
            } else if (position == 1) {
                arrayComments2.add(comment1!!)
            } else if (position == 2) {
                arrayComments3.add(comment1!!)
            }

            adapternewsfeed.notifyDataSetChanged()
            etComment.setText("")
            // Toast.makeText(applicationContext,"aaaaaa",Toast.LENGTH_LONG).show()
        } else if (view.id == com.ids.inpoint.R.id.linearShare) {
            AppHelper.share(
                applicationContext!!,
                arrayFeeds[position].title.toString(),
                arrayFeeds[position].title.toString()
            )
        }
    }

    private fun followUser(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.follow(
                id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    isFollower = response.body()!!

                    if (isFollower)
                        addFollower()
                    else
                        removeFollower()
                    setFollowButton()
                }

                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun addFollower() {
        MyApplication.arrayFollowers.add(
            ResponseFollowers(
                0,
                MyApplication.selectedPost.userId,
                MyApplication.selectedPost.userName,
                MyApplication.selectedPost.image,
                true
            )
        )
    }

    private fun removeFollower() {
        for (i in MyApplication.arrayFollowers.indices) {
            if (MyApplication.arrayFollowers[i].userId == MyApplication.selectedPost.userId) {
                MyApplication.arrayFollowers.removeAt(i)
                break
            }
        }
    }


    private fun getTeamMembers(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamMembers(id)?.enqueue(object : Callback<ArrayList<TeamStartupMember>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartupMember>>,
                    response: Response<ArrayList<TeamStartupMember>>
                ) {
                    try {

                        onTeamMembersReceived(response.body()!!)
                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<TeamStartupMember>>,
                    throwable: Throwable
                ) {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }
    private fun onTeamMembersReceived(response: ArrayList<TeamStartupMember>) {
        arrayTeamMembers = response
        tvActiveTeamMembers.text = "${arrayTeamMembers.count().toString()} ${getString(R.string.active)}"
        adapterTeamMembers = AdapterTeamMembers(arrayTeamMembers, this)
        val llm = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rvTeamMembers.adapter = adapterTeamMembers
        rvTeamMembers.layoutManager = llm
    }
}
