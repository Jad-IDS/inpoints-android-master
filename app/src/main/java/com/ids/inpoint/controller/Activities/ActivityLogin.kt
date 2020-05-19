package com.ids.inpoint.controller.Activities


import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.ids.inpoint.R
import com.ids.inpoint.controller.Base.ActivityBase
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.Fragments.FragementSignup
import com.ids.inpoint.controller.Fragments.FragmentLogin
import com.ids.inpoint.controller.Fragments.FragmentNewsFeed
import com.ids.inpoint.controller.Fragments.FragmentResetPassword
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.LocaleUtils


class ActivityLogin : AppCompactBase() {
    private lateinit var fragmentManager: FragmentManager
    var fragmentAvailable= AppConstants.LOGIN
    var canClickLogin=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        defaultFragment()
    }


    private fun init(){
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
      //  LocaleUtils.updateConfig(this@ActivityLogin)
    }

    fun defaultFragment(){

        fragmentAvailable=AppConstants.LOGIN
        val login = FragmentLogin()
        fragmentManager.beginTransaction()
            .replace(com.ids.inpoint.R.id.container, login, AppConstants.LOGIN_FRAG)
            .commit()
    }

    fun goToSignUp(){
       AppHelper.AddFragment(fragmentManager,AppConstants.SIGNUP,FragementSignup(),AppConstants.SIGNUP_FRAG)
    }
    fun resetPassword(){
        AppHelper.AddFragment(fragmentManager,AppConstants.RESET_PASSWORD,FragmentResetPassword(),AppConstants.RESET_FRAG)
    }
    override fun onBackPressed() {
        if(AppHelper.fragmentAvailable==AppConstants.SIGNUP){
            removeFrag(AppConstants.SIGNUP_FRAG)
            AppHelper.fragmentAvailable=AppConstants.LOGIN
        } else
            if(AppHelper.fragmentAvailable==AppConstants.RESET_PASSWORD){
            removeFrag(AppConstants.RESET_FRAG)
            AppHelper.fragmentAvailable=AppConstants.LOGIN
        }

        else
            super.onBackPressed()
    }


    fun removeFrag(tag:String){

        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null)
            supportFragmentManager.beginTransaction().remove(fragment).commit()

        try{
            val fragment = fragmentManager.findFragmentByTag(AppConstants.LOGIN_FRAG) as FragmentLogin?
            fragment!!.canClick()
        }catch (E:Exception){

        }
    }




}
