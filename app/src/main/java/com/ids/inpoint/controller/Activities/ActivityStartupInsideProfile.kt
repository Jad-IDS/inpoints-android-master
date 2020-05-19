package com.ids.inpoint.controller.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View

import kotlinx.android.synthetic.main.footer.*
import android.view.animation.AlphaAnimation
import android.content.Intent
import android.content.res.Configuration
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.ids.inpoint.R
import com.ids.inpoint.controller.Fragments.*
import com.ids.inpoint.utils.AppHelper.Companion.fragmentAvailable
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseLocations
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ActivityStartupInsideProfile : FragmentActivity() {

    init {
        LocaleUtils.updateConfig(this)
    }

    lateinit var fragmentManager: FragmentManager

    private var previewFragment=AppConstants.STARTUP_PROFILE_INFO
    private var defaulFragment=AppConstants.STARTUP_PROFILE_INFO
    private var defaulFragmentTag=AppConstants.STARTUP_PROFILE_INFO_FRAG
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ids.inpoint.R.layout.activity_startup_profile)
        init()
       // getlocations()

    }

    private fun init(){
        AppHelper.setLocal(this)
        fragmentManager = supportFragmentManager
        defaulFragment=intent.getIntExtra(AppConstants.DEFAULT_FRAG,AppConstants.STARTUP_PROFILE_INFO)
        defaulFragmentTag=intent.getStringExtra(AppConstants.DEFAULT_FRAG_TAG)
        defaultFragmentById()
        btBack.setOnClickListener{
            super.onBackPressed()
        }


    }

    private fun defaultFragmentById(){


        fragmentAvailable=defaulFragment
        Log.wtf("default_id",defaulFragment.toString())
        Log.wtf("default_frag",defaulFragmentTag.toString())
        var frag :Fragment
        frag=FragmentStartupProfileInfo()
        when (defaulFragment) {
            AppConstants.STARTUP_PROFILE_INFO -> frag= FragmentStartupProfileInfo()
            AppConstants.STARTUP_PROFILE_PRODUCT -> frag= FragmentStartupProfileProduct()
            AppConstants.STARTUP_PROFILE_BRANCHES -> frag= FragmentStartupProfleBranches()
            AppConstants.STARTUP_PROFILE_PARTNERS -> frag= FragmentStartupPartners()
            AppConstants.STARTUP_PROFILE_REVIEWS -> frag= FragmentStartupReviews()
        }

        fragmentManager.beginTransaction()
            .add(R.id.container, frag, defaulFragmentTag)
            .commit()

    }





    fun removeFrag(tag:String){
        fragmentAvailable=previewFragment
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null)
            supportFragmentManager.beginTransaction().remove(fragment).commit()
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





}
