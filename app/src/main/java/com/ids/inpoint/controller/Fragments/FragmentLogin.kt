package com.ids.inpoint.controller.Fragments
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Activities.ActivityLogin
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.loading.*

import retrofit2.*


class FragmentLogin : Fragment() , RVOnItemClickListener {

     var canClick=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_login, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    override fun onResume() {
        super.onResume()
        canClick=true
    }

    private fun init(){

/*        etUsrname.setText("i.haydar@ids.com.lb")
        etPassword.setText("0xUR_HBQM[iiex")*/
        btLogin.setOnClickListener{login()}
        btSignUp.setOnClickListener {
            if (canClick) {
                canClick=false
                try {(activity!! as ActivityLogin).goToSignUp()} catch (e: Exception) {}
            }
        }
        tvSignUp.setOnClickListener{
            if (canClick) {
                canClick=false
                try {(activity!! as ActivityLogin).goToSignUp()} catch (e: Exception) {}
        }}


        tvForgetPassword.setOnClickListener{
            if (canClick) {
                canClick=false
                try {(activity!! as ActivityLogin).resetPassword()} catch (e: Exception) {}
            }}

        btShowPassword.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {

                MotionEvent.ACTION_UP -> etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                MotionEvent.ACTION_DOWN -> etPassword.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            }
            true
        })


    }



    private fun login(){
        if(etUsrname.text.toString().isEmpty())
            Toast.makeText(activity,getString(R.string.enter_username),Toast.LENGTH_LONG).show()
        else if(etPassword.text.toString().isEmpty())
            Toast.makeText(activity,getString(R.string.enter_password),Toast.LENGTH_LONG).show()
        else if(!AppHelper.isValidEmail(etUsrname.text.toString()))
            Toast.makeText(activity,getString(R.string.enter_valid_email),Toast.LENGTH_LONG).show()
        else
            checkLogin()
       // goTest()
        //checkLogin()
    }

    private fun goTest(){
        MyApplication.userLoginInfo= ResponseUserInfos(
            6,
            "User",
            "",
            "1992-11-23T00:00:00",
            "432423423",
            true,
            "",
            34,
            1,
            6,1,0,MyApplication.languageCode,
            "53623a78-2ed8-4ebc-b776-6497130313a4.jpg",
            "10155cf8-2f67-4660-9178-12b6196be6b8.jpg",
            "44",1,26,"",30,"","",
            23,26,0,1,0,125,0,0,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiI2IiwiVXNlclR5cGUiOiIyNiIsIm5iZiI6MTU3NTUyODI4OSwiZXhwIjoxNTc2MTMzMDg5LCJpYXQiOjE1NzU1MjgyODksImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTAxOTEiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjUwMTkxIn0.VJji-o78bR-trKsVaQDv5ZSfLQqhQRH8btFgu63lV74"
  )
        startActivity(Intent(activity, ActivityHome::class.java))
        activity!!.finish()
    }

    private fun checkLogin(){
                 loading.visibility=View.VISIBLE
                   RetrofitClient.client?.create(RetrofitInterface::class.java)
                    ?.validateUser(
                        etUsrname.text.toString(),
                        etPassword.text.toString(),
                        MyApplication.languageCode
                    )?.enqueue(object : Callback<ResponseUserInfos> {
                        override fun onResponse(call: Call<ResponseUserInfos>, response: Response<ResponseUserInfos>) {
                            try{
                                loading.visibility=View.GONE
                              onValidateRetrieved(response)

                            }catch (E:Exception){
                                AppHelper.createDialog(activity!!,response.errorBody()!!.string().replace("\"", ""))
                             }
                          }
                          override fun onFailure(call: Call<ResponseUserInfos>, throwable: Throwable) {

                              AppHelper.createDialog(activity!!,throwable.message.toString())

                                  //checkLoginMessage()
                        }
                    })



    }




/*
    private fun getErrorMessage(response: Response<ResponseUserInfos>):String? {


        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(response.errorBody()!!.byteStream()))
            var line: String
            try {
                while ((line = reader!!.readLine()) != null) {
                    sb.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }


        val finallyError = sb.toString()
        return finallyError
    }*/

    private fun checkLoginMessage(){
        loading.visibility=View.VISIBLE
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.validateUserMessage(
                etUsrname.text.toString(),
                etPassword.text.toString(),
                MyApplication.languageCode
            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try{
                        loading.visibility=View.GONE
                        AppHelper.createDialog(activity!!,response.errorBody().toString()!!)
                    }catch (E:Exception){
                        AppHelper.createDialog(activity!!,"Please try again")
                    }
                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })



    }


    fun canClick(){
        canClick=true
    }

    private fun onValidateRetrieved( response: Response<ResponseUserInfos>) {
        MyApplication.userLoginInfo=response.body()!!
        if(MyApplication.userLoginInfo.token!=null && MyApplication.userLoginInfo.token!!.isNotEmpty()) {
            MyApplication.cashedUserName=etUsrname.text.toString()
            MyApplication.cashedPassword=etPassword.text.toString()
            MyApplication.isLoggedIn=true
            try{Log.wtf("access_token",MyApplication.userLoginInfo.token)}catch (e:java.lang.Exception){}
            try{Log.wtf("userid",MyApplication.userLoginInfo.id.toString())}catch (e:java.lang.Exception){}
            getMobileConfiguration()
            getFollowers()
            getAllHeader()
            getUserTeams()
          //  getAllCourses()
           // getAllPublications()

            startActivity(Intent(activity, ActivityHome::class.java))
            activity!!.finish()

        }else
            AppHelper.createDialog(activity!!,"Please try again")
    }

    override fun onItemClicked(view: View, position: Int) {


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
            )?.enqueue(object : Callback<java.util.ArrayList<ResponseConfiguration>> {
                override fun onResponse(call: Call<java.util.ArrayList<ResponseConfiguration>>, response: Response<java.util.ArrayList<ResponseConfiguration>>) {
                    try{
                        MyApplication.arrayConfiguration.clear()
                        MyApplication.arrayConfiguration=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<java.util.ArrayList<ResponseConfiguration>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getAllCourses(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllCources()?.enqueue(object : Callback<ArrayList<ResponseCourse>> {
                override fun onResponse(call: Call<ArrayList<ResponseCourse>>, response: Response<ArrayList<ResponseCourse>>) {
                    try{
                        MyApplication.arrayCources.clear()
                        MyApplication.arrayCources=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseCourse>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getAllPublications(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPublication()?.enqueue(object : Callback<ArrayList<ResponsePublication>> {
                override fun onResponse(call: Call<ArrayList<ResponsePublication>>, response: Response<ArrayList<ResponsePublication>>) {
                    try{
                        MyApplication.arrayPublication.clear()
                        MyApplication.arrayPublication=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponsePublication>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getUserTeams(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserTeams(
            )?.enqueue(object : Callback<java.util.ArrayList<ResponseUserTeam>> {
                override fun onResponse(call: Call<java.util.ArrayList<ResponseUserTeam>>, response: Response<java.util.ArrayList<ResponseUserTeam>>) {
                    try{
                        MyApplication.arrayUserTeams.clear()
                        MyApplication.arrayUserTeams=response.body()!!
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<java.util.ArrayList<ResponseUserTeam>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }

}


