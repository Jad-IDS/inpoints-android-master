package com.ids.inpoint.controller.Adapters


import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ItemSearchTab
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterTabs(val itemTabs: ArrayList<ItemSearchTab>, private val itemClickListener: RVOnItemClickListener,from:Int) :
    RecyclerView.Adapter<AdapterTabs.VHprivacy>() {

    var type=from
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_search_tab, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        holder.tvTabTitle.text=itemTabs[position].name
        if(itemTabs[position].selected!!)
            holder.tabIndicator.visibility=View.VISIBLE
        else
            holder.tabIndicator.visibility=View.INVISIBLE

    }

    override fun getItemCount(): Int {
        return itemTabs.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvTabTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvTabTitle) as TextView
        var tabIndicator: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.tabIndicator) as LinearLayout

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
