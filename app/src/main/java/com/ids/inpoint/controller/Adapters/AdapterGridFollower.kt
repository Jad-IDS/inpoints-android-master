package com.ids.inpoint.controller.Adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.model.response.ResponseFollowers
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.util.*


class AdapterGridFollower(
    val items: ArrayList<ResponseFollowers>,
    private val itemClickListener: RVOnItemClickListener
) :
    RecyclerView.Adapter<AdapterGridFollower.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.ids.inpoint.R.layout.item_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            AppHelper.setRoundImageResize(
                holder.itemView.context,
                holder.ivImage,
                AppConstants.IMAGES_URL + "/" + items[position].image!!,
                false
            )
        } catch (E: java.lang.Exception) {
        }
        try {
            holder.tvTitle.text = items[position].userName
        } catch (e: Exception) {
        }

        holder.tvSubTitle.visibility = View.GONE
        holder.llLevel.visibility = View.GONE
        holder.llActions.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var ivImage: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImage) as ImageView
        var tvTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvTitle) as TextView
        val tvSubTitle = itemView.findViewById(com.ids.inpoint.R.id.tvSubTitle) as CustomTextViewBold
        var llActions: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.llActions) as LinearLayout
        var llLevel: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.llLevel) as LinearLayout

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
