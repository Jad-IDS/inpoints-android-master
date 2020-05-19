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
import com.ids.inpoint.controller.Adapters.AdapterComments
import com.ids.inpoint.controller.Adapters.AdapterNewsFeed

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.controller.MyApplication.Companion.arrayComments
import com.ids.inpoint.model.comments
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClient
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_comment_bottomsheet.*
import kotlinx.android.synthetic.main.fragment_fragment_news_feed.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class FragmentCommentBottomSheet : BottomSheetDialogFragment(), RVOnItemClickListener {



    val CREDIT = 1
    val CASH = 2
    var OrderId=0
    var paymentMethod = CASH
    lateinit var adapterComments: AdapterComments
    var arrayComments= java.util.ArrayList<comments>()
    var comment1:comments?=null

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
        inflater.inflate(com.ids.inpoint.R.layout.fragment_comment_bottomsheet, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        //setComments()
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
        arrayComments=MyApplication.arrayComments
       try{ adapterComments= AdapterComments(arrayComments!!,this)
        val glm = GridLayoutManager(activity, 1)
        rvComments.adapter=adapterComments
        rvComments.layoutManager=glm}catch (e:Exception){}
    }
*/



    override fun onItemClicked(view: View, position: Int) {

    }

}