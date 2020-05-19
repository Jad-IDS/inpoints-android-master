package com.ids.inpoint.controller.Fragments



import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Activities.ActivityInsideComment
import com.ids.inpoint.controller.Activities.ActivityProfile
import com.ids.inpoint.controller.Adapters.*

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class FragmentSearch : Fragment() ,RVOnItemClickListener {



    private var adapterSearch: AdapterSearch?=null
    private var arraySearch= java.util.ArrayList<ResponseSearch>()
    private var arrayFiltered= java.util.ArrayList<ResponseSearch>()

    private var adapterTabs: AdapterTabs?=null
    private var arrayTabs= java.util.ArrayList<ItemSearchTab>()

    override fun onItemClicked(view: View, position: Int) {

        if(view.id==R.id.linearTab){
            setSearch(adapterTabs!!.itemTabs[position].id!!)
            setSelectedtab(position)
        }else if(view.id==R.id.linearItemUser) {
            if(adapterSearch!!.arrayUsers[position].type==AppConstants.SEARCH_TYPE_USERS ){

         /*       MyApplication.selectedPost= ResponsePost()
                MyApplication.selectedPost.userId=adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1).toInt()
                MyApplication.selectedPost.image=adapterSearch!!.arrayUsers[position].image
                startActivity(Intent(activity, ActivityProfile::class.java)
                    .putExtra(AppConstants.USER_ID,(adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1)).toInt())
                    .putExtra(AppConstants.USER_IMAGE,adapterSearch!!.arrayUsers[position].image)

                )*/

                if (MyApplication.userLoginInfo.id!! == (adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1)).toInt()) {
                    startActivity(Intent(activity, ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_MY_PROFILE_FRAG))
                } else {
                    MyApplication.selectedPost= ResponsePost()
                    MyApplication.selectedPost.userId=(adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1)).toInt()
                    MyApplication.selectedPost.image=adapterSearch!!.arrayUsers[position].image
                    MyApplication.selectedPost.userName=adapterSearch!!.arrayUsers[position].username
                    startActivity(Intent(activity, ActivityHome::class.java).putExtra(AppConstants.DEFAULT_FRAG,AppConstants.DEFAULT_OTHER_USER_PROFILE_FRAG))
                }



            }else if(adapterSearch!!.arrayUsers[position].type==AppConstants.SEARCH_TYPE_POST || adapterSearch!!.arrayUsers[position].type==AppConstants.SEARCH_TYPE_EVENT  ){
                MyApplication.selectedPost= ResponsePost()
                startActivity(Intent(activity, ActivityInsideComment::class.java)
                    .putExtra(AppConstants.POST_ID,adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1)))

            }else if(adapterSearch!!.arrayUsers[position].type==AppConstants.SEARCH_TYPE_STARTUP ){

                MyApplication.selectedStartupTeam= TeamStartup()
                MyApplication.selectedStartupTeam.id=(adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1)).toInt()
                MyApplication.selectedStartupTeam.image=adapterSearch!!.arrayUsers[position].image
                MyApplication.selectedStartupTeam.userName=null

                AppHelper.AddFragment(
                    this.fragmentManager!!,
                    AppConstants.STARTUP_PROFILE,
                    FragmentProfile(),
                    AppConstants.STARTUP_PROFILE_FRAG
                )
            }else if(adapterSearch!!.arrayUsers[position].type==AppConstants.SEARCH_TYPE_TEAMS ){

                MyApplication.selectedStartupTeam= TeamStartup()
                MyApplication.selectedStartupTeam.id=(adapterSearch!!.arrayUsers[position].link!!.substring(adapterSearch!!.arrayUsers[position].link!!.lastIndexOf("=")+1)).toInt()
                MyApplication.selectedStartupTeam.image=adapterSearch!!.arrayUsers[position].image
                MyApplication.selectedStartupTeam.userName=null

                AppHelper.AddFragment(
                    this.fragmentManager!!,
                    AppConstants.TEAM_PROFILE,
                    FragmentProfile(),
                    AppConstants.TEAM_PROFILE_FRAG
                )
            }






          //  startActivity(Intent(activity, ActivityChat::class.java))

        }

    }


    private fun setSelectedtab(position: Int){
        for (i in arrayTabs.indices)
            arrayTabs[i].selected=false
        arrayTabs[position].selected=true
        adapterTabs!!.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_search, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setlisteners()
    }

    private fun init(){

        ivToolbarBack.visibility=View.VISIBLE
        setlisteners()
        try{AppHelper.setRoundImageResize(activity!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+MyApplication.userLoginInfo.image!!,MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}

        ivChat.visibility=View.GONE
        ivNotification.visibility=View.GONE
        ivToolbarProfile.visibility=View.GONE
        arrayTabs.clear()
        arrayTabs.add(ItemSearchTab(AppConstants.SEARCH_TYPE_USERS,getString(R.string.tab_users),true))
        arrayTabs.add(ItemSearchTab(AppConstants.SEARCH_TYPE_COMPANIES,getString(R.string.tab_companies),false))
        arrayTabs.add(ItemSearchTab(AppConstants.SEARCH_TYPE_TEAMS,getString(R.string.tab_teams),false))
        arrayTabs.add(ItemSearchTab(AppConstants.SEARCH_TYPE_STARTUP,getString(R.string.tab_startups),false))
        arrayTabs.add(ItemSearchTab(AppConstants.SEARCH_TYPE_POST,getString(R.string.tab_post),false))
        arrayTabs.add(ItemSearchTab(AppConstants.SEARCH_TYPE_EVENT,getString(R.string.tab_event),false))


        tvSearch.visibility=View.GONE
        svSearch.visibility=View.VISIBLE

       // val searchView = findViewById(R.id.search) as android.widget.SearchView
        val searchEditText =svSearch.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        searchEditText.setHintTextColor(ContextCompat.getColor(activity!!, R.color.white))
        searchEditText.textSize = 12f



        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                  search(query)
                return false
            }

        })


        try {
          svSearch.onActionViewExpanded()

        }catch (e:Exception){}
    }

    private fun setlisteners(){
        ivToolbarBack.setOnClickListener{activity!!.onBackPressed()}
   }


    private fun search(key:String){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.search(key,true)?.enqueue(object : Callback<ArrayList<ResponseSearch>> {
                override fun onResponse(call: Call<ArrayList<ResponseSearch>>, response: Response<ArrayList<ResponseSearch>>) {

                    try{

                        setUsers(response.body()!!)
                        setTabs()
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseSearch>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setUsers(response: ArrayList<ResponseSearch>) {
        arraySearch.clear()
        arraySearch.addAll(response)
        setSearch(AppConstants.SEARCH_TYPE_USERS)
    }


    private fun setSearch(type:Int){
        adapterSearch= AdapterSearch(this.filterArraySearch(type)!!,this,0)
        val glm = GridLayoutManager(activity, 1)
        rvSearch!!.adapter=adapterSearch
        rvSearch!!.layoutManager=glm
    }

    private fun filterArraySearch(type:Int):ArrayList<ResponseSearch>?{
        arrayFiltered.clear()
        for (i in arraySearch.indices){
            if(arraySearch[i].type==type)
                arrayFiltered.add(arraySearch[i])
        }
        return arrayFiltered
    }


    private fun setTabs(){
        rvTabSearch.visibility=View.VISIBLE
        adapterTabs= AdapterTabs(arrayTabs,this,1)
        val llm = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        rvTabSearch.adapter=adapterTabs
        rvTabSearch.layoutManager=llm
    }

}
