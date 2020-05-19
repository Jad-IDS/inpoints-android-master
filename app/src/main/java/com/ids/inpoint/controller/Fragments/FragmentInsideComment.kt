package com.ids.inpoint.controller.Fragments


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.AdapterComments
import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.controller.MyApplication.Companion.arrayComments
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.comments
import com.ids.inpoint.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_comment_bottomsheet.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.toolbar_general.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class FragmentInsideComment : Fragment() , RVOnItemClickListener {
    lateinit var adapterComments: AdapterComments
    var arrayComments= java.util.ArrayList<comments>()
    var comment1:comments?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_inside_comment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        tvToolbarTitle.text = ""

        btBack.setOnClickListener{
            try{(activity!! as ActivityHome).removeFrag(AppConstants.POST_FRAG)}catch (e:Exception){ }
        }


    }


    private fun initialize(){
        ivSend.setOnClickListener{
            comment1= comments("1",
                "Dany Soleiman",
                "https://www.eharmony.co.uk/dating-advice/wp-content/uploads/2018/06/datingprofile2-900x600.jpg",
                etComment.text.toString(),
                "now",0,false,false, arrayListOf())
            arrayComments.add(comment1!!)
            adapterComments.notifyDataSetChanged()
            etComment.setText("")
            // Toast.makeText(activity,"aaaaaa",Toast.LENGTH_LONG).show()
        }
    }

/*
    private fun setComments(){
        arrayComments.clear()
        arrayComments= MyApplication.arrayComments
        try{ adapterComments= AdapterComments(arrayComments!!,this)
            val glm = GridLayoutManager(activity, 1)
            rvComments.adapter=adapterComments
            rvComments.layoutManager=glm}catch (e:Exception){}
    }
*/



    override fun onItemClicked(view: View, position: Int) {

    }

}