package com.ids.inpoint.controller.Adapters





import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication.Companion.arrayUsers
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.Students
import com.ids.inpoint.model.response.ResponseCourseRegisteredUsers
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterStudents(val Students: ArrayList<ResponseCourseRegisteredUsers>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterStudents.VHcomments>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcomments {
        return VHcomments(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_student, parent, false))
    }

    override fun onBindViewHolder(holder: VHcomments, position: Int) {
      //  AppHelper.setRoundImage(holder.itemView.context,holder.ivUser, Students[position].profileImage!!,false)
        try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+Students[position].profileImage!!,false)}catch (E: java.lang.Exception){}


        try{holder.tvStudentName.text=Students[position].userName}catch (e:Exception){}
        try{holder.tvAddress.text=Students[position].location}catch (e:Exception){}
       // try{holder.ctvLevel.text=Students[position].level}catch (e:Exception){}
    }

    override fun getItemCount(): Int {
        return Students.size
    }

    inner class VHcomments(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var tvStudentName: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvStudentName) as CustomTextViewBold
        var tvAddress: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvAddress) as CustomTextViewBold
        var ctvLevel: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvLevel) as CustomTextViewBold


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
