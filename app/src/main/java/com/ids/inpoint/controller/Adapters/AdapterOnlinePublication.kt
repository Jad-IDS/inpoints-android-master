package com.ids.inpoint.controller.Adapters




import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.model.response.ResponseCourse
import com.ids.inpoint.model.response.ResponseOnlineCourses
import com.ids.inpoint.model.response.ResponseOnlinePublication

import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterOnlinePublication(val publication: ArrayList<ResponseOnlinePublication>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterOnlinePublication.VHprivacy>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_elearning, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {

        try{holder.tvElearningTitle.text=publication[position].name}catch (e:Exception){}
        try{holder.tvElearningBy.text=publication[position].authorName}catch (e:Exception){}
        if(publication[position].authorName!!.isEmpty())
            holder.linearBy.visibility=View.GONE
        else
            holder.linearBy.visibility=View.VISIBLE

        try{holder.tvElearningInfo.text=publication[position].categoryName}catch (e:Exception){}

       if(publication[position].fileName!=null && publication[position].fileName!=""){
        try{AppHelper.setImageResize(holder.itemView.context,holder.ivElearning,AppConstants.IMAGES_URL+publication[position].fileName,300,200)}catch (e:java.lang.Exception){}
       }else{
           holder.ivElearning.setImageResource(R.drawable.placeholder)
       }

    }

    override fun getItemCount(): Int {
        return publication.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivElearning: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivElearning) as ImageView
        var tvElearningBy: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvElearningBy) as TextView
        var tvElearningInfo: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvElearningInfo) as TextView
        var tvElearningTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvElearningTitle) as TextView
        var linearBy: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearBy) as LinearLayout

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
