package com.ids.inpoint.controller.Adapters


import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.controller.MyApplication

import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.google.android.youtube.player.internal.i
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterNotification(val arrayNotifications: ArrayList<ResponseNotification>, private val itemClickListener: RVOnItemClickListener,private val type:Int) :
    RecyclerView.Adapter<AdapterNotification.VHprivacy>() {
    var userType=type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvNotCount.text=(position+1).toString()+" -"
        try{holder.tvNotText.text=arrayNotifications[position].text}catch (e:Exception){}
        try{holder.tvNotificationDate.text=arrayNotifications[position].TimeRange}catch (e:Exception){}
        if(arrayNotifications[position].seen!!)
            holder.ivRead.setImageResource(R.drawable.circle_gray_dark)
        else
            holder.ivRead.setImageResource(R.drawable.circle_secondary)

        holder.ivNotificationImage.setImageResource(R.drawable.buildings)
    }

    override fun getItemCount(): Int {
        return arrayNotifications.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var ivRead: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivRead) as ImageView
        var tvNotText: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvNotText) as TextView
        var tvNotificationDate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvNotificationDate) as TextView
        var tvNotCount: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvNotCount) as TextView
        var ivNotificationImage: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivNotificationImage) as ImageView


        init {
           itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
