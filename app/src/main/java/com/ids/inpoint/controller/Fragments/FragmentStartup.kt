package com.ids.inpoint.controller.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.AdapterTeamStartup
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_startup.*

import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FragmentStartup : Fragment(), RVOnItemClickListener {

    private var isPublic = true;
    private var selectedTab = 0
    private var skip = 0
    private var take = 6
    private var maxId = 0
    private var isLoading = false

    private lateinit var tabs: Array<Button>

    private var adapterTeamStartup: AdapterTeamStartup? = null
    private var arrayTeams = java.util.ArrayList<TeamStartup>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_startup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {

        try {
            AppHelper.setRoundImageResize(
                activity!!, ivToolbarProfile,
                AppConstants.IMAGES_URL + "/" + MyApplication.userLoginInfo.image!!,
                MyApplication.isProfileImageLocal
            )
        } catch (E: java.lang.Exception) {
        }

        icSearch.setOnClickListener {
            svSearch.requestFocus()
            var imm =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(svSearch, InputMethodManager.SHOW_IMPLICIT)

        }

        linearSearch.setOnClickListener {
            try {
                (activity!! as ActivityHome).openSearch(AppConstants.TEAM)
            } catch (e: java.lang.Exception) {
            }
        }

        ivChat.setOnClickListener {
            try {
                (activity!! as ActivityHome).goToUserChat(AppConstants.TEAM)
            } catch (e: java.lang.Exception) {
            }
        }

        ivNotification.setOnClickListener {
            try {
                (activity!! as ActivityHome).goNotification(AppConstants.TEAM)
            } catch (e: java.lang.Exception) {
            }
        }

        linearToolbar.background.alpha = 255

        getAllStartups()


        btSearchStartups.setOnClickListener {
            resetParams()
            getAllStartups()
        }

        btAddStartup.setOnClickListener {
            llAddStartup.visibility = View.VISIBLE
        }

        etStartupName!!.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    btClear.visibility = View.VISIBLE
                }else{
                    btClear.visibility = View.GONE
                }
            }
        })


        btClearSearch.setOnClickListener {
            etSearchStartup.setText("")
        }

        btClear.setOnClickListener {
            etStartupName.setText("")
        }

        btCancel.setOnClickListener {
            etStartupName.setText("")
            llAddStartup.visibility = View.GONE
        }

        btSave.setOnClickListener {
            val userName = etStartupName.text.toString().trim()
            if (userName.isEmpty()) {
                AppHelper.createDialog(activity!!,getString(R.string.startup_name_is_required))
            } else {
                loading.visibility = View.VISIBLE
                RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
                    ?.saveStartup(TeamStartup(userName = userName, showToPublic = isPublic))
                    ?.enqueue(object : Callback<Int> {
                        override fun onResponse(
                            call: Call<Int>,
                            response: Response<Int>
                        ) {
                            try {
                                try{view?.let { activity?.hideKeyboard(it) }}catch (e: java.lang.Exception){}
                                onAddTeamResponseReceived(response.body()!!)
                            } catch (E: java.lang.Exception) {
                                loading.visibility = View.GONE
                                Toast.makeText(
                                    activity,
                                    getString(R.string.startup_creation_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Int>, throwable: Throwable) {
                            loading.visibility = View.GONE
                            // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }

        btPrivate.setOnClickListener {
            isPublic = !isPublic

            if (isPublic) {
                tvStartupType.text = context!!.resources.getString(R.string.public_team)
                ivPrivate.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.visibility_on_white))
            } else {
                tvStartupType.text = context!!.resources.getString(R.string.private_team)
                ivPrivate.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.visibility_off_white))
            }
        }

        svTeams.viewTreeObserver
            .addOnScrollChangedListener {
                try {
                    if (svTeams.getChildAt(0).bottom <= svTeams.height + svTeams.scrollY && selectedTab == 1 && etSearchStartup.text.toString().isEmpty()) {
                        if (!isLoading) {
                            isLoading = true
                            skip += take
                            getAllStartups()
                        }
                    }
                } catch (E: java.lang.Exception) {
                }

            }

        srlTeams.setOnRefreshListener {
            resetParams()
            getAllStartups()
        }

    }





    private fun getAllStartups() {
        try{loading.visibility = View.VISIBLE}catch (e:Exception){}
        var searchKey = etSearchStartup.text.toString()

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllStartup(searchKey, skip, take, maxId)
            ?.enqueue(object : Callback<ArrayList<TeamStartup>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartup>>,
                    response: Response<ArrayList<TeamStartup>>
                ) {
                    try {
                        isLoading = response.body()!!.isEmpty()
                        if (!response.body()!!.isEmpty()) {
                            maxId = response.headers().get("maxid").toString().toInt()
                            onTeamsResponseReceived(response.body()!!)
                        } else {
                            loading.visibility = View.GONE
                        }

                    } catch (E: java.lang.Exception) {
                        try{loading.visibility = View.GONE}catch (e:Exception){}
                    }
                }

                override fun onFailure(call: Call<ArrayList<TeamStartup>>, throwable: Throwable) {
                    loading.visibility = View.GONE
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onTeamsResponseReceived(response: ArrayList<TeamStartup>) {
        loading.visibility = View.GONE

        if (etSearchStartup.text.toString().isNotEmpty()) {
            adapterTeamStartup = null
            arrayTeams.clear()
        }

        arrayTeams.addAll(response)

        if (adapterTeamStartup == null) {
            adapterTeamStartup = AdapterTeamStartup(arrayTeams, this, true,AppConstants.ADAPTER_STARTUP)
            val glm = GridLayoutManager(activity, 3)
            rvTeams.adapter = adapterTeamStartup
            rvTeams.layoutManager = glm
        } else {
            val position = adapterTeamStartup!!.teamStartups.size
            adapterTeamStartup!!.notifyItemChanged(position)
        }
        srlTeams.isRefreshing = false;
    }

    private fun onAddTeamResponseReceived(body: Int) {
        llAddStartup.visibility = View.GONE
        etSearchStartup.setText("")
        Toast.makeText(
            activity,
            getString(R.string.team_created),
            Toast.LENGTH_SHORT
        ).show()
        resetParams()
        getAllStartups()
    }

    override fun onItemClicked(view: View, position: Int) {



            MyApplication.selectedStartupTeam = arrayTeams[position]
            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.STARTUP_PROFILE,
                FragmentProfile(),
                AppConstants.STARTUP_PROFILE_FRAG
            )

    }

    private fun resetParams() {
        skip = 0
        take = 6
        maxId = 0
        adapterTeamStartup = null
        arrayTeams.clear()
    }




}


