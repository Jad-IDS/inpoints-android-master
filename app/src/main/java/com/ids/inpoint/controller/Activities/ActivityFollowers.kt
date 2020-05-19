package com.ids.inpoint.controller.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterGridFollower
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseFollowers
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_followers.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_general.*
import kotlinx.android.synthetic.main.toolbar_general.linearToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityFollowers : AppCompactBase(), RVOnItemClickListener {

    private lateinit var adapterGridFollower: AdapterGridFollower
    private var followers = java.util.ArrayList<ResponseFollowers>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)
        init()
    }

    private fun init() {
        supportActionBar!!.hide()
        btBack.setOnClickListener { super.onBackPressed() }

        var pageUserId = intent.getIntExtra(AppConstants.PAGE_USER_ID, 0)
        getFollowers(pageUserId)
    }

    private fun getFollowers(id: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getFollowers(id)?.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseFollowers>>,
                    response: Response<ArrayList<ResponseFollowers>>
                ) {
                    try {
                        onFollowersReceived(response.body()!!)
                    } catch (E: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseFollowers>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                    Toast.makeText(this@ActivityFollowers, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onFollowersReceived(response: ArrayList<ResponseFollowers>) {
        followers = response
        adapterGridFollower = AdapterGridFollower(followers, this)
        val glm = GridLayoutManager(this, 3)
        rvFollowers!!.adapter = adapterGridFollower
        rvFollowers!!.layoutManager = glm
        loading.visibility = View.GONE
    }

    override fun onItemClicked(view: View, position: Int) {
        if (MyApplication.userLoginInfo.id!! == adapterGridFollower.items[position].userId!!) {
            startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_MY_PROFILE_FRAG))
        } else {
            MyApplication.selectedPost= ResponsePost()
            MyApplication.selectedPost.userId=adapterGridFollower.items[position].userId
            MyApplication.selectedPost.image=adapterGridFollower.items[position].image
            MyApplication.selectedPost.userName=adapterGridFollower.items[position].userName
            startActivity(Intent(this,ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
        }
    }

    override fun onResume() {
        try{linearToolbar.background.alpha=255}catch (e:Exception){}
        super.onResume()
    }
}
