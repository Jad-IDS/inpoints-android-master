package com.ids.inpoint.controller.Adapters



import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.events
import com.ids.inpoint.model.response.ResponseComments
import com.ids.inpoint.model.response.ResponseSubCommrent
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import org.greenrobot.eventbus.EventBus
import java.lang.Exception


import java.util.*


class AdapterComments(val comments: ArrayList<ResponseComments>, private val itemClickListener: RVOnItemClickListener,private val PostPosition:Int,private val details:Boolean) :
    RecyclerView.Adapter<AdapterComments.VHcomments>() ,RVOnSubItemClickListener{
    override fun onSubItemClicked(view: View, position: Int, parentPosition: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcomments {
        return VHcomments(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: VHcomments, position: Int) {
        AppHelper.setRoundImage(holder.itemView.context,holder.ivUserComment, AppConstants.IMAGES_URL+comments[position].image!!,false)
        try{holder.ctvCommentUserName.text=comments[position].userName}catch (e:Exception){}
        try{holder.ctvComment.text=comments[position].comment}catch (e:Exception){}
        try{holder.ctvCommentTime.text=comments[position].time}catch (e:Exception){}


       if(comments[position].userId==MyApplication.userLoginInfo.id)
           holder.ctvdelete.visibility=View.VISIBLE
        else
           holder.ctvdelete.visibility=View.GONE


        if(comments[position].replies!=null && comments[position].replies!!.size>0){

            try {

                holder.rvSubComments.visibility = View.VISIBLE

                if (!details) {
                    if (comments[position].replies!!.size > 2) {
                        if (comments[position].replies!!.size == 3)
                            holder.tvReplyCount.text =
                                (comments[position].replies!!.size - 2).toString() + " " + holder.itemView.context.getString(
                                    R.string.reply
                                )
                        else
                            holder.tvReplyCount.text =
                                (comments[position].replies!!.size - 2).toString() + " " + holder.itemView.context.getString(
                                    R.string.replies
                                )


                        holder.tvReplyCount.visibility = View.VISIBLE


                    } else
                        holder.tvReplyCount.visibility = View.GONE

                }

                var arraySubComments: ArrayList<ResponseSubCommrent>? = null
                arraySubComments = arrayListOf()
                if (comments[position].showSubComments == null)
                    comments[position].showSubComments = false


                if (!details) {
                    if (comments[position].replies!!.size > 0) {
                        Log.wtf("reply_size", comments[position].replies!![0].ReplyId.toString())
                        holder.rvSubComments.visibility = View.VISIBLE
                        if (comments[position].showSubComments) {
                            arraySubComments = comments[position].replies
                        } else {
                            if (comments[position].replies!!.size > 2) {
                                for (i in 0..1) {
                                    arraySubComments.add(comments[position].replies!![i])
                                }
                            } else {
                                arraySubComments = comments[position].replies
                            }
                        }
                    } else {
                        holder.rvSubComments.visibility = View.GONE
                        holder.tvReplyCount.visibility=View.GONE
                    }
                } else
                    arraySubComments = comments[position].replies

                if (comments[position].replies!!.size != 0) {
                    holder.rvSubComments.visibility=View.VISIBLE
                    val adapterSubComments = AdapterSubComments(arraySubComments!!, this, position, PostPosition)
                    val glm = GridLayoutManager(holder.itemView.context, 1)
                    holder.rvSubComments.adapter = adapterSubComments
                    holder.rvSubComments.layoutManager = glm
                }else{
                    holder.rvSubComments.visibility=View.GONE
                    holder.tvReplyCount.visibility=View.GONE
                }
            }
            catch (e:Exception){
                holder.rvSubComments.visibility=View.GONE
                holder.tvReplyCount.visibility=View.GONE
            }



        }else {
            holder.rvSubComments.visibility = View.GONE
            holder.tvReplyCount.visibility=View.GONE
        }


        holder.ctvReply.setOnClickListener{
            EventBus.getDefault().post(events(AppConstants.REPLY_COMMENT,PostPosition,position,-1,false))

        }


        holder.ctvdelete.setOnClickListener{
            EventBus.getDefault().post(events(AppConstants.DELETE_COMMENT,PostPosition,position,-1,false))

        }

       holder.tvReplyCount.setOnClickListener{
          if(details) {
              comments[position].showSubComments = true
              notifyDataSetChanged()
          }

           EventBus.getDefault().post(events(AppConstants.MORE_REPLY_COMMENT,PostPosition,position,-1,false))


       }

        if(comments[position].showSubComments)
            holder.tvReplyCount.visibility=View.GONE
        else
            holder.tvReplyCount.visibility=View.VISIBLE

        if(comments[position].replies!!.size==0)
            holder.rvSubComments.visibility=View.GONE
        else {
            try {
                if (comments[position].replies!![0].ReplyId != null)
                    holder.rvSubComments.visibility = View.VISIBLE
                else
                    holder.rvSubComments.visibility = View.GONE
            }catch (e:Exception){
                holder.rvSubComments.visibility = View.GONE
            }
        }

    }





    override fun getItemCount(): Int {
        return comments.size
    }

    inner class VHcomments(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivUserComment: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUserComment) as ImageView
        var ctvCommentUserName: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentUserName) as CustomTextViewBold
        var ctvComment: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvComment) as CustomTextViewMedium
        var ctvCommentTime: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentTime) as CustomTextViewMedium
        var ctvdelete: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvDelete) as CustomTextViewMedium
        var ctvReply: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvReply) as CustomTextViewMedium
        var rvSubComments: RecyclerView = itemView.findViewById(com.ids.inpoint.R.id.rvSubComments) as RecyclerView
        var tvReplyCount:TextView = itemView.findViewById(com.ids.inpoint.R.id.tvReplyCount) as TextView

        init {
            itemView.setOnClickListener(this)
            tvReplyCount.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
