package com.ids.inpoint.controller.Adapters




import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.controller.MyApplication


import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ResponsePoint
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception

import java.util.*


class AdapterPopupPoints(val arrayPoints: ArrayList<ResponsePoint>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterPopupPoints.VHprivacy>() {

    var userType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_popup_points, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        try{ holder.tvDescription.text=arrayPoints[position].descriptionEn}catch (e:Exception){}
        try{ holder.tvPointsValue.text=arrayPoints[position].points.toString()}catch (e:Exception){}

    }

    override fun getItemCount(): Int {
        return arrayPoints.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvDescription: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDescription) as TextView
        var tvPointsValue: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvPointsValue) as TextView

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
