package com.ids.inpoint.controller.Adapters

import android.widget.ImageView
import android.widget.TextView


import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewMedium

import com.ids.inpoint.model.response.ResponseTeamFile
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterTeamFiles(val arrayFiles: ArrayList<ResponseTeamFile>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterTeamFiles.VHprivacy>() {

    var fileType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_team_files, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        try{ holder.tvDate.text=AppHelper.formatDate(
            holder.itemView.context,
            arrayFiles[position].creationDate!!,
            "yyyy-MM-dd'T'hh:mm:ss.SSS","MM/dd/yyyy" )}catch (e:Exception){}
        try{ holder.tvDescription.text=arrayFiles[position].description}catch (e:Exception){}
        try{ holder.tvSize.text=arrayFiles[position].size}catch (e:Exception){}

        if(arrayFiles[position].userId==MyApplication.userLoginInfo.id){
            holder.btDelete.visibility=View.VISIBLE
            holder.btEdit.visibility=View.VISIBLE
        }else{
            holder.btDelete.visibility=View.GONE
            holder.btEdit.visibility=View.GONE
        }

        when {
            arrayFiles[position].icon!!.contains("video") -> holder.ivFileType.setImageResource(R.drawable.icon_file_video_1)
            arrayFiles[position].icon!!.contains("image") -> holder.ivFileType.setImageResource(R.drawable.icon_file_image_1)
            arrayFiles[position].icon!!.contains("music") -> holder.ivFileType.setImageResource(R.drawable.icon_file_audio)
            else -> // if(item.icon.contains("file"))
                holder.ivFileType.setImageResource(R.drawable.icon_file_file_1)
        }
    }

    override fun getItemCount(): Int {
        return arrayFiles.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivFileType: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivFileType) as ImageView
        var btDownload: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDownload) as ImageView
        var btEdit: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btEdit) as ImageView
        var btDelete: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView
        
        var tvDescription: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDescription) as TextView
        var tvSize: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvSize) as TextView
        var tvDate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDate) as TextView




        init {
            itemView.setOnClickListener(this)
            btDownload.setOnClickListener(this)
            btEdit.setOnClickListener(this)
            btDelete.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
