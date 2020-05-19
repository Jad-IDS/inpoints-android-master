package com.ids.inpoint.controller.Adapters



import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.ResponseCategory
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper


import java.util.*


class AdapterCategories(val categories: ArrayList<ResponseCategory>, private val itemClickListener: RVOnItemClickListener,val postCategories: ArrayList<ResponseCategory>) :
    RecyclerView.Adapter<AdapterCategories.VHprivacy>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_category_verify, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
  // if(MyApplication.lang=="en")
      try{holder.tvCategoryTitle.text=categories[position].valueEn}catch (e:Exception){}
/*   else
      try{holder.tvCategoryTitle.text=categories[position].valueAr}catch (e:Exception){} */

      try{AppHelper.setImageResize(holder.itemView.context,holder.ivCategoryImage,AppConstants.IMAGES_URL+categories[position].iconPath!!,200,200)}catch (e:java.lang.Exception){}

        holder.itemView.setOnClickListener{
            categories[position].isVerified=!categories[position].isVerified!!
            notifyItemChanged(position)
        }

        Log.wtf("icon_url",AppConstants.ICONS_URL+categories[position].iconPath!!)
        if(postCategories.contains(categories[position]))
             categories[position].isVerified = true

        if(categories[position].isVerified!!){
            holder.ivVerified.visibility = View.VISIBLE
        }else{
            holder.ivVerified.visibility = View.INVISIBLE
        }


    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivCategoryImage: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivCategoryImage) as ImageView
        var ivVerified: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivVerified) as ImageView
        var tvCategoryTitle: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvCategoryTitle) as TextView
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
