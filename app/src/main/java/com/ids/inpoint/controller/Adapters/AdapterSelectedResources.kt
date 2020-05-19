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
import com.ids.inpoint.model.response.Funding
import com.ids.inpoint.model.response.Resources
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterSelectedResources(val arrayResources: ArrayList<Resources>, private val itemClickListener: RVOnItemClickListener,private val type:Int) :
    RecyclerView.Adapter<AdapterSelectedResources.VHprivacy>() {
    var userType=type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_funding_resources, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvDescription.text=arrayResources[position].Description
        if(arrayResources[position].Type==AppConstants.EVENT_TYPE_COMPLETED)
           holder.tvAmount.text=holder.itemView.context.getString(R.string.type_completed)
        else
           holder.tvAmount.text=holder.itemView.context.getString(R.string.type_needed)


    }

    override fun getItemCount(): Int {
        return arrayResources.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvDescription: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDescription) as TextView
        var tvAmount: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvAmount) as TextView

        var ivRemoveFunding: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivRemoveFunding) as ImageView
        var ivEditFunding: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivEditFunding) as ImageView

        init {

                itemView.id = R.id.linearResources
                ivRemoveFunding.id=R.id.ivRemoveResources
                ivEditFunding.id=R.id.ivEditResources

            itemView.setOnClickListener(this)
            ivRemoveFunding.setOnClickListener(this)
            ivEditFunding.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
