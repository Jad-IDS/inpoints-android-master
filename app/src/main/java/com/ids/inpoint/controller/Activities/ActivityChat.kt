package com.ids.inpoint.controller.Activities


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.ids.inpoint.controller.Adapters.AdapterChat
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseChat
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.loading_trans.loading

import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList





class ActivityChat : AppCompactBase(),RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {
        if (MyApplication.userLoginInfo.id!! == adapterChat!!.arrayChats[position].senderId!!) {
            startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_MY_PROFILE_FRAG))
        } else {
            MyApplication.selectedPost= ResponsePost()
            MyApplication.selectedPost.userId=adapterChat!!.arrayChats[position].senderId
            MyApplication.selectedPost.image=adapterChat!!.arrayChats[position].senderImage
            MyApplication.selectedPost.userName=adapterChat!!.arrayChats[position].senderName
            startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
        }
    }

    private lateinit var fragmentManager: FragmentManager
    var fragmentAvailable= AppConstants.LOGIN
    var canClickLogin=true
    private var maxId=0
    private var skip=0
    private var take=10
    private var isLoading = false
    private var isTeam=false
    private var changeCount=0

    var adapterChat: AdapterChat?=null
    var arrayChats= java.util.ArrayList<ResponseChat>()
    var arrayReverseChat= java.util.ArrayList<ResponseChat>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ids.inpoint.R.layout.fragment_chat)
        init()

    }


    private fun init(){
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
        btBack.visibility= View.VISIBLE
        setlisteners()
        isTeam=MyApplication.isTeamChat

/*        try{AppHelper.setRoundImageResize(applicationContext!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+ MyApplication.userLoginInfo.image!!,
            MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}*/

        getChats(false,false)

        ivSend.setOnClickListener{
            if(etMessage.text.toString().isNotEmpty()) {
               sendMessage()
            }
        }


    }



    private fun startTimerChat() {
        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                getChats(false,false)
            }
        }
        timer.start()

    }


    private fun setlisteners(){
        btBack.setOnClickListener{this.onBackPressed()}

    }




    private fun getChats(isScroll:Boolean,isSent:Boolean){
        if(isSent)
            loading.visibility=View.VISIBLE
        changeCount=0
        if(!isTeam){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
         ?.getChats(MyApplication.pageUserId,skip,take,maxId)
            ?.enqueue(object : Callback<ArrayList<ResponseChat>> {
                override fun onResponse(call: Call<ArrayList<ResponseChat>>, response: Response<ArrayList<ResponseChat>>) {
                    try{
                        loading.visibility=View.GONE
                       onchatRetreived(response.body(),isScroll,isSent)
                    }catch (E: Exception){
                        loading.visibility=View.GONE
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseChat>>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })
         }else{
            RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
                ?.getTeamChat(MyApplication.pageUserId,skip,take,maxId)
                ?.enqueue(object : Callback<ArrayList<ResponseChat>> {
                    override fun onResponse(call: Call<ArrayList<ResponseChat>>, response: Response<ArrayList<ResponseChat>>) {
                        try{
                            loading.visibility=View.GONE
                            onchatRetreived(response.body(),isScroll,isSent)
                        }catch (E: Exception){ }
                    }
                    override fun onFailure(call: Call<ArrayList<ResponseChat>>, throwable: Throwable) {
                        loading.visibility=View.GONE
                    }
                })
        }
    }

    private fun onchatRetreived(response: ArrayList<ResponseChat>?,isScroll:Boolean,isSent: Boolean) {


        if(adapterChat==null) {
            arrayChats.clear()
            arrayChats.addAll(response!!)
            arrayReverseChat.clear()
            arrayReverseChat.addAll(response)
            arrayReverseChat.reverse()
            adapterChat = AdapterChat(arrayReverseChat, this, 0)
            val glm = GridLayoutManager(this, 1)
            rvChats!!.adapter = adapterChat
            rvChats!!.layoutManager = glm
            if(response.size==0)
                isLoading=true
            rvChats.scrollToPosition(arrayReverseChat.size-1)
            setChatPaging()
        }else{
            val position = 0

            if(isScroll)
                arrayChats.addAll(response!!)
            else
                checkAndAdd(response)

            Log.wtf("change_count",changeCount.toString()+"aa")

            arrayReverseChat.clear()


                arrayReverseChat.addAll(arrayChats)
                arrayReverseChat.reverse()

          // adapterChat!!.notifyItemInserted(0)


            resetArrays()
            adapterChat!!.notifyDataSetChanged()

            if(isScroll) {
                (rvChats.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(response!!.size - 1, 0)
            }
           // try{rvChats.scrollToPosition(response.size-1)}catch (e:Exception){}
            if(response!!.size==0 && isScroll)
                isLoading=true

            if(isSent)
                rvChats.scrollToPosition(arrayReverseChat.size - 1)
        }

        startTimerChat()

    }

    private fun resetArrays(){
        var array= arrayListOf<ResponseChat>()
        for(i in arrayChats.indices){
            if(arrayChats[i].id!=0)
                array.add(arrayChats[i])
        }

        arrayChats.clear()
        arrayChats.addAll(array)



        var array2= arrayListOf<ResponseChat>()
        for(i in arrayReverseChat.indices){
            if(arrayReverseChat[i].id!=0)
                array2.add(arrayReverseChat[i])
        }

        arrayReverseChat.clear()
        arrayReverseChat.addAll(array2)

    }

    private fun checkAndAdd(response: ArrayList<ResponseChat>?) {
        //arrayChats.addAll(response!!)
        for(i in response!!.indices){
            if(!this.containId(response[i].id!!)!!) {
                arrayChats.add(0,response[i])
                changeCount++
            }
        }

    }


    private fun containId(id:Int):Boolean?{
        var contain=false
        for (i in arrayReverseChat.indices){
            if(arrayReverseChat[i].id==id){
                contain=true
                break
            }
        }
        return contain
    }


    private fun sendMessage(){
        var dateNow = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())
/*        arrayChats.add(0,ResponseChat(0,MyApplication.userLoginInfo.id,MyApplication.userLoginInfo.userName,MyApplication.userLoginInfo.image,MyApplication.pageUserId,dateNow,etMessage.text.toString()))
        arrayReverseChat.add(ResponseChat(0,MyApplication.userLoginInfo.id,MyApplication.userLoginInfo.userName,MyApplication.userLoginInfo.image,MyApplication.pageUserId,dateNow,etMessage.text.toString()))
        try {
            adapterChat!!.notifyDataSetChanged()
            rvChats.scrollToPosition(arrayReverseChat.size - 1)
        }catch (e:Exception){}*/
        var message=etMessage.text.toString()
        etMessage.setText("")

        if(!isTeam){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.sendChat(ResponseChat(0,MyApplication.userLoginInfo.id,MyApplication.userLoginInfo.userName,MyApplication.userLoginInfo.image,MyApplication.pageUserId,dateNow,message))?.enqueue(object : Callback<ResponseChat> {
                override fun onResponse(call: Call<ResponseChat>, response: Response<ResponseChat>) {
                    try{
                        onChatSent(response.body())
                        skip=0
                        getChats(false,true)
                    }catch (E: Exception){ }
                }
                override fun onFailure(call: Call<ResponseChat>, throwable: Throwable) {
                }
            })}else{
            RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
                ?.sendTeamChat(ResponseChat(0,MyApplication.userLoginInfo.id,MyApplication.userLoginInfo.userName,MyApplication.userLoginInfo.image,MyApplication.pageUserId,dateNow,message))?.enqueue(object : Callback<ResponseChat> {
                    override fun onResponse(call: Call<ResponseChat>, response: Response<ResponseChat>) {
                        try{
                            onChatSent(response.body())
                            skip=0
                            getChats(false,true)
                        }catch (E: Exception){ }
                    }
                    override fun onFailure(call: Call<ResponseChat>, throwable: Throwable) {
                    }
                })
        }
    }

    private fun onChatSent(response: ResponseChat?) {

    }

    private fun setChatPaging(){


               rvChats.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                   override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                       super.onScrolled(recyclerView, dx, dy)
                          if(!recyclerView.canScrollVertically(dy) && dy<0 && !isLoading){
                             // Log.wtf("can_scroll_top","false")
                              // isLoading = true
                               skip+=take
                               getChats(true,false)
                     }
                   }
               })

    }


    private fun IsRecyclerViewAtTop(): Boolean {
        return if (rvChats.childCount == 0) true else rvChats.getChildAt(0).top == 0
    }
}
