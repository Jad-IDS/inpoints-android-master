package com.ids.inpoint.controller.Fragments


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView

import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Activities.ActivityInsideCourse
import com.ids.inpoint.controller.Activities.ActivityInsidePublication
import com.ids.inpoint.controller.Adapters.AdapterEvents
import com.ids.inpoint.controller.Adapters.AdapterOnlineCourses
import com.ids.inpoint.controller.Adapters.AdapterOnlinePublication

import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.*
import com.ids.inpoint.model.response.ResponseOnlineCourses
import com.ids.inpoint.model.response.ResponseOnlinePublication

import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_elearning.*
import kotlinx.android.synthetic.main.fragment_events.linearSortCategory
import kotlinx.android.synthetic.main.fragment_events.rvCategory
import kotlinx.android.synthetic.main.fragment_events.rvEvents
import kotlinx.android.synthetic.main.fragment_events.tvSelectedSortCategory
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentElearning : Fragment() ,RVOnItemClickListener {
    lateinit var adapterSort: AdapterSpinner
    var arraySort= java.util.ArrayList<ItemSpinner>()

    lateinit var adapterOnlineCourses: AdapterOnlineCourses
    var arrayOnlineCourses= java.util.ArrayList<ResponseOnlineCourses>()

    lateinit var adapterOnlinePublication: AdapterOnlinePublication
    var arrayOnlinePublication= java.util.ArrayList<ResponseOnlinePublication>()

    private var isSortClicked=false


    lateinit var adapterEvents: AdapterEvents
    var arrayFeeds= java.util.ArrayList<News_feed>()
    var arrayComments= java.util.ArrayList<comments>()

    private var searchText=""
    private var sort=""

    private var isOnlineCourses=true



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_elearning, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        searchText=""
        isOnlineCourses=true
        setSortSpinner()
        try{
            AppHelper.setRoundImageResize(activity!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+ MyApplication.userLoginInfo.image!!,
                MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}

        linearSortCategory.setOnClickListener{
            if(!isSortClicked){
                rvCategory.visibility=View.VISIBLE
                isSortClicked=true
            }else{
                rvCategory.visibility=View.GONE
                isSortClicked=false
            }
        }


        icSearch.setOnClickListener{
            svSearch.requestFocus()
            var imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(svSearch, InputMethodManager.SHOW_IMPLICIT)

        }

        try{ setEvents()}catch (e:Exception){}

        getOnlineCourses()

        btOnlineCourses.setOnClickListener{
            try{sort=arraySort[0].name!!}catch (e:java.lang.Exception){}
            btClear.visibility=View.GONE
            searchText=""
            etSearch.setText("")
            selectedOnline.visibility=View.VISIBLE
            selectedPublication.visibility=View.INVISIBLE
            AppHelper.setTextColor(activity!!,tvOnlineCourses,R.color.secondary)
            AppHelper.setTextColor(activity!!,tvPublications,R.color.gray_dark)
            ivOnlineCourses.setTint(R.color.secondary)
            ivPublication.setTint(R.color.gray_dark)
            isOnlineCourses=true
            getOnlineCourses()
        }
        btOnlinePublications.setOnClickListener{
            searchText=""
            try{sort=arraySort[0].name!!}catch (e:java.lang.Exception){}
            btClear.visibility=View.GONE
            etSearch.setText("")
            selectedOnline.visibility=View.INVISIBLE
            selectedPublication.visibility=View.VISIBLE
            isOnlineCourses=false
            AppHelper.setTextColor(activity!!,tvOnlineCourses,R.color.gray_dark)
            AppHelper.setTextColor(activity!!,tvPublications,R.color.secondary)
            ivOnlineCourses.setTint(R.color.gray_dark)
            ivPublication.setTint(R.color.secondary)
            getOnlinePublication()
        }

        btSearch.setOnClickListener{
            if(etSearch.text.toString().isNotEmpty()){
                searchText=etSearch.text.toString()
                getData()
                try{view?.let { activity?.hideKeyboard(it) }}catch (e: java.lang.Exception){}
            }

        }


        linearSearch.setOnClickListener{
            try{(activity!! as ActivityHome).openSearch(AppConstants.ELEARNING)}catch (e: java.lang.Exception){ }
        }
        ivChat.setOnClickListener{ try{(activity!! as ActivityHome).goToUserChat(AppConstants.ELEARNING)}catch (e: java.lang.Exception){ }}
        ivNotification.setOnClickListener{ try{(activity!! as ActivityHome).goNotification(AppConstants.ELEARNING)}catch (e: java.lang.Exception){ }}

        linearToolbar.background.alpha=255


        etSearch!!.addTextChangedListener(object: TextWatcher {override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btClear.visibility=View.VISIBLE
            }

        })

        btClear.setOnClickListener{
            btClear.visibility=View.GONE
            searchText=""
            etSearch.setText("")
            getData()
            try{view?.let { activity?.hideKeyboard(it) }}catch (e: java.lang.Exception){}
        }
    }

    private fun setEvents(){
        adapterEvents= AdapterEvents(arrayFeeds,this)
        val glm = GridLayoutManager(activity, 1)
        rvEvents.adapter=adapterEvents
        rvEvents.layoutManager=glm
    }

    fun ImageView.setTint(@ColorRes colorRes: Int) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
    }


    private fun setSortSpinner(){

        arraySort.clear()
        arraySort.add(ItemSpinner(1,getString(R.string.latest)))
        arraySort.add(ItemSpinner(2,getString(R.string.name)))
        arraySort.add(ItemSpinner(3,getString(R.string.categories)))
        adapterSort= AdapterSpinner(arraySort,this,AppConstants.SPINNER_EVENT_SORT)
        val glm = GridLayoutManager(activity, 1)
        rvCategory.adapter=adapterSort
        rvCategory.layoutManager=glm
        sort=arraySort[0].name!!
    }




    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.IdSpinnerSort){
            tvSelectedSortCategory.text = adapterSort.itemSpinner[position].name
            rvCategory.visibility=View.GONE
            isSortClicked=false
            sort=adapterSort.itemSpinner[position].name!!
            getData()
        }else{
            try{
            if(isOnlineCourses){
                MyApplication.selectedOnlineCourse=adapterOnlineCourses.courses[position]
                startActivity(Intent(activity, ActivityInsideCourse::class.java))
            }else{
                MyApplication.selectedOnlinePublication=adapterOnlinePublication.publication[position]
                startActivity(Intent(activity, ActivityInsidePublication::class.java))
            }



            }catch (e:Exception){}
        }
    }


    private fun getData(){
        if(isOnlineCourses)
            getOnlineCourses()
        else
            getOnlinePublication()
    }

    private fun getOnlineCourses(){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getOnlineCourses(searchText,sort)?.enqueue(object : Callback<ArrayList<ResponseOnlineCourses>> {
                override fun onResponse(call: Call<ArrayList<ResponseOnlineCourses>>, response: Response<ArrayList<ResponseOnlineCourses>>) {
                    try{
                        onOnlineCoursesRetrieved(response.body()!!)
                    }catch (E: java.lang.Exception){
                        try{loading.visibility=View.GONE}catch (e:Exception){}
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseOnlineCourses>>, throwable: Throwable) {
                    loading.visibility=View.GONE
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onOnlineCoursesRetrieved(response: ArrayList<ResponseOnlineCourses>) {
        loading.visibility=View.GONE
        arrayOnlineCourses.clear()
        arrayOnlineCourses.addAll(response)
        adapterOnlineCourses= AdapterOnlineCourses(arrayOnlineCourses,this)
        val glm = GridLayoutManager(activity, 2)
        rvElearning.adapter=adapterOnlineCourses
        rvElearning.layoutManager=glm
    }


    private fun getOnlinePublication(){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getOnlinePublications(searchText,sort)?.enqueue(object : Callback<ArrayList<ResponseOnlinePublication>> {
                override fun onResponse(call: Call<ArrayList<ResponseOnlinePublication>>, response: Response<ArrayList<ResponseOnlinePublication>>) {
                    try{
                        onOnlinePublicationRetrieved(response.body()!!)
                    }catch (E: java.lang.Exception){
                        loading.visibility=View.GONE
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseOnlinePublication>>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })
    }

    private fun onOnlinePublicationRetrieved(response: ArrayList<ResponseOnlinePublication>) {
        loading.visibility=View.GONE
        arrayOnlinePublication.clear()
        arrayOnlinePublication.addAll(response)
        adapterOnlinePublication= AdapterOnlinePublication(arrayOnlinePublication,this)
        val glm = GridLayoutManager(activity, 2)
        rvElearning.adapter=adapterOnlinePublication
        rvElearning.layoutManager=glm
    }

}
