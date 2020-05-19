package com.ids.inpoint.controller.Fragments



import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityInsideComment
import com.ids.inpoint.controller.Activities.ActivityProfile
import com.ids.inpoint.controller.Adapters.*

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.RequestReview
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_startup_profile_info.*
import kotlinx.android.synthetic.main.fragment_startup_profile_product.*
import kotlinx.android.synthetic.main.fragment_startup_reviews.*
import kotlinx.android.synthetic.main.item_reviews.*
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentStartupReviews : Fragment() ,RVOnItemClickListener {



    lateinit var adapterReviews: AdapterReviews
    var arrayReviews= java.util.ArrayList<ResponseReview>()
    private var skip=0
    private var take=100
    private var dialog: Dialog? = null
    private var reviewId=0
    private var userHasReview=false


    override fun onItemClicked(view: View, position: Int) {
         if(view.id==R.id.btEditReview){
             reviewId=adapterReviews.Reviews[position].ReviewId!!
             showReviewDialog(adapterReviews.Reviews[position].rating!!,adapterReviews.Reviews[position].text!!)
         }else if(view.id==R.id.btDeleteReview){
            deleteConfirm(activity!!,adapterReviews.Reviews[position].ReviewId!!)
         }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_startup_reviews, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getStartupReviews()

    }

    private fun init(){
        btWriteReview.visibility=View.GONE
        btWriteReview.setOnClickListener{showReviewDialog(0,"")}


    }



    private fun getStartupReviews(){
        userHasReview=false
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupReviews(MyApplication.selectedStartupTeam.id!!,take,skip)?.enqueue(object : Callback<ArrayList<ResponseReview>> {
                override fun onResponse(call: Call<ArrayList<ResponseReview>>, response: Response<ArrayList<ResponseReview>>) {
                    try{ setReviews(response.body()!!)}catch (E: java.lang.Exception){
                        btWriteReview.visibility=View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseReview>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setReviews(response: ArrayList<ResponseReview>) {

        arrayReviews.clear()
        arrayReviews.addAll(response)
        adapterReviews= AdapterReviews(arrayReviews,this,AppConstants.REVIEW_TYPE_STARTUP)
        val glm = GridLayoutManager(activity, 1)
        rvReviews!!.adapter=adapterReviews
        rvReviews!!.layoutManager=glm

        if(arrayReviews.size>0)
          tvReviewCount.text = arrayReviews.size.toString()+" "+getString(R.string.reviews)
        else
            tvReviewCount.visibility=View.GONE

        for (i in arrayReviews.indices){
            if(arrayReviews[i].userId==MyApplication.userLoginInfo.id){
                userHasReview=true
                break
            }
        }
        if(userHasReview)
            btWriteReview.visibility=View.GONE
        else
            btWriteReview.visibility=View.VISIBLE

    }





    fun showReviewDialog(rating:Int,text:String) {

        dialog = Dialog(activity!!, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_review)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        var rbCourse = dialog!!.findViewById<View>(R.id.rbCourse) as RatingBar
        var etReview = dialog!!.findViewById<View>(R.id.etReview) as EditText
        var btSave = dialog!!.findViewById<View>(R.id.btSave) as LinearLayout
        if(text.isNotEmpty())
            etReview.setText(text)
        rbCourse.rating=rating.toFloat()
        btSave.setOnClickListener{
            saveReview(rbCourse.rating.toInt(),etReview.text.toString())
        }
        var  btCancel = dialog!!.findViewById<View>(R.id.btCancel) as LinearLayout
        btCancel.setOnClickListener{dialog!!.dismiss()}
        dialog!!.show();

    }


    private fun saveReview(rating:Int,text:String){
        var senddate = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveStartupReview(
                reviewId,MyApplication.selectedStartupTeam.id!!,text,rating,senddate,false
            )?.enqueue(object : Callback<RequestReview> {
                override fun onResponse(call: Call<RequestReview>, response: Response<RequestReview>) {
                    try{
                        dialog!!.dismiss()
                        getStartupReviews()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<RequestReview>, throwable: Throwable) {

                }

            })

    }




    fun deleteConfirm(c: Activity, id: Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_confirmation))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                deleteStartupUser(id)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun deleteStartupUser(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupReview(id)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{getStartupReviews()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }

}
