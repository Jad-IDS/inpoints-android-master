package com.ids.inpoint.controller.Adapters

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


class AdapterSpinner(val itemSpinner: ArrayList<ItemSpinner>, private val itemClickListener: RVOnItemClickListener,from:Int) :
    RecyclerView.Adapter<AdapterSpinner.VHprivacy>() {

    var type=from
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_privacy, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvPrivacy.text=itemSpinner[position].name

    }

    override fun getItemCount(): Int {
        return itemSpinner.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvPrivacy: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.tvPrivacy) as CustomTextViewMedium

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(type==AppConstants.SPINNER_EVENT_SORT)
                itemView.id= R.id.IdSpinnerSort
            else if(type==AppConstants.SPINNER_EVENT_SELECT)
                itemView.id=R.id.IdSpinnerSelect
            else if(type==AppConstants.SPINNER_POST_CITIES)
                itemView.id=R.id.IdSpinnerCities

            else if(type==AppConstants.SPINNER_NATIONALITIES)
                itemView.id=R.id.IdSpinnerNationalities
            else if(type==AppConstants.SPINNER_LOCATIONS)
                itemView.id=R.id.IdSpinnerLocations
            else if(type==AppConstants.SPINNER_POST_GENDERS)
                itemView.id=R.id.IdSpinnerGender
            else if(type==AppConstants.SPINNER_POST_PRIVACY)
                itemView.id=R.id.IdSpinnerPrivacy
            else if(type==AppConstants.SPINNER_POST_USERS)
                itemView.id=R.id.IdSpinnerUsers
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
