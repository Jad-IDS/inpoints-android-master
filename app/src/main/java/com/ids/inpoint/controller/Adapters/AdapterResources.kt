package com.ids.inpoint.controller.Adapters


import com.ids.inpoint.model.Resources


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ResponseEventFundRes

import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import kotlinx.android.synthetic.main.item_sponsors.view.*


import java.util.*


class AdapterResources(val resources: ArrayList<ResponseEventFundRes>, private val itemClickListener: RVOnItemClickListener,private val type:Int) :
    RecyclerView.Adapter<AdapterResources.VHprivacy>() {
    var userType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_sponsors, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvResource.text=resources[position].description
        if(resources[position].Sponsored==1)
            holder.tvSponsor.text=holder.itemView.context.getText(R.string.unsponsor)
        else
            holder.tvSponsor.text=holder.itemView.context.getText(R.string.sponsor)

    }

    override fun getItemCount(): Int {
        return resources.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvResource: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.tvResource) as CustomTextViewMedium
        var tvSponsor: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvSponsor) as TextView
        init {
            if(type==AppConstants.TYPE_RESOURCES) {
                itemView.tvSponsor.id = R.id.btSponsorResources
            }else
                itemView.tvSponsor.id = R.id.btSponsorFunding

            tvSponsor.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
