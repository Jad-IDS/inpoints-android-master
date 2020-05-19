package com.ids.inpoint.controller.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.AdapterFollowers
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener

import com.ids.inpoint.model.Followers
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_fragment_profile.*
import kotlinx.android.synthetic.main.fragment_image.*


class FragmentImage : Fragment() ,RVOnItemClickListener {
    var ImageUrl=""
    override fun onItemClicked(view: View, position: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_image, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        ImageUrl = try{
            arguments!!.getString(AppConstants.URL)
        }catch (e: java.lang.Exception){
            ""
        }
        AppHelper.setImage(this!!.activity!!,ivFullImage,ImageUrl)

         ivClose.setOnClickListener{
           try{(activity!! as ActivityHome).removeFrag(AppConstants.IMAGE_FRAG)}catch (e:Exception){ }
       }
    }





}
