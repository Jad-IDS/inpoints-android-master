package com.ids.inpoint.controller.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterTeamRequests
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.Fragments.ResponseTeamRequest
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_team_projects.btClose
import kotlinx.android.synthetic.main.activity_team_projects.etSearchFile
import kotlinx.android.synthetic.main.activity_team_requests.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_general.*


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityTeamRequest : AppCompactBase(),RVOnItemClickListener {

    private var adapterRequests: AdapterTeamRequests? = null
    private var arrayRequests = java.util.ArrayList<ResponseTeamRequest>()
    private var arrayRequestsFiltered = java.util.ArrayList<ResponseTeamRequest>()

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.btAcceptRequest)
            saveTeamRequest(adapterRequests!!.arrayUsers[position].id!!, position, true, false)
         else if (view.id == R.id.btCancelRequest)
            saveTeamRequest(adapterRequests!!.arrayUsers[position].id!!,position,false,true)
        else{
            try{
                MyApplication.selectedPost= ResponsePost()
                MyApplication.selectedPost.userId=adapterRequests!!.arrayUsers[position].userId
                MyApplication.selectedPost.image=adapterRequests!!.arrayUsers[position].image
                MyApplication.selectedPost.userName=adapterRequests!!.arrayUsers[position].userName
                startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
            }catch (e: java.lang.Exception){}


        }

    }


    private fun saveTeamRequest(id:Int,position: Int,approved:Boolean,rejected:Boolean){
        val builder = AlertDialog.Builder(this)
        var messsage=""
        if(approved)
            messsage=getString(R.string.confirm_accept_request)
        else
            messsage=getString(R.string.confirm_delete_request)

        builder
            .setMessage(messsage)
            .setCancelable(true)
            .setNegativeButton(getString(R.string.no)) {
                    dialog, _ -> dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                acceptTeamRequest(adapterRequests!!.arrayUsers[position].id!!,position,approved,rejected)

                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    private lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_requests)
        init()

    }


    private fun init(){
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
        setlisteners()

        getTeamRequests()
        etSearchFile!!.addTextChangedListener(object: TextWatcher {override fun afterTextChanged(s: Editable?) {

        }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                filterFiles(s.toString())
            }

        })


        btClose.setOnClickListener{
            btClose.visibility=View.GONE
            etSearchFile.setText("")
            adapterRequests!!.notifyDataSetChanged()
            try{hideKeyboard(it) }catch (e: java.lang.Exception){}
        }

        try{
            AppHelper.setRoundImageResize(this,ivToolbarProfile, AppConstants.IMAGES_URL+"/"+ MyApplication.userLoginInfo.image!!,
                MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}


    }


    private fun filterFiles(word:String){
        if(arrayRequests.size>0 && word.isNotEmpty()){
            btClose.visibility=View.VISIBLE
            arrayRequestsFiltered.clear()
            for(i in arrayRequests.indices){
                if(arrayRequests[i].userName!!.contains(word))
                    arrayRequestsFiltered.add(arrayRequests[i])
            }
        }else {
            btClose.visibility=View.GONE
            arrayRequestsFiltered.addAll(arrayRequests)
        }
        adapterRequests!!.notifyDataSetChanged()
    }


    private fun acceptTeamRequest(id:Int,position:Int,approved:Boolean,rejected:Boolean){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.acceptTeamRequest(
                id,
                approved,
                rejected
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{
                        if(response.body()!!){
                            arrayRequestsFiltered.removeAt(position)
                            adapterRequests!!.notifyDataSetChanged()
                        }


                    }catch (E: Exception){


                    }
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })
    }

    private fun setlisteners(){
        btBack.setOnClickListener{this.onBackPressed()}

    }

    private fun getTeamRequests(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllTeamRequests(
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseTeamRequest>> {
                override fun onResponse(call: Call<ArrayList<ResponseTeamRequest>>, response: Response<ArrayList<ResponseTeamRequest>>) {
                    try{
                        onTeamRequestRetrieved(response.body())

                    }catch (E: Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseTeamRequest>>, throwable: Throwable) {
                }
            })
    }


    private fun onTeamRequestRetrieved(body: ArrayList<ResponseTeamRequest>?) {
        arrayRequestsFiltered.clear()
        arrayRequests.clear()
        arrayRequestsFiltered.addAll(body!!)
        arrayRequests.addAll(body)

        adapterRequests = AdapterTeamRequests(arrayRequestsFiltered, this, 1)
        val glm = GridLayoutManager(this, 1)
        rvRequests.adapter = adapterRequests
        rvRequests.layoutManager = glm
    }

}
