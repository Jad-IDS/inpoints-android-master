package com.ids.inpoint.controller.Adapters


import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseFollowers

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


class AdapterPopupUsers(val arrayUsers: ArrayList<ResponseFollowers>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterPopupUsers.VHprivacy>() {

    var userType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_popup_user, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvUsername.text=arrayUsers[position].userName
        try{ AppHelper.setRoundImageResize(holder.itemView.context!!,holder.ivUser, AppConstants.IMAGES_URL+arrayUsers[position].image!!,false)}catch (E: java.lang.Exception){}
        if(arrayUsers[position].isFollowed!!)
            holder.btFollow.text=holder.itemView.context.getString(R.string.unfollow)
        else
            holder.btFollow.text=holder.itemView.context.getString(R.string.follow)

    }

    override fun getItemCount(): Int {
        return arrayUsers.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var tvUsername: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvUsername) as TextView
        var btFollow: TextView = itemView.findViewById(com.ids.inpoint.R.id.btFollow) as TextView

        init {
            btFollow.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(userType==AppConstants.TYPE_FOLLOW_FOLLOWERS)
                btFollow.id=R.id.btFollowFollower
            else
                btFollow.id=R.id.btFollowLikers

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
