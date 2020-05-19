package com.ids.inpoint.controller.Fragments

import android.content.Intent.getIntent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ids.inpoint.R
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_settings.*
import android.content.Intent.getIntent
import android.content.Intent
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Activities.ActivityLogin
import com.ids.inpoint.utils.LocaleUtils
import java.util.*


class FragmentSettings : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.ids.inpoint.R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {

        btLogout.setOnClickListener{
            MyApplication.isLoggedIn=false
            startActivity(Intent(activity!!,ActivityLogin::class.java))
            activity!!.finish()
        }
        swShowNotifications.isChecked = MyApplication.showNotifications

        btEnglish.setOnClickListener {
            MyApplication.languageCode = AppConstants.LANG_ENGLISH
            //AppHelper.changeLanguage(activity!!, MyApplication.languageCode)
            LocaleUtils.setLocale(Locale("en"))
            LocaleUtils.updateConfig(activity!!.application, activity!!.baseContext.resources.configuration)
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                val token = instanceIdResult.token
                Log.wtf("token is " , token)
                AppHelper.addDevice(activity!!, token)
            }
            try{(activity!! as ActivityHome).reloadActivity()}catch (e:Exception){ }
        }

        btArabic.setOnClickListener {
            MyApplication.languageCode = AppConstants.LANG_ARABIC
            LocaleUtils.setLocale(Locale("ar"))
            LocaleUtils.updateConfig(activity!!.application, activity!!.baseContext.resources.configuration)
           // AppHelper.changeLanguage(activity!!, MyApplication.languageCode)
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                val token = instanceIdResult.token
                Log.wtf("token is " , token)
                AppHelper.addDevice(activity!!, token)
            }

            try{(activity!! as ActivityHome).reloadActivity()}catch (e:Exception){ }
        }

        swShowNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            MyApplication.showNotifications = isChecked

            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                val token = instanceIdResult.token
                Log.wtf("token is " , token)
                AppHelper.addDevice(activity!!, token)
            }

        }
    }


}
