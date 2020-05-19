package com.ids.inpoint.controller.Fragments



import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ids.inpoint.controller.Activities.ActivityChat
import com.ids.inpoint.controller.Adapters.AdapterChatUsers

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseNotification
import com.ids.inpoint.model.response.ResponseUser
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_signup.*

import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.lang.Exception


class FragmentUsersChat : Fragment() ,RVOnItemClickListener {



    var adapterUsers: AdapterChatUsers?=null
    var arrayUsers= java.util.ArrayList<ResponseUser>()

    override fun onItemClicked(view: View, position: Int) {
        MyApplication.pageUserId=adapterUsers!!.arrayUsers[position].UserId!!
        startActivity(Intent(activity, ActivityChat::class.java))
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_chat, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setlisteners()
    }

    private fun init(){

        btBack.visibility=View.VISIBLE
        setlisteners()
        linearWriteComment.visibility=View.GONE
     /*   try{AppHelper.setRoundImageResize(activity!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+MyApplication.userLoginInfo.image!!,MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}
*/
        if(MyApplication.arrayUsersHeader.size>0)
            getAllHeader()
        else
            setUsers()


    }

    private fun setlisteners(){
        btBack.setOnClickListener{activity!!.onBackPressed()}

    }


    override fun onResume() {
        super.onResume()

    }



    private fun getAllHeader(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllHeader()?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                    try{
                        MyApplication.arrayUsersHeader.clear()
                        MyApplication.arrayUsersHeader=response.body()!!
                        setUsers()
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setUsers(){
        adapterUsers= AdapterChatUsers(MyApplication.arrayUsersHeader,this,0)
        val glm = GridLayoutManager(activity, 1)
        rvChats!!.adapter=adapterUsers
        rvChats!!.layoutManager=glm
    }

}
