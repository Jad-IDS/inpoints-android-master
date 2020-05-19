package com.ids.inpoint.controller.Adapters





import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RelativeLayout
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.controller.MyApplication.Companion.arrayUsers

import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.Reviews
import com.ids.inpoint.model.response.ResponseReview
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterReviews(val Reviews: ArrayList<ResponseReview>, private val itemClickListener: RVOnItemClickListener, val type:Int) :
    RecyclerView.Adapter<AdapterReviews.VHcomments>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcomments {
        return VHcomments(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_reviews, parent, false))
    }

    override fun onBindViewHolder(holder: VHcomments, position: Int) {

        if(Reviews[position].profileImage==null || Reviews[position].profileImage=="" ||Reviews[position].profileImage=="null"){
            holder.ivUserComment.setImageResource(R.drawable.avatar)
        }else{
            try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUserComment, AppConstants.IMAGES_URL+Reviews[position].profileImage!!,false)}catch (E: java.lang.Exception){}

        }



        try{holder.ctvCommentUserName.text=Reviews[position].userName}catch (e:Exception){}
        try{holder.ctvComment.text=Reviews[position].text}catch (e:Exception){}
        try{
            holder.rbCourse.rating=Reviews[position].rating!!.toFloat()
        }catch (e:Exception){}

       // if(type==AppConstants.REVIEW_TYPE_STARTUP){
            if(Reviews[position].userId==MyApplication.userLoginInfo.id){
                holder.linearReviewSettings.visibility=View.VISIBLE
            }else
                holder.linearReviewSettings.visibility=View.GONE
      //  }

    }

    override fun getItemCount(): Int {
        return Reviews.size
    }

    inner class VHcomments(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUserComment: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUserComment) as ImageView
        var ctvCommentUserName: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentUserName) as CustomTextViewBold
        var ctvComment: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvComment) as CustomTextViewMedium
        var rbCourse: RatingBar = itemView.findViewById(com.ids.inpoint.R.id.rbCourse) as RatingBar
        var linearReviewSettings: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearReviewSettings) as LinearLayout
        var btEditReview: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btEditReview) as ImageView
        var btDeleteReview: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDeleteReview) as ImageView



        init {
            if(type==AppConstants.REVIEW_TYPE_COURSE)
                linearReviewSettings.visibility=View.GONE
            else
                linearReviewSettings.visibility=View.VISIBLE

            btEditReview.setOnClickListener(this)
            btDeleteReview.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
