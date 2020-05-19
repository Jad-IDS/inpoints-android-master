package com.ids.inpoint.controller.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View

import kotlinx.android.synthetic.main.footer.*
import android.view.animation.AlphaAnimation
import android.content.Intent
import android.content.res.Configuration
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import com.ids.inpoint.R
import com.ids.inpoint.controller.Fragments.*
import com.ids.inpoint.utils.AppHelper.Companion.fragmentAvailable
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseLocations
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ActivityHome : FragmentActivity() {

    init {
       LocaleUtils.updateConfig(this)
    }

    lateinit var fragmentManager: FragmentManager

    var previewFragment=AppConstants.NEWS
    private val buttonClick = AlphaAnimation(1f, 0.8f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ids.inpoint.R.layout.activity_home)
        init()
        try{Log.wtf("intent_frag",intent.extras.getString(AppConstants.DEFAULT_FRAG,AppConstants.NEWS_FEED_FRAG))}catch (e:Exception){}
        try{
            when {
                intent.extras.getString(AppConstants.DEFAULT_FRAG,AppConstants.NEWS_FEED_FRAG)==AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG -> defaultOtherProfileFrag()
                intent.extras.getString(AppConstants.DEFAULT_FRAG,AppConstants.NEWS_FEED_FRAG)==AppConstants.DEFAULT_MY_PROFILE_FRAG -> defaultMyProfileFrag()
                else -> defaultFragment()
            }
       }catch (e:Exception){
            defaultFragment()
        }

        setTabs()
        getlocations()
        getLocationsSubLoc()
        getGeneralLookup()

    }

    private fun init(){
      // supportActionBar!!.hide()
       // LocaleUtils.setLocale(Locale("ar","LB"))
        AppHelper.setLocal(this)
        fragmentManager = supportFragmentManager


     }

    private fun defaultFragment(){
        resetTabs()
        ivFooterNewsFeed.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY);
        fragmentAvailable=AppConstants.NEWS
        val newsFeedFrag = FragmentNewsFeed()
        fragmentManager.beginTransaction()
            .add(R.id.container, newsFeedFrag, AppConstants.NEWS_FEED_FRAG)
            .commit()
    }


    private fun defaultOtherProfileFrag(){
        resetTabs()
        ivFooterProfile.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
        fragmentAvailable=AppConstants.DEFAULT_OTHER_USER_PROFILE
        val profile = FragmentProfile()
        fragmentManager.beginTransaction()
            .add(R.id.container, profile, AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG)
            .commit()
    }

    private fun defaultMyProfileFrag(){
        resetTabs()
        ivFooterProfile.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY);
        fragmentAvailable=AppConstants.DEFAULT_MY_PROFILE
        val profile = FragmentProfile()
        fragmentManager.beginTransaction()
            .add(R.id.container, profile, AppConstants.DEFAULT_MY_PROFILE_FRAG)
            .commit()
    }


    private fun resetTabs(){

        ivFooterProfile.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        ivFooterElearning.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        ivFooterNewsFeed.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        ivFooterTeams.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        ivFooterSettings.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        ivFooterStartup.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
    /*    ivFooterProfile.setImageResource(R.drawable.ic_profile)
        ivFooterElearning.setImageResource(R.drawable.tab)
        ivFooterNewsFeed.setImageResource(R.drawable.newsfeed)
        ivFooterTeams.setImageResource(R.drawable.ic_team)
        ivFooterSettings.setImageResource(R.drawable.option_bar)*/
    }

    private fun setTabs(){


        btNewsfeed.setOnClickListener(View.OnClickListener { v ->
          //if(fragmentAvailable!=AppConstants.NEWS) {
              AppHelper.resetMemory()
               AppHelper.ReplaceFragment(fragmentManager,AppConstants.NEWS,FragmentNewsFeed(), AppConstants.NEWS_FEED_FRAG)
              resetTabs()
              ivFooterNewsFeed.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
         // }
        })


        btProfile.setOnClickListener(View.OnClickListener { v ->
            if(fragmentAvailable!=AppConstants.PROFILE) {
                AppHelper.resetMemory()
                AppHelper.ReplaceFragment(fragmentManager,AppConstants.PROFILE,FragmentProfile(), AppConstants.PROFILE_FRAG)
                resetTabs()
                ivFooterProfile.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        })



        btSettings.setOnClickListener(View.OnClickListener { v ->

           if(fragmentAvailable!=AppConstants.SETTINGS) {
               AppHelper.resetMemory()
               AppHelper.ReplaceFragment(fragmentManager, AppConstants.SETTINGS, FragmentSettings(), AppConstants.SETTINGS_FRAG)
               resetTabs()
               ivFooterSettings.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
//               v.startAnimation(buttonClick)
           }
        })




        btElearning.setOnClickListener(View.OnClickListener { v ->
            if(fragmentAvailable!=AppConstants.ELEARNING) {
                AppHelper.resetMemory()
                AppHelper.ReplaceFragment(fragmentManager,AppConstants.ELEARNING,FragmentElearning(), AppConstants.ELEARNING_FRAG)
                resetTabs()
                ivFooterElearning.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        })



        btTeams.setOnClickListener{
            if(fragmentAvailable != AppConstants.TEAM){
                AppHelper.resetMemory()
                AppHelper.ReplaceFragment(fragmentManager, AppConstants.TEAM, FragmentTeams(), AppConstants.TEAM_FRAG)
                resetTabs()
                ivFooterTeams.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        }


        btStartUp.setOnClickListener{
            if(fragmentAvailable != AppConstants.STARTUP){
                AppHelper.resetMemory()
                AppHelper.ReplaceFragment(fragmentManager, AppConstants.STARTUP, FragmentStartup(), AppConstants.STARTUP_FRAG)
                resetTabs()
                ivFooterStartup.setColorFilter(ContextCompat.getColor(this, R.color.secondary), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    fun goProfile(){
        btProfile.performClick()
    }

    fun openComments(id:Int){
        startActivity(Intent(this,ActivityInsideComment::class.java).putExtra(AppConstants.POST_ID,id))
    }


     fun showImage(url:String){
         if(fragmentAvailable==AppConstants.EVENTS)
             previewFragment=AppConstants.EVENTS
         else
             previewFragment=AppConstants.NEWS
        fragmentAvailable=AppConstants.IMAGE
        val imageFrag = FragmentImage()

         val bundle = Bundle()
         bundle.putString(AppConstants.URL, url)
         imageFrag.arguments = bundle
         fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, imageFrag, AppConstants.IMAGE_FRAG)
            .commit()
    }


    fun removeFrag(tag:String){
        fragmentAvailable=previewFragment
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null)
            supportFragmentManager.beginTransaction().remove(fragment).commit()
    }

    fun writePost(){
        fragmentAvailable=AppConstants.WRITE_POST
        val postFrag = FragmentPost()
        fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, postFrag, AppConstants.POST_FRAG)
            .commit()
    }

    fun createEvent(){
        fragmentAvailable=AppConstants.CREATE_EVENT
        val postFrag = FragmentCreateEvent()
        fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, postFrag, AppConstants.CREATE_EVENT_FRAG)
            .commit()
    }


    fun goToUserChat(previews:Int){
        previewFragment=previews
        fragmentAvailable=AppConstants.CHAT
        val postFrag = FragmentUsersChat()
        fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, postFrag, AppConstants.CHAT_FRAG)
            .commit()
    }

    fun goToOtherprofile(previews:Int,id:Int,fragmentTag:String){
        previewFragment=previews
        fragmentAvailable=id
        val frag = FragmentProfile()
        fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, frag, fragmentTag)
            .commit()
    }


    fun goNotification(previews:Int){
        previewFragment=previews
        fragmentAvailable=AppConstants.NOTIFICATION
        val postFrag = FragmnetNotification()
        fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, postFrag, AppConstants.NOTIFICATION_FRAG)
            .commit()
    }


    fun openSearch(previews:Int){
        previewFragment=previews
        fragmentAvailable=AppConstants.SEARCH
        val postFrag = FragmentSearch()
        fragmentManager.beginTransaction()
            .add(com.ids.inpoint.R.id.container, postFrag, AppConstants.SEARCH_FRAG)
            .commit()
    }


    override fun onBackPressed() {
        try{
            if(fragmentAvailable==AppConstants.IMAGE)
               removeFrag(AppConstants.IMAGE_FRAG)
            else if(fragmentAvailable==AppConstants.WRITE_POST) {
                removeFrag(AppConstants.POST_FRAG)
                try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.NEWS_FEED_FRAG) as FragmentNewsFeed?
                    fragment!!.reloadData()
                }catch (e:Exception){
                    try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.PROFILE_FRAG) as FragmentProfile?
                        fragment!!.reloadData()
                    }catch (e:Exception){
                        try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.TEAM_PROFILE_FRAG) as FragmentProfile?
                            fragment!!.reloadData()
                        }catch (e:Exception){
                            try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.STARTUP_PROFILE_FRAG) as FragmentProfile?
                                fragment!!.reloadData()}catch (e:Exception){}
                        }

                    }
                }
            }
            else if(fragmentAvailable==AppConstants.CREATE_EVENT) {
                removeFrag(AppConstants.CREATE_EVENT_FRAG)
                try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.NEWS_FEED_FRAG) as FragmentNewsFeed?
                    fragment!!.reloadData()}catch (e:Exception){

                    try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.PROFILE_FRAG) as FragmentProfile?
                        fragment!!.reloadData()}catch (e:Exception){

                        try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.TEAM_PROFILE_FRAG) as FragmentProfile?
                            fragment!!.reloadData()}catch (e:Exception){
                            try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.STARTUP_PROFILE_FRAG) as FragmentProfile?
                                fragment!!.reloadData()
                            }catch (e:Exception){
                            }
                        }
                    }

                }
            }
            else if(fragmentAvailable==AppConstants.NOTIFICATION) {
                removeFrag(AppConstants.NOTIFICATION_FRAG)
            }
            else if(fragmentAvailable==AppConstants.SEARCH) {
                removeFrag(AppConstants.SEARCH_FRAG)
            }
            else if(fragmentAvailable==AppConstants.CHAT) {
                removeFrag(AppConstants.CHAT_FRAG)
            }
            else if(fragmentAvailable==AppConstants.COURSES) {
                val fragment = fragmentManager.findFragmentByTag(AppConstants.COURSES_FRAG) as FragmentCourses?
                fragment!!.disableFullScreen()
            }  else if(fragmentAvailable==AppConstants.EDIT_PROFILE){
                removeFrag(AppConstants.EDIT_PROFILE_FRAG)
                previewFragment=AppConstants.PROFILE
            }
            else if(fragmentAvailable==AppConstants.EDIT_PROFILE_IMAGE) {
                removeFrag(AppConstants.EDIT_PROFILE_IMAGE_FRAG)
               try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.PROFILE_FRAG) as FragmentProfile?
                 fragment!!.setInfo(MyApplication.userLoginInfo)
               }catch (e:Exception){
                  try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.EDIT_PROFILE_FRAG) as FragmentEditProfile?
                      fragment!!.setImages()
                   }catch (e2:Exception){
                      try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.TEAM_PROFILE_FRAG) as FragmentProfile?
                          fragment!!.resetImageProfile()
                      }catch (e3:Exception){
                          try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.STARTUP_PROFILE_FRAG) as FragmentProfile?
                              fragment!!.resetImageProfile()
                          }catch (e3:Exception){}
                      }
                  }
               }
                fragmentAvailable=MyApplication.previews_frag
            }
            else if(fragmentAvailable==AppConstants.UPDATE_PASSWORD)
                removeFrag(AppConstants.UPDATE_PASSWORD_FRAG)
            else if(fragmentAvailable==AppConstants.EDIT_COVER_IMAGE) {
                removeFrag(AppConstants.EDIT_COVER_IMAGE_FRAG)
                try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.EDIT_PROFILE_FRAG) as FragmentEditProfile?
                   fragment!!.setImages()
                    fragmentAvailable=AppConstants.EDIT_PROFILE
                }catch (e2:Exception){
                    try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.TEAM_PROFILE_FRAG) as FragmentProfile?
                        fragment!!.resetImageProfile()
                        fragmentAvailable=AppConstants.TEAM_PROFILE
                    }catch (e:Exception){

                        try{ val fragment = fragmentManager.findFragmentByTag(AppConstants.STARTUP_PROFILE_FRAG) as FragmentProfile?
                            fragment!!.resetImageProfile()
                            fragmentAvailable=AppConstants.STARTUP_PROFILE
                        }catch (e:Exception){}
                    }

                }
            }else if( fragmentAvailable == AppConstants.TEAM_PROFILE){
                removeFrag(AppConstants.TEAM_PROFILE_FRAG)
            }
            else if( fragmentAvailable == AppConstants.STARTUP_PROFILE){
                removeFrag(AppConstants.STARTUP_PROFILE_FRAG)
            }

            else if( fragmentAvailable == AppConstants.OTHER_USER_PROFILE){
                removeFrag(AppConstants.OTHER_USER_PROFILE_FRAG)
            }

            else if( fragmentAvailable == AppConstants.DEFAULT_OTHER_USER_PROFILE){
                super.onBackPressed()
            }


            else if(fragmentAvailable!=AppConstants.NEWS){
                btNewsfeed.performClick()
            }
             else
                super.onBackPressed()
        }catch (e:Exception){
            super.onBackPressed()
        }

    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
      //  Toast.makeText(applicationContext,"asdsdssdf",Toast.LENGTH_LONG).show()
    }





    private fun getlocations(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getLocations(
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(call: Call<ArrayList<ResponseLocations>>, response: Response<ArrayList<ResponseLocations>>) {
                    try{
                      MyApplication.arrayAllLocations.clear()
                      MyApplication.arrayAllLocations.addAll(response.body()!!)
                    }catch (E:Exception){

                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseLocations>>, throwable: Throwable) {
                }
            })

    }


    private fun getLocationsSubLoc(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getLocationSubLocation(
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(call: Call<ArrayList<ResponseLocations>>, response: Response<ArrayList<ResponseLocations>>) {
                    try{
                        MyApplication.arrayLocationSubLocations.clear()
                        MyApplication.arrayLocationSubLocations.addAll(response.body()!!)
                    }catch (E:Exception){

                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseLocations>>, throwable: Throwable) {
                }
            })

    }


    private fun getGeneralLookup(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getGeneralLookup(
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(call: Call<ArrayList<ResponseLocations>>, response: Response<ArrayList<ResponseLocations>>) {
                    try{
                        MyApplication.arrayGeneralLookup.clear()
                        MyApplication.arrayGeneralLookup.addAll(response.body()!!)
                    }catch (E:Exception){

                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseLocations>>, throwable: Throwable) {
                }
            })

    }

    fun reloadActivity(){
        finish()
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        linearToolbar.background.alpha=255
    }

}
