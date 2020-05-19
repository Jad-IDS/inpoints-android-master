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
import android.widget.LinearLayout
import android.widget.SpinnerAdapter
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterStartupUsers(val arrayUsers: ArrayList<ResponseUser>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterStartupUsers.VHprivacy>() {

    var userType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_chat_users, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvUsername.text=arrayUsers[position].name
        try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+arrayUsers[position].image!!,false)}catch (E: java.lang.Exception){}

    }

    override fun getItemCount(): Int {
        return arrayUsers.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var tvUsername: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvUsername) as TextView
        var btDelete: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView

        val btView =itemView.findViewById(com.ids.inpoint.R.id.btView) as ImageView
        val btEdit = itemView.findViewById(com.ids.inpoint.R.id.btEdit) as ImageView
        val llActions = itemView.findViewById(com.ids.inpoint.R.id.llActions) as LinearLayout



        init {
             btDelete.visibility=View.VISIBLE
             btView.visibility=View.GONE
             btEdit.visibility=View.GONE
             llActions.visibility=View.VISIBLE
             itemView.setOnClickListener(this)
             btDelete.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (userType) {
                AppConstants.STARTUP_TYPE_PARTNER -> {
                    btEdit.id=R.id.btEditA
                    btDelete.id=R.id.btDeleteA
                }
                AppConstants.STARTUP_TYPE_INVESTOR -> {
                    btEdit.id=R.id.btEditB
                    btDelete.id=R.id.btDeleteB
                }
                AppConstants.STARTUP_TYPE_SPONSOR -> {
                    btEdit.id=R.id.btEditC
                    btDelete.id=R.id.btDeleteC
                }
            }



            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
