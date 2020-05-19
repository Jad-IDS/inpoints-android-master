package com.ids.inpoint.controller.Fragments



import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout


import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.AdapterComments
import com.ids.inpoint.controller.Adapters.AdapterNewsFeed

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.controller.MyApplication.Companion.arrayComments
import com.ids.inpoint.model.comments
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClient
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_comment_bottomsheet.*
import kotlinx.android.synthetic.main.fragment_fragment_news_feed.*
import kotlinx.android.synthetic.main.fragment_image.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class FragmentImageBottomSheet : BottomSheetDialogFragment(), RVOnItemClickListener {



    val CREDIT = 1
    val CASH = 2
    var OrderId=0
    var paymentMethod = CASH
    lateinit var adapterComments: AdapterComments
    var arrayComments= java.util.ArrayList<comments>()
    var comment1:comments?=null
    var ImageUrl=""
    override fun onStart() {
        super.onStart()
        val dialog = dialog

        if (dialog != null) {
            val bottomSheet = dialog.findViewById(com.ids.inpoint.R.id.design_bottom_sheet) as FrameLayout
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val view = view
        view.let {

            it!!.post {
                val parent = view!!.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
                bottomSheetBehavior!!.setPeekHeight(view.measuredHeight)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.ids.inpoint.R.style.CustomBottomSheetDialogTheme)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_image, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

     private fun init(){
        ImageUrl=MyApplication.selectedImage
       if(MyApplication.imageType==AppConstants.BOTTOM_SHEET_IMAGE_TYPE_LINK)
          AppHelper.setImage(this.activity!!,ivFullImage,ImageUrl)
       else if(MyApplication.imageType==AppConstants.BOTTOM_SHEET_IMAGE_TYPE_MEDIA_POST){
           if(MyApplication.selectedMedia.isLocal!!) {
               if(MyApplication.selectedMedia.type==AppConstants.IMAGES_TYPE_GALLERY)
                   ivFullImage.setImageURI(MyApplication.selectedMedia.uri)
               else
                   ivFullImage.setImageBitmap(MyApplication.selectedMedia.bitmap)
           }
           else
               AppHelper.setImage(this.activity!!,ivFullImage, MyApplication.selectedMedia.fileName!!, MyApplication.selectedMedia.isLocal!!)

       }

        ivClose.setOnClickListener{

            try{dismiss()}catch (e:java.lang.Exception){}

        }
    }



    override fun onItemClicked(view: View, position: Int) {

    }

}