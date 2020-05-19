package com.ids.inpoint.controller.Adapters


import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseSocialMedia

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


class AdapterSocialMedia(val arraySocialMedias: ArrayList<ResponseSocialMedia>, private val itemClickListener: RVOnItemClickListener,private val type:Int) :
    RecyclerView.Adapter<AdapterSocialMedia.VHprivacy>() {
    var userType=type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_social_media, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvTitle.text=arraySocialMedias[position].description
        holder.tvValue.text=arraySocialMedias[position].description
        if(type==AppConstants.TYPE_CAN_DELETE)
            holder.btDelete.visibility=View.VISIBLE
        else
            holder.btDelete.visibility=View.GONE
    }

    override fun getItemCount(): Int {
        return arraySocialMedias.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvTitle) as TextView
        var tvValue: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvValue) as TextView
        var btDelete: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView

        init {

            btDelete.setOnClickListener(this)
            itemView.setOnClickListener(this)
     
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
