package com.ids.inpoint.controller.Fragments

import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.ids.inpoint.controller.Activities.ActivityTeamRequest
import com.ids.inpoint.controller.Adapters.AdapterTeamStartup
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_teams.*
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTeams : Fragment(), RVOnItemClickListener {

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
        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        tabs = arrayOf<Button>(btMyTeams, btAllTeams)

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

        getMyTeams()
        btMyTeams.setOnClickListener {
            if (selectedTab != 0) {
                selectTab(0)
                getMyTeams()
            }
        }

        btAllTeams.setOnClickListener {
            if (selectedTab != 1) {
                selectTab(1)
                getAllTeams()
            }
        }

        btSearchTeams.setOnClickListener {
            resetParams()
            getTeams()
        }

        btAddTeam.setOnClickListener {
            llAddTeam.visibility = View.VISIBLE
        }

        etTeamName!!.addTextChangedListener(object :
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

        etSearchTeams!!.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    btClearSearch.visibility = View.VISIBLE
                }else{
                    btClearSearch.visibility = View.GONE
                }
            }
        })

        btClearSearch.setOnClickListener {
            etSearchTeams.setText("")
        }

        btClear.setOnClickListener {
            etTeamName.setText("")
        }

        btCancel.setOnClickListener {
            etTeamName.setText("")
            llAddTeam.visibility = View.GONE
        }

        btSave.setOnClickListener {
            val userName = etTeamName.text.toString().trim()
            if (userName.isEmpty()) {
                AppHelper.createDialog(activity!!,getString(R.string.team_name_is_required))
            } else {
                loading.visibility = View.VISIBLE
                RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
                    ?.saveTeam(TeamStartup(userName = userName, showToPublic = isPublic))
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
                                    getString(R.string.team_creation_failed),
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
                tvTeamType.text = context!!.resources.getString(R.string.public_team)
                ivPrivate.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.visibility_on_white))
            } else {
                tvTeamType.text = context!!.resources.getString(R.string.private_team)
                ivPrivate.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.visibility_off_white))
            }
        }

        svTeams.viewTreeObserver
            .addOnScrollChangedListener {
                try {
                    if (svTeams.getChildAt(0).bottom <= svTeams.height + svTeams.scrollY && selectedTab == 1 && etSearchTeams.text.toString().isEmpty()) {
                        if (!isLoading) {
                            isLoading = true
                            skip += take
                            getTeams()
                        }
                    }
                } catch (E: java.lang.Exception) {
                }

            }

        srlTeams.setOnRefreshListener {
            resetParams()
            getTeams()
        }
        selectTab(0)
    }

    private fun selectTab(tabIndex: Int) {
        isLoading = false
        selectedTab = tabIndex
        resetParams()

        tabs.forEachIndexed(fun(index: Int, button: Button) {
            if (tabIndex == index) {
               if(MyApplication.languageCode==AppConstants.LANG_ENGLISH)
                  button.setBackgroundResource(R.drawable.right_corners_secondary_filled)
                else
                   button.setBackgroundResource(R.drawable.left_corners_secondary_filled)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    button.setTextColor(
                        ContextCompat.getColor(
                            this.activity!!,
                            R.color.white_trans
                        )
                    )
                } else {
                    button.setTextColor(activity!!.resources.getColor(R.color.white_trans))
                }
            } else {
                if(MyApplication.languageCode==AppConstants.LANG_ENGLISH)
                    button.setBackgroundResource(R.drawable.left_corners_secondary)
                else
                    button.setBackgroundResource(R.drawable.right_corners_secondary)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    button.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black));
                } else {
                    button.setTextColor(activity!!.resources.getColor(R.color.black))
                }
            }
        })
    }

    private fun getTeams() {
        if (selectedTab == 0) {
            getMyTeams()
        } else {
            getAllTeams()
        }
    }

    private fun getMyTeams() {
        loading.visibility = View.VISIBLE
        var searchKey = etSearchTeams.text.toString()

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getMyTeams(searchKey)?.enqueue(object : Callback<ArrayList<TeamStartup>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartup>>,
                    response: Response<ArrayList<TeamStartup>>
                ) {
                    try {
                        onTeamsResponseReceived(response.body()!!)
                    } catch (E: java.lang.Exception) {
                        try{loading.visibility = View.GONE}catch (e:java.lang.Exception){}
                    }
                }

                override fun onFailure(call: Call<ArrayList<TeamStartup>>, throwable: Throwable) {
                   try{ loading.visibility = View.GONE}catch (e:Exception){}
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getAllTeams() {
        loading.visibility = View.VISIBLE
        var searchKey = etSearchTeams.text.toString()

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllTeams(searchKey, skip, take, maxId)
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
                        loading.visibility = View.GONE
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

        if (etSearchTeams.text.toString().isNotEmpty()) {
            adapterTeamStartup = null
            arrayTeams.clear()
        }

        arrayTeams.addAll(response)

        if (adapterTeamStartup == null) {
            adapterTeamStartup = AdapterTeamStartup(arrayTeams, this, selectedTab == 1,AppConstants.ADAPTER_TEAM)
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
        llAddTeam.visibility = View.GONE
        etTeamName.setText("")
        Toast.makeText(
            activity,
            getString(R.string.team_created),
            Toast.LENGTH_SHORT
        ).show()
        resetParams()
        getTeams()
    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.btItemTeamAction && arrayTeams[position].canJoin == AppConstants.TEAM_TYPE_CAN_JOIN ) {
           joinTeam(arrayTeams[position].id!!)
            arrayTeams[position].canJoin=AppConstants.TEAM_TYPE_PENDING
            adapterTeamStartup!!.notifyDataSetChanged()
        }

        else if (view.id == R.id.btItemTeamAction && arrayTeams[position].canJoin == AppConstants.TEAM_TYPE_PENDING ) {
            joinTeam(arrayTeams[position].id!!)
            arrayTeams[position].canJoin=AppConstants.TEAM_TYPE_CAN_JOIN
            adapterTeamStartup!!.notifyDataSetChanged()
        }
        else if (view.id == R.id.btItemTeamAction && arrayTeams[position].canJoin == AppConstants.TEAM_TYPE_JOINED &&  arrayTeams[position].admin!! ) {
            MyApplication.selectedStartupTeam = arrayTeams[position]
            startActivity(Intent(activity!!, ActivityTeamRequest::class.java))
        }

        else {
            MyApplication.selectedStartupTeam = arrayTeams[position]
            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.TEAM_PROFILE,
                FragmentProfile(),
                AppConstants.TEAM_PROFILE_FRAG
            )
        }
    }

    private fun resetParams() {
        skip = 0
        take = 6
        maxId = 0
        adapterTeamStartup = null
        arrayTeams.clear()
    }



    private fun joinTeam(id:Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.joinTeam(id)?.enqueue(object : Callback<Boolean> {
                override fun onResponse(
                    call: Call<Boolean>,
                    response: Response<Boolean>
                ) {
                    loading.visibility = View.GONE
                }

                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    try{ loading.visibility = View.GONE}catch (e:Exception){}
                }
            })
    }
}


