package com.ids.inpoint.controller.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.PostVideo
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterVideos(val itemVideo: ArrayList<PostVideo>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterVideos.VHprivacy>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_video_links, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvVideoLink.text=itemVideo[position].link
        holder.tvVideoType.text=itemVideo[position].typeName
    }

    override fun getItemCount(): Int {
        return itemVideo.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvVideoType: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.tvVideoType) as CustomTextViewMedium
        var tvVideoLink: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.tvVideoLink) as CustomTextViewMedium
        var btRemoveLink: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.btRemoveLink) as LinearLayout


        init {
            itemView.setOnClickListener(this)
            btRemoveLink.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)

        }
    }
}
