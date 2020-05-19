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
import com.ids.inpoint.controller.Fragments.ResponseTeamRequest
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterTeamRequests(val arrayUsers: ArrayList<ResponseTeamRequest>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterTeamRequests.VHprivacy>() {

    var userType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_team_request, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        try{holder.tvUsername.text=arrayUsers[position].userName}catch (e:Exception){}
        try{AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+arrayUsers[position].image!!,false)}catch (E: java.lang.Exception){}
        try{holder.tvTeamName.text=arrayUsers[position].teamName}catch (e:Exception){}
    }

    override fun getItemCount(): Int {
        return arrayUsers.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var tvUsername: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvUsername) as TextView
        var tvTeamName: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvTeamName) as TextView
        var btAcceptRequest: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btAcceptRequest) as ImageView
        var btCancelRequest: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btCancelRequest) as ImageView
        var linearViewUser: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearViewUser) as LinearLayout


        init {

            btAcceptRequest.setOnClickListener(this)
            btCancelRequest.setOnClickListener(this)
            linearViewUser.setOnClickListener(this)
        }

        override fun onClick(v: View) {


            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
