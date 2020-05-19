package com.ids.inpoint.controller.Adapters


import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseUser

import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ResponseSearch
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterSearch(val arrayUsers: ArrayList<ResponseSearch>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterSearch.VHprivacy>() {

    var userType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_chat_users, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        try{holder.tvUsername.text=arrayUsers[position].username}catch (e:Exception){}
        try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+arrayUsers[position].image!!,false)}catch (E: java.lang.Exception){}
        try{
            holder.tvDesc.text=arrayUsers[position].details
            holder.tvDesc.visibility=View.VISIBLE

        }catch (e:Exception){
            holder.tvDesc.visibility=View.GONE
        }

    }

    override fun getItemCount(): Int {
        return arrayUsers.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var tvUsername: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvUsername) as TextView
        var tvDesc: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDesc) as TextView
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(userType==AppConstants.PICK_IMAGE_ORGANIZATION)
                itemView.id=R.id.linearItemUserOrganizer

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
