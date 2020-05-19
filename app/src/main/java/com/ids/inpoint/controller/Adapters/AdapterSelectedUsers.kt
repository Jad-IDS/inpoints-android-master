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
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterSelectedUsers(val arrayUsers: ArrayList<ResponseUser>, private val itemClickListener: RVOnItemClickListener,private val type:Int) :
    RecyclerView.Adapter<AdapterSelectedUsers.VHprivacy>() {
    var userType=type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_selected_user, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvUsername.text=arrayUsers[position].userName
        if(arrayUsers[position].image==null || arrayUsers[position].image=="" ||arrayUsers[position].image=="null"){
            holder.ivUser.setImageResource(R.drawable.avatar)
        }else{
            try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+arrayUsers[position].image!!,false)}catch (E: java.lang.Exception){}

        }
    }

    override fun getItemCount(): Int {
        return arrayUsers.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var tvUsername: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvUsername) as TextView
        var ivRemoveUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivRemoveUser) as ImageView

        init {
            if(type==AppConstants.PICK_IMAGE_ORGANIZATION) {
                itemView.id = R.id.linearOrganization
                ivRemoveUser.id=R.id.ivRemoveUserOrganizer

            }
            itemView.setOnClickListener(this)
            ivRemoveUser.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
