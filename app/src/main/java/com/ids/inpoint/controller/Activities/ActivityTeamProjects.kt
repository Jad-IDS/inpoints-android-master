package com.ids.inpoint.controller.Activities


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.TextView

import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterPopupTeamMembers
import com.ids.inpoint.controller.Adapters.AdapterPopupTeamTaskMembers
import com.ids.inpoint.controller.Adapters.AdapterTeamProjects

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ResponseTeamProject
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.model.response.ResponseUser
import com.ids.inpoint.utils.AppConstants

import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface

import kotlinx.android.synthetic.main.activity_team_projects.*
import kotlinx.android.synthetic.main.activity_team_projects.btClose
import kotlinx.android.synthetic.main.activity_team_projects.etSearchFile


import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityTeamProjects : AppCompactBase(),RVOnItemClickListener {

    private var adapterProjects: AdapterTeamProjects? = null
    private var arrayProjects = java.util.ArrayList<ResponseTeamProject>()
    private var arrayProjectsFiltered = java.util.ArrayList<ResponseTeamProject>()
    var rvPopUpUsers: RecyclerView ?= null
    private var dialog: Dialog? = null
    private var arrayMembers = java.util.ArrayList<ResponseUser>()
    private lateinit var adapterTeamMembers: AdapterPopupTeamTaskMembers

    override fun onItemClicked(view: View, position: Int) {
        when {
            view.id==R.id.btTeam -> {
                showUserDialog(adapterProjects!!.arrayProjects[position].id!!)
            }
            view.id==R.id.btEdit -> {
                startActivity(Intent(this, ActivityTeamProject::class.java)
                    .putExtra(AppConstants.PROJECT_ID, adapterProjects!!.arrayProjects[position].id!!))
            }
            view.id==R.id.btDelete -> deleteSelectedFile(adapterProjects!!.arrayProjects[position].id!!,position)
            view.id==R.id.btFollow->{
                adapterTeamMembers.arrayUsers[position].IsFollowed=!adapterTeamMembers.arrayUsers[position].IsFollowed!!
                adapterTeamMembers.notifyDataSetChanged()
                followUser(adapterTeamMembers.arrayUsers[position].UserId!!,position,AppConstants.POPUP_TYPE_LIKERS)
            }
            view.id==R.id.linearPopupUsers->{
                MyApplication.selectedPost= ResponsePost()
                MyApplication.selectedPost.userId=adapterTeamMembers.arrayUsers[position].UserId
                MyApplication.selectedPost.image=adapterTeamMembers.arrayUsers[position].image
                MyApplication.selectedPost.userName=adapterTeamMembers.arrayUsers[position].userName
                startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))

            }
        }

    }


    private fun deleteSelectedFile(id:Int,position: Int){
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(getString(R.string.delete_confirmation)+" \n "+ adapterProjects!!.arrayProjects[position].name)
            .setCancelable(true)
            .setNegativeButton(getString(R.string.no)) {
                    dialog, _ -> dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                deleteMyFile(adapterProjects!!.arrayProjects[position].id!!,position)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    private lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_projects)
        init()

    }


    private fun init(){
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager

        btAddProject.setOnClickListener {
            startActivity(Intent(this, ActivityTeamProject::class.java))
        }

        btBack.visibility= View.VISIBLE
        setlisteners()


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
            adapterProjects!!.notifyDataSetChanged()
            try{hideKeyboard(it) }catch (e: java.lang.Exception){}
        }

/*        try{
            AppHelper.setRoundImageResize(this,ivToolbarProfile, AppConstants.IMAGES_URL+"/"+ MyApplication.userLoginInfo.image!!,
                MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}*/

    }


    private fun filterFiles(word:String){
        if(arrayProjects.size>0 && word.isNotEmpty()){
            btClose.visibility=View.VISIBLE
            arrayProjectsFiltered.clear()
            for(i in arrayProjects.indices){
                if(arrayProjects[i].name!!.contains(word) || arrayProjects[i].name!!.contains(word))
                    arrayProjectsFiltered.add(arrayProjects[i])
            }
        }else {
            btClose.visibility=View.GONE
            arrayProjectsFiltered.clear()

            arrayProjectsFiltered.addAll(arrayProjects)
        }
        adapterProjects!!.notifyDataSetChanged()
    }


    private fun deleteMyFile(id:Int,position:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteProject(
                id,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{
                        if(response.body()!!){
                        /*    arrayProjectsFiltered.removeAt(position)
                            adapterProjects!!.notifyDataSetChanged()*/
                            getTeamProject()
                        }else{
                            //  AppHelper.createDialog(this@ActivityTeamFiles,response.errorBody()!!.string().replace("\"", ""))
                            AppHelper.createDialog(this@ActivityTeamProjects,"you cannot delete this project")

                        }


                    }catch (E: Exception){
                        AppHelper.createDialog(this@ActivityTeamProjects,getString(R.string.cannot_del_proj))

                    }
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })
    }

    private fun setlisteners(){
        btBack.setOnClickListener{this.onBackPressed()}

    }

    private fun getTeamProject(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamProjects(MyApplication.languageCode,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseTeamProject>> {
                override fun onResponse(call: Call<ArrayList<ResponseTeamProject>>, response: Response<ArrayList<ResponseTeamProject>>) {
                    try{
                       onProjectRetrieved(response.body())

                    }catch (E: Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseTeamProject>>, throwable: Throwable) {
                }
            })
    }


    private fun onProjectRetrieved(body: ArrayList<ResponseTeamProject>?) {
        arrayProjectsFiltered.clear()
        arrayProjects.clear()
        arrayProjectsFiltered.addAll(body!!)
        arrayProjects.addAll(body!!)

        adapterProjects = AdapterTeamProjects(arrayProjectsFiltered, this, 1)
        val glm = GridLayoutManager(this, 1)
        rvProjects.adapter = adapterProjects
        rvProjects.layoutManager = glm
    }


    override fun onResume() {
        getTeamProject()
        super.onResume()
    }



    fun showUserDialog(id:Int) {
        arrayMembers.clear()
        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_users)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        var tvPopupTitle = dialog!!.findViewById<View>(R.id.tvPopupTitle) as TextView
        var  btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        rvPopUpUsers= dialog!!.findViewById<View>(R.id.rvUsers) as RecyclerView
        tvPopupTitle.text=getString(R.string.team_members)
        getTeamMembers(id)
        btCancel.setOnClickListener{dialog!!.dismiss()}
        dialog!!.show();

    }



    private fun getTeamMembers(id: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPopupProjectMembers(id)?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseUser>>,
                    response: Response<ArrayList<ResponseUser>>
                ) {
                    try {

                        setTeamMembers(response.body()!!)
                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseUser>>,
                    throwable: Throwable
                ) {
                }
            })
    }


    private fun setTeamMembers(response: ArrayList<ResponseUser>){
        arrayMembers.clear()
        arrayMembers.addAll(response)
        adapterTeamMembers = AdapterPopupTeamTaskMembers(arrayMembers, this,0)
        val glm = GridLayoutManager(this,1)
        rvPopUpUsers!!.adapter = adapterTeamMembers
        rvPopUpUsers!!.layoutManager = glm
    }

    private fun followUser(id: Int,position:Int,type: Int) {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.follow(
                id
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                }

                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })
    }

}
