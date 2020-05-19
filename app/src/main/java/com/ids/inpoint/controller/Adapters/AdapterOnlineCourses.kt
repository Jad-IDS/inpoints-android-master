package com.ids.inpoint.controller.Adapters



import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ResponseCourse
import com.ids.inpoint.model.response.ResponseOnlineCourses

import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterOnlineCourses(val courses: ArrayList<ResponseOnlineCourses>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterOnlineCourses.VHprivacy>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_elearning, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {

        try{holder.tvElearningTitle.text=courses[position].name}catch (e:Exception){}
        try{holder.tvElearningBy.text=courses[position].instructorName}catch (e:Exception){}
        try{holder.tvElearningInfo.text=courses[position].categoryName}catch (e:Exception){}
        try{AppHelper.setImageResize(holder.itemView.context,holder.ivElearning,AppConstants.IMAGES_URL+courses[position].fileName,300,200)}catch (e:java.lang.Exception){}


    }

    override fun getItemCount(): Int {
        return courses.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivElearning: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivElearning) as ImageView
        var tvElearningBy: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvElearningBy) as TextView
        var tvElearningInfo: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvElearningInfo) as TextView
        var tvElearningTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvElearningTitle) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
