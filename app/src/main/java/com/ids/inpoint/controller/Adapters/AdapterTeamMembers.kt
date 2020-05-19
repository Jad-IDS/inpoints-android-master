package com.ids.inpoint.controller.Adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterTeamMembers(val teamMembers: ArrayList<TeamStartupMember>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterTeamMembers.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_follower_images, parent, false))
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          try{AppHelper.setRoundImage(holder.itemView.context,holder.ivFollower, AppConstants.IMAGES_URL+"/"+teamMembers[position].image!!,false)}catch (e:Exception){}
          if(position!=0)
           AppHelper.setMargins(holder.itemView.context,holder.rlFollower,-10,0,0,0)


    }

    override fun getItemCount(): Int {
        return teamMembers.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivFollower: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivFollower) as ImageView
        var rlFollower: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlFollower) as RelativeLayout
        init {
             itemView.setOnClickListener(this)
         }

      override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
