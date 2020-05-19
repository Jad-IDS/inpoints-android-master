package com.ids.inpoint.controller.Activities

import android.annotation.TargetApi
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View

import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.ResponseTeamProject
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_action_plan.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.*
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request

import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.ids.inpoint.R


import java.util.HashMap


class ActivityActionPlan : AppCompactBase(), RVOnItemClickListener {

    private lateinit var adapterProjects: AdapterSpinner
    private var arrayProjects: ArrayList<ItemSpinner> = ArrayList()

    private var mUploadMessage: ValueCallback<Uri>? = null
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    val REQUEST_SELECT_FILE = 100
    private val FILECHOOSER_RESULTCODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_plan)

        init()
        var url=MyApplication.arrayConfiguration[5].value.toString()+
                "?Id="+MyApplication.selectedStartupTeam.id+"&ProjectId=0&Authentication="+MyApplication.userLoginInfo.token
        Log.wtf("action_url",url)
        loadUrl(url)
    }

    private fun init() {
        supportActionBar!!.hide()
        btBack.setOnClickListener { super.onBackPressed() }

        getTeamProjects()

        llProject.setOnClickListener {
            if(llProjects.visibility == View.VISIBLE){
                llProjects.visibility=View.GONE
            }else{
                llProjects.visibility=View.VISIBLE
            }
        }

/*        var mimeType = "text/html";
        var encoding = "UTF-8";
        var html = "<br /><br />Read the handouts please for tomorrow.<br /><br /><!--homework help homework" +
        "help help with homework homework assignments elementary school high school middle school" +
                "// --><font color='#60c000' size='4'><strong>Please!</strong></font>" +
                "<img src='http://www.homeworknow.com/hwnow/upload/images/tn_star300.gif'  />";


        wvHtml.loadDataWithBaseURL("", html, mimeType, encoding, "")*/
    }

    private fun getTeamProjects(){
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamProjects(AppConstants.LANG_ENGLISH,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseTeamProject>> {
                override fun onResponse(call: Call<ArrayList<ResponseTeamProject>>, response: Response<ArrayList<ResponseTeamProject>>) {
                    try{
                        onProjectsRetrieved(response.body())

                    }catch (E: Exception){
                        loading.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseTeamProject>>, throwable: Throwable) {
                    loading.visibility = View.GONE
                }
            })
    }


    private fun onProjectsRetrieved(body: ArrayList<ResponseTeamProject>?) {
        arrayProjects.clear()
        for (i in body!!.indices)
            arrayProjects.add(ItemSpinner(body[i].id, body[i].name))

        adapterProjects = AdapterSpinner(arrayProjects, this, AppConstants.SPINNER_EVENT_SORT)
        val glm = GridLayoutManager(this, 1)
        rvProjects.adapter = adapterProjects
        rvProjects.layoutManager = glm
        loading.visibility = View.GONE
    }

    override fun onItemClicked(view: View, position: Int) {
        tvProject.text = arrayProjects[position].name
        llProjects.visibility = View.GONE
     /*   var url=MyApplication.arrayConfiguration[5].value.toString()+
                "?Id="+MyApplication.selectedStartupTeam.id+"&ProjectId="+adapterProjects.itemSpinner[position].id!!
*/
        var url=MyApplication.arrayConfiguration[5].value.toString()+
                "?Id="+MyApplication.selectedStartupTeam.id+"&ProjectId="+adapterProjects.itemSpinner[position].id!!+"&Authentication="+MyApplication.userLoginInfo.token
        Log.wtf("action_url_click",url)
        loadUrl(url)

    }



    private fun loadUrl(url:String){
        val mWebSettings = wvHtml.settings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.setSupportZoom(false)
        mWebSettings.allowFileAccess = true
        mWebSettings.allowFileAccess = true
        mWebSettings.allowContentAccess = true
        wvHtml.settings.allowFileAccess = true;
        wvHtml.setOnLongClickListener { true }
        wvHtml.isHapticFeedbackEnabled = false
        Log.wtf("webview_url",url)
        //wvHtml.webViewClient=wvc


        wvHtml.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                Log.wtf("load", " started")
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                Log.wtf("load", " finished")
                Log.wtf("load", " finished")
                try{
                   // progressBar.hideView()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }


        wvHtml.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Log.wtf("doc_url",url)





            if(url.toLowerCase().contains(".doc") ||
                url.toLowerCase().contains(".xls") ||
                url.toLowerCase().contains(".ppt")||
                url.toLowerCase().contains(".xps")) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse("http://docs.google.com/viewer?url=$url"), "text/html")
                startActivity(intent)
            }else{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }


        }

      //  val headerMap = HashMap<String, String>()
   //     headerMap["Authorization"] = MyApplication.userLoginInfo.token


        wvHtml.loadUrl(url)
        wvHtml.webChromeClient = object : WebChromeClient() {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE)
            }


            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(
                wvPage: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: WebChromeClient.FileChooserParams
            ): Boolean {
                if (uploadMessage != null) {
                    uploadMessage!!.onReceiveValue(null)
                    uploadMessage = null
                }

                uploadMessage = filePathCallback

                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(applicationContext, "Cannot Open File Chooser", Toast.LENGTH_LONG).show()
                    return false
                }

                return true
            }

            //For Android 4.1 only
            protected fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
                mUploadMessage = uploadMsg
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE)
            }

            protected fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
            }
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return
                uploadMessage!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent))
                uploadMessage = null
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        } else
            Toast.makeText(applicationContext, "Failed to Upload Image", Toast.LENGTH_LONG).show()
    }
/*
    private fun getTeamProjects(projectId:Int){
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTaskForPlan(
                MyApplication.selectedStartupTeam.id!!,projectId
            )?.enqueue(object : Callback<ArrayList<ResponseTeamProject>> {
                override fun onResponse(call: Call<ArrayList<ResponseTeamProject>>, response: Response<ArrayList<ResponseTeamProject>>) {
                    try{
                        onProjectsRetrieved(response.body())

                    }catch (E: Exception){
                        loading.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseTeamProject>>, throwable: Throwable) {
                    loading.visibility = View.GONE
                }
            })
    }*/


    var wvc: WebViewClient = object : WebViewClient() {

        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
            try {


                val okHttpClient = OkHttpClient()
                val request =
                    Request.Builder().url(url).addHeader("Authorization",  MyApplication.userLoginInfo.token!!)
                        .build()

                val response = okHttpClient.newCall(request).execute()

                return WebResourceResponse(
                    response.header(
                        "text/html",
                        response.body()!!.contentType()!!.type()
                    ), // You can set something other as default content-type
                    response.header(
                        "content-encoding",
                        "utf-8"
                    ), // Again, you can set another encoding as default
                    response.body()!!.byteStream()
                )


            } catch (e: Exception) {
                //return null to tell WebView we failed to fetch it WebView should try again.
                return null
            }

        }
    }
}
