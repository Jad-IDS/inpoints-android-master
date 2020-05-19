package com.ids.inpoint.controller.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper

class AdapterTeamStartup(
    val teamStartups: ArrayList<TeamStartup>,
    val itemClickListener: RVOnItemClickListener,
    val showActionButton: Boolean = false,
    val type:Int
) :
    RecyclerView.Adapter<AdapterTeamStartup.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.ids.inpoint.R.layout.item_team,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            AppHelper.setRoundImageResize(
                holder.itemView.context,
                holder.ivTeam,
                AppConstants.IMAGES_URL + teamStartups[position].image!!,
                false
            )
        } catch (E: java.lang.Exception) {
        }
        try {
            holder.tvTeamName.text = teamStartups[position].userName
        } catch (e: Exception) {
        }

        try {
          //  if (showActionButton) {
                holder.btItemTeamAction.visibility = View.VISIBLE

                when {
                    teamStartups[position].canJoin == AppConstants.TEAM_TYPE_PENDING ->{
                        holder.tvItemTeamAction.text = holder.itemView.context.getString(R.string.pending)
                        holder.ivItemTeamAction.setImageResource(R.drawable.ic_team)}
                    teamStartups[position].canJoin == AppConstants.TEAM_TYPE_JOINED -> {
                        if(teamStartups[position].admin!!) {
                            holder.tvItemTeamAction.text = holder.itemView.context.getString(R.string.view_requests)
                            holder.ivItemTeamAction.setImageResource(R.drawable.icon_request)
                        }
                        else {
                           if(type==AppConstants.ADAPTER_TEAM)
                               holder.tvItemTeamAction.text = holder.itemView.context.getString(R.string.view_team)
                            else
                               holder.tvItemTeamAction.text = holder.itemView.context.getString(R.string.view_startup)

                            holder.ivItemTeamAction.setImageResource(R.drawable.ic_team)
                        }
                       }
                    else ->{
                        if(type==AppConstants.ADAPTER_TEAM) {
                            holder.tvItemTeamAction.text =holder.itemView.context.getString(R.string.join_team)
                            holder.ivItemTeamAction.setImageResource(R.drawable.ic_team)
                        }
                        else {
                            holder.tvItemTeamAction.text = holder.itemView.context.getString(R.string.view_startup)
                            holder.ivItemTeamAction.setImageResource(R.drawable.visibility_on_white)
                        }
                    }


                }
        //    }
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return teamStartups.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var ivTeam: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivTeam) as ImageView
        var tvTeamName: CustomTextViewBold =
            itemView.findViewById(com.ids.inpoint.R.id.tvTeamName) as CustomTextViewBold
        var btItemTeamAction: LinearLayout =
            itemView.findViewById(com.ids.inpoint.R.id.btItemTeamAction) as LinearLayout
        var ivItemTeamAction: ImageView =
            itemView.findViewById(com.ids.inpoint.R.id.ivItemTeamAction) as ImageView
        var tvItemTeamAction: CustomTextViewMedium =
            itemView.findViewById(com.ids.inpoint.R.id.tvItemTeamAction) as CustomTextViewMedium

        init {
            itemView.setOnClickListener(this)
            btItemTeamAction.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}