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
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterImagesPicked(val itemMedia: ArrayList<PostMedia>, private val itemClickListener: RVOnItemClickListener,from:Int) :
    RecyclerView.Adapter<AdapterImagesPicked.VHprivacy>() {

    var type=from
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_image_picked, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {

        if(itemMedia[position].type==AppConstants.FILE_TYPE){
            holder.ivImagesPicked.setImageResource(R.drawable.icon_file_file_1)
        }
        else if(itemMedia[position].type==AppConstants.AUDIO_TYPE){
            holder.ivImagesPicked.setImageResource(R.drawable.icon_file_audio)
        }
        else if(itemMedia[position].type==AppConstants.VIDEO_TYPE){
            holder.ivImagesPicked.setImageResource(R.drawable.icon_file_video_1)
        }
        else{
        if(itemMedia[position].isLocal!!) {
          if(itemMedia[position].type==AppConstants.IMAGES_TYPE_GALLERY)
              holder.ivImagesPicked.setImageURI(itemMedia[position].uri)
           else
              AppHelper.setImage(holder.itemView.context!!, holder.ivImagesPicked, itemMedia[position].fileName!!, true)
             // holder.ivImagesPicked.setImageBitmap(itemMedia[position].bitmap)
        }
        else
           AppHelper.setImageResize(holder.itemView.context,holder.ivImagesPicked, AppConstants.IMAGES_URL+"/"+ itemMedia[position].fileName!!)

       }



        // AppHelper.setImage(holder.itemView.context,holder.ivImagesPicked, itemMedia[position].fileName!!, itemMedia[position].isLocal!!)

     }

    override fun getItemCount(): Int {
        return itemMedia.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivImagesPicked: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivPickedImage) as ImageView
        var btRemove: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.btRemove) as LinearLayout

        init {
            itemView.setOnClickListener(this)
            btRemove.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
