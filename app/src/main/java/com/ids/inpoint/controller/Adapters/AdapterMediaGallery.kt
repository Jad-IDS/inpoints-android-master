package com.ids.inpoint.controller.Adapters




import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.response.ResponseMedia
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterMediaGallery(val itemMedia: ArrayList<ResponseMedia>, private val itemClickListener: RVOnItemClickListener,from:Int) :
    RecyclerView.Adapter<AdapterMediaGallery.VHprivacy>() {

    var type=from
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_profile_images, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {

         AppHelper.setImageResize(holder.itemView.context,holder.ivProfileGallery, AppConstants.IMAGES_URL+"/"+itemMedia[position].fileName!!)

      }

    override fun getItemCount(): Int {
        return itemMedia.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivProfileGallery: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivProfileGallery) as ImageView


        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
