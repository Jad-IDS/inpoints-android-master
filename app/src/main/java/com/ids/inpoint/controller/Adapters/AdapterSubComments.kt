package com.ids.inpoint.controller.Adapters



import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.events
import com.ids.inpoint.model.response.ResponseSubCommrent
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import org.greenrobot.eventbus.EventBus
import java.lang.Exception


import java.util.*


class AdapterSubComments(val comments: ArrayList<ResponseSubCommrent>, private val itemClickListener: RVOnSubItemClickListener,private val parentPosition:Int,private val PostPosition:Int) :
    RecyclerView.Adapter<AdapterSubComments.VHcomments>() {
  

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcomments {
        return VHcomments(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_subcomment, parent, false))
    }

    override fun onBindViewHolder(holder: VHcomments, position: Int) {
        try{AppHelper.setRoundImage(holder.itemView.context,holder.ivUserComment, AppConstants.IMAGES_URL+comments[position].ReplyImage!!,false)}catch (E:Exception){}
        try{holder.ctvCommentUserName.text=comments[position].ReplyUserName}catch (e:Exception){}
        try{holder.ctvComment.text=comments[position].Reply}catch (e:Exception){}
        try{holder.ctvCommentTime.text=comments[position].Time}catch (e:Exception){}

        try{holder.ctvDelete.setOnClickListener{
            EventBus.getDefault().post(events(AppConstants.DELETE_SUB_COMMENT,PostPosition,parentPosition,position,true))
        }}catch (e:Exception){}


        try{
            if(comments[position].liked!!){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    holder.ctvLike.setTextColor(ContextCompat.getColor(holder.itemView.context, com.ids.inpoint.R.color.primary));
                else
                    holder.ctvLike.setTextColor(holder.itemView.resources.getColor(com.ids.inpoint.R.color.primary))

            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    holder.ctvLike.setTextColor(ContextCompat.getColor(holder.itemView.context, com.ids.inpoint.R.color.gray_text));
                else
                    holder.ctvLike.setTextColor(holder.itemView.resources.getColor(com.ids.inpoint.R.color.gray_text))
            }



        }catch (e:Exception){}


        if(MyApplication.userLoginInfo.id==comments[position].ReplyUserId)
            holder.ctvDelete.visibility=View.VISIBLE
        else
            holder.ctvDelete.visibility=View.GONE

    }





    override fun getItemCount(): Int {
        return comments.size
    }

    inner class VHcomments(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUserComment: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUserComment) as ImageView
        var ctvCommentUserName: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentUserName) as CustomTextViewBold
        var ctvComment: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvComment) as CustomTextViewMedium
        var ctvCommentTime: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentTime) as CustomTextViewMedium
        var ctvLike: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvLike) as CustomTextViewMedium
        var ctvDelete: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvDelete) as CustomTextViewMedium

        init {

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onSubItemClicked(v, layoutPosition,parentPosition)
        }
    }
}
