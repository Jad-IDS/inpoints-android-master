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
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.util.*


class AdapterGridTeamMember(
    val teamMembers: ArrayList<TeamStartupMember>,
    private val itemClickListener: RVOnItemClickListener,
    private val showActions: Boolean
) :
    RecyclerView.Adapter<AdapterGridTeamMember.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.ids.inpoint.R.layout.item_team_member,
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
                AppConstants.IMAGES_URL + "/" + teamMembers[position].image!!,
                false
            )
        } catch (E: java.lang.Exception) {
        }
        try {
            holder.tvName.text = teamMembers[position].name
        } catch (e: Exception) {
        }

        if (teamMembers[position].admin != true) {
            holder.tvAdmin.setText("")
        }

        if (showActions) {
            holder.llMemberLevel.visibility = View.GONE
        } else {
            holder.llActions.visibility = View.GONE
            holder.ctvLevel.text = teamMembers[position].level.toString()
        }
    }

    override fun getItemCount(): Int {
        return teamMembers.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var ivImage: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImage) as ImageView
        var tvName: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvName) as TextView
        var ctvLevel: CustomTextViewBold =
            itemView.findViewById(com.ids.inpoint.R.id.ctvLevel) as CustomTextViewBold
        var llActions: LinearLayout =
            itemView.findViewById(com.ids.inpoint.R.id.llActions) as LinearLayout
        var llMemberLevel: LinearLayout =
            itemView.findViewById(com.ids.inpoint.R.id.llMemberLevel) as LinearLayout

        val tvAdmin = itemView.findViewById(com.ids.inpoint.R.id.tvAdmin) as CustomTextViewBold

        val btEdit = itemView.findViewById(com.ids.inpoint.R.id.btEdit) as ImageView
        val btDelete = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView

        init {
            itemView.setOnClickListener(this)
            btEdit.setOnClickListener(this)
            btDelete.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
