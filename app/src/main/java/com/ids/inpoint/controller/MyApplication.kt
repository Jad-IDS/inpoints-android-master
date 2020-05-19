package com.ids.inpoint.controller

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import java.util.*

class MyApplication : Application() {
    companion object {
        lateinit var sharedPreferences : SharedPreferences
        lateinit var sharedPreferencesEditor : SharedPreferences.Editor

        internal lateinit var instance: MyApplication
        var isLoggedIn : Boolean
            get() = sharedPreferences.getBoolean(AppConstants.IS_LOGGED_IN, false)
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.IS_LOGGED_IN, value).apply() }

        var cashedUserName : String
            get() = sharedPreferences.getString(AppConstants.CASHED_USERNAME, "")
            set(value) { sharedPreferencesEditor.putString(AppConstants.CASHED_USERNAME, value).apply() }

        var cashedPassword : String
            get() = sharedPreferences.getString(AppConstants.CASHED_PASSWORD, "")
            set(value) { sharedPreferencesEditor.putString(AppConstants.CASHED_PASSWORD, value).apply() }

        var languageCode : String
            get() = sharedPreferences.getString(AppConstants.SELECTED_LANGUAGE, AppConstants.LANG_ARABIC)
            set(value) { sharedPreferencesEditor.putString(AppConstants.SELECTED_LANGUAGE, value).apply() }

        var showNotifications : Boolean
            get() = sharedPreferences.getBoolean("key_show_notifications", true)
            set(value){
                sharedPreferencesEditor.putBoolean(
                    "key_show_notifications",
                    value
                ).apply()
            }
        var arrayComments = ArrayList<ResponseComments>()
        var arrayTypes = ArrayList<ResponsePostType>()
        var videoId = ""
        internal lateinit var mShared: SharedPreferences
        internal lateinit var editor: SharedPreferences.Editor
        lateinit var userLoginInfo: ResponseUserInfos
        var arrayFiles = java.util.ArrayList<ResponseTeamFile>()
        lateinit var selectedPost: ResponsePost
        lateinit var selectedMedia: PostMedia
        lateinit var selectedOnlineCourse: ResponseOnlineCourses
        lateinit var selectedOnlinePublication: ResponseOnlinePublication

        lateinit var selectedLesson: ResponseLesson
        lateinit var selectedStartupTeam: TeamStartup
        lateinit var pageUserInfo: ResponseUserInfos
        var arrayAllLocations = ArrayList<ResponseLocations>()
        var arrayUsers = ArrayList<ResponseUser>()
        var arrayLocationSubLocations = ArrayList<ResponseLocations>()
        var arrayGeneralLookup = ArrayList<ResponseLocations>()

        var arrayReports = ArrayList<ResponseLocations>()

        var arrayFollowers = ArrayList<ResponseFollowers>()
        var arrayCategories = ArrayList<ResponseCategory>()
        var arrayConfiguration = ArrayList<ResponseConfiguration>()

        var arrayUserTeams = ArrayList<ResponseUserTeam>()

        var selectedImage = ""
        var previews_frag = 0
        var UNIQUE_REQUEST_CODE = 0
        var searchkeyword = ""
        var isPostEdit = false
        var isTeamChat = false
        var isEventEdit = false
        var pageUserId = 0
        var arrayUsersHeader = ArrayList<ResponseUser>()

        var arrayCources = ArrayList<ResponseCourse>()
        var arrayPublication = ArrayList<ResponsePublication>()


        var imageType = AppConstants.BOTTOM_SHEET_IMAGE_TYPE_LINK
        var isProfileImageLocal = false
        var isCoverImageLocal = false
        var systemLanguage: Int = 0
        var language:Int = 0
        var selectedUserId = 0

        //<editor-fold desc="Shared Prefs">

        //</editor-fold>
    }


    override fun onCreate() {
        super.onCreate()

        //<editor-fold desc="Shared Prefs">
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferencesEditor = sharedPreferences.edit()
        instance=this
        //</editor-fold>
/*        AppHelper.changeLanguage(this, languageCode)
        Log.wtf("language_code", languageCode)
        BaseActivityLang.dLocale = Locale(languageCode) //set any locale you want here*/
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

    override fun attachBaseContext(newBase: Context) {
        var newBase = newBase
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val config = newBase.resources.configuration
            config.setLocale(Locale.getDefault())
            newBase = newBase.createConfigurationContext(config)
        }
        super.attachBaseContext(newBase)
    }



}
