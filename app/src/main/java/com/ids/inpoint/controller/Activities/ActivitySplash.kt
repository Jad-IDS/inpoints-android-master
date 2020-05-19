package com.ids.inpoint.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.ids.inpoint.R
import com.ids.inpoint.controller.Base.ActivityBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivitySplash : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Crashlytics.getInstance().crash() // Force a crash
/*        MyApplication.languageCode = AppConstants.LANG_ARABIC
        LocaleUtils.setLocale(Locale("ar"))
        LocaleUtils.updateConfig(application, baseContext.resources.configuration)
        AppHelper.setLocal(this)*/



        Log.wtf("cashing_isloggedin1","---"+MyApplication.isLoggedIn.toString())
        Log.wtf("cashing_username","---"+MyApplication.cashedUserName)
        Log.wtf("cashing_password","---"+MyApplication.cashedPassword)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            Log.wtf("token","token is " + token)

        }

        Handler().postDelayed({
           if(!MyApplication.isLoggedIn) {
              goToLogin()
           }else {
               checkLogin()
           }

        }, 1000)

    }



    private fun checkLogin(){
        Log.wtf("check_login","check")
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.validateUser(
                MyApplication.cashedUserName,
                MyApplication.cashedPassword,
                MyApplication.languageCode
            )?.enqueue(object : Callback<ResponseUserInfos> {
                override fun onResponse(call: Call<ResponseUserInfos>, response: Response<ResponseUserInfos>) {
                    try{

                        onValidateRetrieved(response)

                    }catch (E:Exception){
                        Log.wtf("check_login","exception1"+E.toString())
                        MyApplication.cashedUserName=""
                        MyApplication.cashedPassword=""
                        MyApplication.isLoggedIn=false
                        goToLogin()
                    }
                }
                override fun onFailure(call: Call<ResponseUserInfos>, throwable: Throwable) {
                    Log.wtf("check_login","exception2"+throwable.message.toString())
                    MyApplication.cashedUserName=""
                    MyApplication.cashedPassword=""
                    MyApplication.isLoggedIn=false
                    goToLogin()
                    //checkLoginMessage()
                }
            })



    }

    private fun onValidateRetrieved( response: Response<ResponseUserInfos>) {
        MyApplication.userLoginInfo=response.body()!!
        if(MyApplication.userLoginInfo.token!=null && MyApplication.userLoginInfo.token!!.isNotEmpty()) {
            try{
                Log.wtf("access_token",MyApplication.userLoginInfo.token)}catch (e:java.lang.Exception){}
            try{
                Log.wtf("userid",MyApplication.userLoginInfo.id.toString())}catch (e:java.lang.Exception){}
            getMobileConfiguration()
            getFollowers()
            getAllHeader()
            getUserTeams()

            //  getAllCourses()
            // getAllPublications()

            startActivity(Intent(this, ActivityHome::class.java))
            finish()

        }else {
            MyApplication.cashedUserName = ""
            MyApplication.cashedPassword = ""
            MyApplication.isLoggedIn = false
            goToLogin()
        }
    }



    private fun getFollowers(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getFollowers(MyApplication.userLoginInfo.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
                override fun onResponse(call: Call<ArrayList<ResponseFollowers>>, response: Response<ArrayList<ResponseFollowers>>) {
                    try{
                        MyApplication.arrayFollowers.clear()
                        MyApplication.arrayFollowers=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseFollowers>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getAllHeader(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllHeader()?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                    try{
                        MyApplication.arrayUsersHeader.clear()
                        MyApplication.arrayUsersHeader=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }



    private fun getMobileConfiguration(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getMobileConfigurations(
            )?.enqueue(object : Callback<ArrayList<ResponseConfiguration>> {
                override fun onResponse(call: Call<ArrayList<ResponseConfiguration>>, response: Response<ArrayList<ResponseConfiguration>>) {
                    try{
                        MyApplication.arrayConfiguration.clear()
                        MyApplication.arrayConfiguration=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseConfiguration>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getUserTeams(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserTeams(
            )?.enqueue(object : Callback<ArrayList<ResponseUserTeam>> {
                override fun onResponse(call: Call<ArrayList<ResponseUserTeam>>, response: Response<ArrayList<ResponseUserTeam>>) {
                    try{
                        MyApplication.arrayUserTeams.clear()
                        MyApplication.arrayUserTeams=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseUserTeam>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun goToLogin(){
        startActivity(Intent(this, ActivityLogin::class.java))
        finish()
    }

}
