package com.ids.inpoint.controller.Adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.Followers
import com.ids.inpoint.model.response.ResponseFollowers
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper



import java.util.*


class AdapterFollowers(val followers: ArrayList<ResponseFollowers>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterFollowers.VHfollowers>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHfollowers {
        return VHfollowers(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_follower_images, parent, false))
     }

    override fun onBindViewHolder(holder: VHfollowers, position: Int) {
          AppHelper.setRoundImage(holder.itemView.context,holder.ivFollower, AppConstants.IMAGES_URL+"/"+followers[position].image!!,false)
          if(position!=0)
           AppHelper.setMargins(holder.itemView.context,holder.rlFollower,-10,0,0,0)


    }

    override fun getItemCount(): Int {
        return followers.size
    }

    inner class VHfollowers(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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
