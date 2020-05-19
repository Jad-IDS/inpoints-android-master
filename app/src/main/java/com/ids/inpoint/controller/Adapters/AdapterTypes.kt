package com.ids.inpoint.controller.Adapters

import android.support.v7.widget.CardView
import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ResponsePostType
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterTypes(val itemSpinner: ArrayList<ResponsePostType>, private val itemClickListener: RVOnItemClickListener,from:Int) :
    RecyclerView.Adapter<AdapterTypes.VHprivacy>() {

    var type=from
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_category_verify, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {

        try{holder.tvCategoryTitle.text=itemSpinner[position].valueEn}catch (e:Exception){}


        holder.itemView.setOnClickListener{
            itemSpinner[position].isSelected=!itemSpinner[position].isSelected!!
            notifyItemChanged(position)
        }

          holder.cvImage.visibility=View.GONE

        if(itemSpinner[position].isSelected!!){
            holder.ivVerified.visibility = View.VISIBLE
        }else{
            holder.ivVerified.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return itemSpinner.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivCategoryImage: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivCategoryImage) as ImageView
        var cvImage: CardView = itemView.findViewById(com.ids.inpoint.R.id.cvImage) as CardView
        var ivVerified: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivVerified) as ImageView
        var tvCategoryTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvCategoryTitle) as TextView


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(type==AppConstants.SPINNER_POST_PRIVACY)
                itemView.id= R.id.IdSpinnerTypes

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
