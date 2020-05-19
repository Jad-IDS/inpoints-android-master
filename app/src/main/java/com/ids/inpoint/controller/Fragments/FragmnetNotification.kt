package com.ids.inpoint.controller.Fragments




import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Activities.ActivityInsideComment
import com.ids.inpoint.controller.Adapters.AdapterNotification

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.model.response.ResponseNotification
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface

import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.lang.Exception


class FragmnetNotification : Fragment() ,RVOnItemClickListener {

    var adapterNotifications: AdapterNotification?=null
    var arrayNotification= java.util.ArrayList<ResponseNotification>()
    override fun onItemClicked(view: View, position: Int) {
        MyApplication.selectedPost= ResponsePost()
        setSeenNotification(adapterNotifications!!.arrayNotifications[position].id!!)

        if(adapterNotifications!!.arrayNotifications[position].type==AppConstants.SEARCH_TYPE_POST || adapterNotifications!!.arrayNotifications[position].type==AppConstants.SEARCH_TYPE_EVENT  ){
            startActivity(Intent(activity, ActivityInsideComment::class.java)
                .putExtra(AppConstants.POST_ID,adapterNotifications!!.arrayNotifications[position].url!!.substring(adapterNotifications!!.arrayNotifications[position].url!!.lastIndexOf("=")+1)))
        }else if(adapterNotifications!!.arrayNotifications[position].type==AppConstants.SEARCH_TYPE_USERS ){


            if (MyApplication.userLoginInfo.id!! == (adapterNotifications!!.arrayNotifications[position].url!!.substring(adapterNotifications!!.arrayNotifications[position].url!!.lastIndexOf("=")+1)).toInt()) {
                startActivity(Intent(activity, ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_MY_PROFILE_FRAG))
            } else {
                MyApplication.selectedPost= ResponsePost()
                MyApplication.selectedPost.userId=(adapterNotifications!!.arrayNotifications[position].url!!.substring(adapterNotifications!!.arrayNotifications[position].url!!.lastIndexOf("=")+1)).toInt()
                MyApplication.selectedPost.image=null
                MyApplication.selectedPost.userName=null
                startActivity(Intent(activity, ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
            }



        }else if(adapterNotifications!!.arrayNotifications[position].type==AppConstants.SEARCH_TYPE_STARTUP ){

            MyApplication.selectedStartupTeam= TeamStartup()
            MyApplication.selectedStartupTeam.id=(adapterNotifications!!.arrayNotifications[position].url!!.substring(adapterNotifications!!.arrayNotifications[position].url!!.lastIndexOf("=")+1)).toInt()
            MyApplication.selectedStartupTeam.image=null
            MyApplication.selectedStartupTeam.userName=null

            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.STARTUP_PROFILE,
                FragmentProfile(),
                AppConstants.STARTUP_PROFILE_FRAG
            )
        }else if(adapterNotifications!!.arrayNotifications[position].type==AppConstants.SEARCH_TYPE_TEAMS ){

            MyApplication.selectedStartupTeam= TeamStartup()
            MyApplication.selectedStartupTeam.id=(adapterNotifications!!.arrayNotifications[position].url!!.substring(adapterNotifications!!.arrayNotifications[position].url!!.lastIndexOf("=")+1)).toInt()
            MyApplication.selectedStartupTeam.image=null
            MyApplication.selectedStartupTeam.userName=null

            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.TEAM_PROFILE,
                FragmentProfile(),
                AppConstants.TEAM_PROFILE_FRAG
            )
        }





  /*      startActivity(Intent(activity, ActivityInsideComment::class.java)
            .putExtra(AppConstants.POST_ID,adapterNotifications!!.arrayNotifications[position].url!!.substring(adapterNotifications!!.arrayNotifications[position].url!!.lastIndexOf("=")+1)))
  */

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_notification, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setlisteners()
    }

    private fun init(){
      try{linearToolbar.background.alpha = 255}catch (e:Exception){}
      getNotifications()
      ivToolbarBack.visibility=View.VISIBLE
       setlisteners()

        try{AppHelper.setRoundImageResize(activity!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+MyApplication.userLoginInfo.image!!,MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}

    }

    private fun setlisteners(){
        ivToolbarBack.setOnClickListener{activity!!.onBackPressed()}
        srlNotification.setOnRefreshListener {
            adapterNotifications=null
            getNotifications()
            srlNotification
        }
    }








    private fun getNotifications(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getNotifications()?.enqueue(object : Callback<ArrayList<ResponseNotification>> {
                override fun onResponse(call: Call<ArrayList<ResponseNotification>>, response: Response<ArrayList<ResponseNotification>>) {
                    try{
                        onNotificationRetreived(response.body())
                    }catch (E:Exception){ }
                }
                override fun onFailure(call: Call<ArrayList<ResponseNotification>>, throwable: Throwable) {
                }
            })
    }

    private fun onNotificationRetreived(response: ArrayList<ResponseNotification>?) {
        arrayNotification.clear()
        arrayNotification.addAll(response!!)
        try{srlNotification.isRefreshing = false }catch (e:Exception){}
        adapterNotifications= AdapterNotification(arrayNotification,this,0)
        val glm = GridLayoutManager(activity, 1)
        rvNotification!!.adapter=adapterNotifications
        rvNotification!!.layoutManager=glm

       if(arrayNotification.size==0)
           tvUnread.visibility=View.GONE
        else {
           var count = 0
           for (i in arrayNotification.indices) {
               if (!arrayNotification[i].seen!!)
                   count++
           }
           if(count!=0){
               tvUnread.visibility = View.VISIBLE
               tvUnread.text=count.toString()+" unread"
           }else
               tvUnread.visibility=View.GONE
       }

    }

    private fun setSeenNotification(id:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.setSeenNotification(id)?.enqueue(object : Callback<ResponseNotification> {
                override fun onResponse(call: Call<ResponseNotification>, response: Response<ResponseNotification>) {
                    try{
                        //Toast.makeText(activity,response.body()!!.seen.toString(),Toast.LENGTH_LONG).show()
                    }catch (E:Exception){ }
                }
                override fun onFailure(call: Call<ResponseNotification>, throwable: Throwable) {
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getNotifications()
    }


}
