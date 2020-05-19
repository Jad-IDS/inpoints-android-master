package com.ids.inpoint.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterAllUsers
import com.ids.inpoint.controller.Adapters.AdapterGridTeamMember
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.model.response.ResponseUser
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_team_members.*
import kotlinx.android.synthetic.main.activity_team_members.btCancel
import kotlinx.android.synthetic.main.activity_team_members.btClear
import kotlinx.android.synthetic.main.activity_team_members.btSave
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar_general.*
import kotlinx.android.synthetic.main.toolbar_general.linearToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityStartupMembers : AppCompactBase(), RVOnItemClickListener {

    private var lastFragmentId = 0
    private lateinit var adapterGridTeamMember: AdapterGridTeamMember
    private var startupMembers = java.util.ArrayList<TeamStartupMember>()
    private lateinit var teamMember: TeamStartupMember
    private var teamAdmin: Boolean = false
    private var edit: Boolean = false

    lateinit var adapterAllUsers: AdapterAllUsers
    var arrayFilteredUsers = java.util.ArrayList<ResponseUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_members)

        init()
    }

    private fun init() {
        teamMember = TeamStartupMember()
        getAllUsers()
        getstartupMembers(MyApplication.selectedStartupTeam.id!!)
        supportActionBar!!.hide()
        btBack.setOnClickListener { super.onBackPressed() }

        btAddTeamMember.setOnClickListener {
            llAddTeamMember.visibility = View.VISIBLE
        }

        btClear.setOnClickListener {
            etUser.setText("")
        }

        btCancel.setOnClickListener {
            llAddTeamMember.visibility = View.GONE
            arrayFilteredUsers.clear()
            etUser.setText("")
            swAdmin.isChecked = false
            edit = false
            etUser.isEnabled = true
            btClear.isEnabled = true
        }

        btSave.setOnClickListener {

            if (teamMember.userId == null) {
                AppHelper.createDialog(this, getString(R.string.user_is_required))
            } else {
                teamMember.admin = swAdmin.isChecked
                saveStartupMember()
                btCancel.callOnClick()
            }
        }

        etUser!!.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edit) {
                    etUser.isEnabled = false
                    btClear.isEnabled = false
                } else {
                    if(s.toString().isNotEmpty()){
                        btClear.visibility = View.VISIBLE
                    }else{
                        btClear.visibility = View.GONE
                    }
                    filterUsers(s.toString())
                }
            }
        })
    }

    private fun getstartupMembers(id: Int) {
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupMembers(id)?.enqueue(object : Callback<ArrayList<TeamStartupMember>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartupMember>>,
                    response: Response<ArrayList<TeamStartupMember>>
                ) {
                    try {

                        onTeamMembersReceived(response.body()!!)
                    } catch (E: Exception) {
                    }
                    loading.visibility = View.GONE;
                }

                override fun onFailure(
                    call: Call<ArrayList<TeamStartupMember>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE;
                    Toast.makeText(this@ActivityStartupMembers, getString(R.string.failed), Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onTeamMembersReceived(response: ArrayList<TeamStartupMember>) {
        startupMembers.clear()
        startupMembers = response

        var a = startupMembers.filter {
            it.userId == MyApplication.userLoginInfo.id && it.admin == true
        }.any()
        onTeamAdminChanged(a)

//        startupMembers.forEach {
//            it.level = MyApplication.arrayUsers.single { responseUser -> responseUser.id == it.id }.
//        }

        adapterGridTeamMember = AdapterGridTeamMember(startupMembers, this, teamAdmin)
        rvTeamMembers.adapter = adapterGridTeamMember
        var grid = GridLayoutManager(this, 3)
        rvTeamMembers.layoutManager = grid
    }

    private fun getAllUsers() {
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllUsers()?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseUser>>,
                    response: Response<ArrayList<ResponseUser>>
                ) {
                    try {
                        MyApplication.arrayUsers.clear()
                        MyApplication.arrayUsers.addAll(response.body()!!)
                        setAllUsers()
                    } catch (e: java.lang.Exception) {
                    }
                    loading.visibility = View.GONE
                }

                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun setAllUsers() {
        arrayFilteredUsers.clear()
        arrayFilteredUsers.addAll(MyApplication.arrayUsers)
        adapterAllUsers = AdapterAllUsers(arrayFilteredUsers, this, AppConstants.PICK_IMAGE_PARTNER)
        val glm = GridLayoutManager(this, 1)
        rvAllUsers!!.adapter = adapterAllUsers
        rvAllUsers!!.layoutManager = glm
    }

    private fun saveStartupMember() {
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveStartupMember(teamMember)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    getstartupMembers(MyApplication.selectedStartupTeam.id!!)
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility = View.GONE;
                    Toast.makeText(applicationContext, "error..", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun deleteStartupMember() {
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupMember(teamMember.id!!)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    getstartupMembers(MyApplication.selectedStartupTeam.id!!)
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility = View.GONE;
                    Toast.makeText(applicationContext, "error..", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun filterUsers(word: String) {
        if (MyApplication.arrayUsers.size > 0) {
            arrayFilteredUsers.clear()

            var notMembers =
                MyApplication.arrayUsers.filter { ru -> startupMembers.all { it.userId != ru.id } }

            for (i in notMembers.indices) {
                if (notMembers[i].userName!!.toLowerCase().contains(word.toLowerCase()))
                    arrayFilteredUsers.add(notMembers[i])
            }
            adapterAllUsers.notifyDataSetChanged()

            if (word.isEmpty() || arrayFilteredUsers.size == 0) {
                llUsers!!.visibility = View.GONE
                teamMember = TeamStartupMember()
            } else
                llUsers!!.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        when (view.id) {
            R.id.linearItemUser -> {
                teamMember = TeamStartupMember()
                teamMember.userId = adapterAllUsers.arrayUsers[position].id!!
                teamMember.StartupId = MyApplication.selectedStartupTeam.id

                arrayFilteredUsers.add(adapterAllUsers.arrayUsers[position])
                etUser!!.setText(adapterAllUsers.arrayUsers[position].userName.toString())
                llUsers!!.visibility = View.GONE
            }

            R.id.btDelete -> {
                teamMember = startupMembers[position]
                var delete = AppHelper.createYesNoDialog(
                    this,
                    getString(R.string.delete_confirm_member)
                ) { deleteStartupMember() }
            }

            R.id.btEdit -> {
                edit = true
                llAddTeamMember.visibility = View.VISIBLE
                teamMember = startupMembers[position]

                etUser.setText(startupMembers[position].name)
                swAdmin.isChecked = startupMembers[position].admin == true
                edit = false
                etUser.isEnabled = false
                btClear.isEnabled = false
            }

            else -> {
                if (MyApplication.userLoginInfo.id!! == startupMembers[position].userId!!) {
                    startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_MY_PROFILE_FRAG))
                } else {
                    MyApplication.selectedPost= ResponsePost()
                    MyApplication.selectedPost.userId=adapterGridTeamMember.teamMembers[position].userId
                    MyApplication.selectedPost.image=adapterGridTeamMember.teamMembers[position].image
                    MyApplication.selectedPost.userName=adapterGridTeamMember.teamMembers[position].name
                    startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
                }
            }
        }
    }

    private fun onTeamAdminChanged(value: Boolean) {
        teamAdmin = value
        if (!value) {
            btCancel.callOnClick()
            llAddTeamMemberButtonContainer.visibility = View.GONE
        } else {
            llAddTeamMemberButtonContainer.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {

//        supportFragmentManager.fragments.forEach {
//            it.childFragmentManager.fragments.forEach { fragment ->
//                if (fragment.id == lastFragmentId) {
//                    //do something
//                    it.childFragmentManager.beginTransaction().remove(fragment).commit()
//                }
//            }
//        }

        fragment_holder.visibility = View.INVISIBLE
        var lastFragment = supportFragmentManager.findFragmentById(lastFragmentId)
        if (lastFragment != null)
            supportFragmentManager.beginTransaction().remove(lastFragment).commit()
        fragment_holder.visibility = View.GONE
        //  super.onBackPressed()

    }

    override fun onResume() {
        try{linearToolbar.background.alpha = 255}catch (e: java.lang.Exception){}
        super.onResume()
    }
}
