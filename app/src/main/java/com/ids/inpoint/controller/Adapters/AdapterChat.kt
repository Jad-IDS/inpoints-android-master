package com.ids.inpoint.controller.Adapters

import android.widget.ImageView
import android.widget.TextView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.util.*


class AdapterChat(val arrayChats: ArrayList<ResponseChat>, private val itemClickListener: RVOnItemClickListener,private val type:Int) :
    RecyclerView.Adapter<AdapterChat.VHprivacy>() {
    var userType=type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_chat, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {


            holder.tvDate.text=arrayChats[position].creationDate
            holder.tvMessage.text=arrayChats[position].content
            holder.tvUsername.text=arrayChats[position].senderName
            try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+arrayChats[position].senderImage!!,false)}catch (E: java.lang.Exception){}


        if(arrayChats[position].senderId==MyApplication.userLoginInfo.id) {
            holder.itemView.layoutDirection=View.LAYOUT_DIRECTION_RTL
            holder.tvMessage.setBackgroundResource(R.drawable.rectangular_primary)
        }else{
            holder.itemView.layoutDirection=View.LAYOUT_DIRECTION_LTR
            holder.tvMessage.setBackgroundResource(R.drawable.rectangular_secondary)
        }


    }

    override fun getItemCount(): Int {
        return arrayChats.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvDate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDate) as TextView
        var tvMessage: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvMessage) as TextView
        var tvUsername: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvUsername) as TextView
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView

        init {
            ivUser.setOnClickListener(this)
            tvUsername.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
