package com.ids.inpoint.controller.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.model.ItemSpinnerTextImage

class AdapterMultiSelectSpinnerImageText(
    var items: ArrayList<ItemSpinnerTextImage>,
    private val itemClickListener: RVOnItemClickListener
) :
    RecyclerView.Adapter<AdapterMultiSelectSpinnerImageText.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.ids.inpoint.R.layout.item_category_verify,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.tvCategoryTitle.text = items[position].text
        } catch (e: Exception) {
        }

        holder.itemView.setOnClickListener {
            items[position].isSelected = !items[position].isSelected
            notifyItemChanged(position)
        }

        holder.cvImage.visibility = View.GONE

        if (items[position].isSelected) {
            holder.ivVerified.visibility = View.VISIBLE
        } else {
            holder.ivVerified.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var ivCategoryImage: ImageView =
            itemView.findViewById(com.ids.inpoint.R.id.ivCategoryImage) as ImageView
        var cvImage: CardView = itemView.findViewById(com.ids.inpoint.R.id.cvImage) as CardView
        var ivVerified: ImageView =
            itemView.findViewById(com.ids.inpoint.R.id.ivVerified) as ImageView
        var tvCategoryTitle: TextView =
            itemView.findViewById(com.ids.inpoint.R.id.tvCategoryTitle) as TextView


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}