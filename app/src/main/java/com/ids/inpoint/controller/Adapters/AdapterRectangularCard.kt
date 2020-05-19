package com.ids.inpoint.controller.Adapters


import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.Action
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.ItemRectangularCard
import java.util.*


class AdapterRectangularCard<T>(
    private val context: Context,
    private val items: ArrayList<ItemRectangularCard<T>>,
    private val itemClickListener: RVOnItemClickListener,
    private val icon: Int,
    private val action: Action
) :
    RecyclerView.Adapter<AdapterRectangularCard<T>.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.ids.inpoint.R.layout.item_rectangular_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTitle.text = items[position].title
        holder.tvSubTitle1.text = items[position].subtitle1
        holder.tvSubTitle2.text = items[position].subtitle2

        if (icon == 2) {
            holder.ivIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.baseline_work_24
                )
            )
        } else if (icon == 3) {
            holder.ivIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.baseline_assessment_24
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvTitle = itemView.findViewById(com.ids.inpoint.R.id.tvTitle) as CustomTextViewBold
        var tvSubTitle1 =
            itemView.findViewById(com.ids.inpoint.R.id.tvSubTitle1) as CustomTextViewMedium
        val tvSubTitle2 =
            itemView.findViewById(com.ids.inpoint.R.id.tvSubTitle2) as CustomTextViewMedium

        val ivIcon =
            itemView.findViewById(com.ids.inpoint.R.id.ivIcon) as ImageView

        val btEdit = itemView.findViewById(com.ids.inpoint.R.id.btEdit) as ImageView
        val btDelete = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView

        init {
            when (action) {
                Action.A -> {
                    btEdit.id = R.id.btEditA
                    btDelete.id = R.id.btDeleteA
                }
                Action.B -> {
                    btEdit.id = R.id.btEditB
                    btDelete.id = R.id.btDeleteB
                }
                Action.C -> {
                    btEdit.id = R.id.btEditC
                    btDelete.id = R.id.btDeleteC
                }
            }

            itemView.setOnClickListener(this)
            btEdit.setOnClickListener(this)
            btDelete.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
