package com.ids.inpoint.controller.Adapters





import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium

import com.ids.inpoint.model.News_feed
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterEvents(val newsFeeds: ArrayList<News_feed>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterEvents.VHnewsFeeds>(),RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHnewsFeeds {
        return VHnewsFeeds(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_events, parent, false))
    }

    override fun onBindViewHolder(holder: VHnewsFeeds, position: Int) {
        try{AppHelper.setRoundImage(holder.itemView.context,holder.ivUser, newsFeeds[position].userImage!!,false)}catch (e:Exception){}

        try{
            if(newsFeeds[position].imagePostUrl.toString().isNotEmpty()) {
                holder.ivImagePost.visibility=View.VISIBLE
                AppHelper.setImage(holder.itemView.context, holder.ivImagePost, newsFeeds[position].imagePostUrl!!)
            }else{
                holder.ivImagePost.visibility=View.GONE
            }
        }catch (e:Exception){
            holder.ivImagePost.visibility=View.GONE
        }

        if(newsFeeds[position].enableSettings!!)
            holder.linearDots.visibility=View.VISIBLE
        else
            holder.linearDots.visibility=View.GONE


        if(newsFeeds[position].enableSettings!!)
            holder.linearDots.visibility=View.VISIBLE
        else
            holder.linearDots.visibility=View.GONE


        try{ holder.ctvName.text = newsFeeds[position].name }catch (e:Exception){}
        try{ holder.ctvLevel.text = newsFeeds[position].level }catch (e:Exception){}
        try{ holder.ctvTextPost.text = newsFeeds[position].text_post }catch (e:Exception){}
        try{ holder.ctvUni.text = newsFeeds[position].university }catch (e:Exception){}
        try{ holder.ctvTimePost.text = newsFeeds[position].name }catch (e:Exception){}
        try{ holder.ctvTimePost.text = newsFeeds[position].postTime }catch (e:Exception){}
        try{ holder.ctvLikesCount.text = newsFeeds[position].likesCount.toString() }catch (e:Exception){}
        try{ holder.ctvCommentsCount.text = newsFeeds[position].commentsCount.toString()+" "+holder.itemView.context.getString(R.string.comments) }catch (e:Exception){}


        if(newsFeeds[position].isLiked!!)
            holder.ivLike.setImageResource(R.drawable.like_on)
        else
            holder.ivLike.setImageResource(R.drawable.like)

        if(newsFeeds[position].showComments!!) {
            holder.linearWriteComment.visibility=View.VISIBLE
            holder.rvComments.visibility=View.VISIBLE
        }else{
            holder.linearWriteComment.visibility=View.GONE
            holder.rvComments.visibility=View.GONE
        }


        if(newsFeeds[position].enableSettings!!){
            holder.linearDots.visibility=View.VISIBLE
            try{
                holder.dot1.background.setColorFilter(Color.parseColor(newsFeeds[position].colorSettings), PorterDuff.Mode.SRC_ATOP)
                holder.dot2.background.setColorFilter(Color.parseColor(newsFeeds[position].colorSettings), PorterDuff.Mode.SRC_ATOP)
                holder.dot3.background.setColorFilter(Color.parseColor(newsFeeds[position].colorSettings), PorterDuff.Mode.SRC_ATOP)}
            catch (e:Exception){}
        }else{
            holder.linearDots.visibility=View.GONE
        }

        try{


            if(newsFeeds[position].enableCorner!!){
                holder.linearCorner.visibility=View.VISIBLE
                try{ holder.corner1.setBackgroundColor(Color.parseColor(newsFeeds[position].colorSettings))}catch (e:Exception){}
                try{ holder.corner2.setBackgroundColor(Color.parseColor(newsFeeds[position].colorSettings))}catch (e:Exception){}
            }else{
                holder.linearCorner.visibility=View.INVISIBLE
            }}catch (e:Exception){holder.linearCorner.visibility=View.INVISIBLE}




/*        if(newsFeeds[position].arrayComments!=null){
            try{
                var adapterComments= AdapterComments(newsFeeds[position].arrayComments!!,this)
                val glm = GridLayoutManager(holder.itemView.context, 1)
                holder.rvComments.adapter=adapterComments
                holder.rvComments.layoutManager=glm}catch (e:Exception){}
        }*/


        if(newsFeeds[position].settingsViewVisible!!)
            holder.linearPostSettings.visibility=View.VISIBLE
        else
            holder.linearPostSettings.visibility=View.GONE


        holder.linearDots.setOnClickListener{
            if(!newsFeeds[position].settingsViewVisible!!){
                holder.linearPostSettings.visibility=View.VISIBLE
                newsFeeds[position].settingsViewVisible=true
            }else{
                holder.linearPostSettings.visibility=View.GONE
                newsFeeds[position].settingsViewVisible=false
            }

        }


        holder.linearCorner.setOnClickListener{
            if(!newsFeeds[position].settingsViewVisible!!){
                holder.linearPostSettings.visibility=View.VISIBLE
                newsFeeds[position].settingsViewVisible=true
            }else{
                holder.linearPostSettings.visibility=View.GONE
                newsFeeds[position].settingsViewVisible=false
            }

        }


        holder.ivSend.setOnClickListener(View.OnClickListener { v ->
            if(holder.etComment.text.toString().isNotEmpty()){
                itemClickListener.onItemClicked(v, position)
            } })




        if(newsFeeds[position].arrayResources!=null){
     /*       try{
                var adapterResources= AdapterResources(newsFeeds[position].arrayResources!!,this)
                val glm = GridLayoutManager(holder.itemView.context, 1)
                holder.rvResources.adapter=adapterResources
                holder.rvResources.layoutManager=glm
                holder.rvResources.visibility=View.VISIBLE
                holder.tvResourcesTitle.visibility=View.VISIBLE

            }catch (e:Exception){
                holder.rvResources.visibility=View.GONE
                holder.tvResourcesTitle.visibility=View.GONE
            }*/
        }else{
            holder.rvResources.visibility=View.GONE
            holder.tvResourcesTitle.visibility=View.GONE
        }


        try{ holder.tvEventTitle.text=newsFeeds[position].eventTitle }catch (e:Exception){}
        try{ holder.tvEventTime.text="Duration: "+newsFeeds[position].eventDuration }catch (e:Exception){}
        try{ holder.tvEventDate.text=newsFeeds[position].eventDate }catch (e:Exception){}
        try{ holder.tvEventLocation.text=newsFeeds[position].eventLocation }catch (e:Exception){}

        if(newsFeeds[position].isShowMore!!){
            holder.linearShowedMoreData.visibility=View.VISIBLE
            holder.tvShowMoreLess.text="Show less"
        }else{
            holder.linearShowedMoreData.visibility=View.GONE
            holder.tvShowMoreLess.text="Show more"
        }

    }

    override fun getItemCount(): Int {
        return newsFeeds.size
    }

    inner class VHnewsFeeds(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //related to item_news_feed
        var linearCorner: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearCorner) as LinearLayout
        var corner1: View = itemView.findViewById(com.ids.inpoint.R.id.corner1) as View
        var corner2: View = itemView.findViewById(com.ids.inpoint.R.id.corner2) as View
        var dot1: View = itemView.findViewById(com.ids.inpoint.R.id.dot1) as View
        var dot2: View = itemView.findViewById(com.ids.inpoint.R.id.dot2) as View
        var dot3: View = itemView.findViewById(com.ids.inpoint.R.id.dot3) as View
        var linearDots: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearDots) as LinearLayout
        var linearPostSettings: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearPostSettings) as LinearLayout


        //related to item_news_feed_info
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var ctvName: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvName) as CustomTextViewBold
        var ctvLevel: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvLevel) as CustomTextViewBold
        var ctvUni: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvUni) as CustomTextViewMedium
        var ctvTimePost: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvTimePost) as CustomTextViewMedium

        //related to item_news_feed_body
        var ivImagePost: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePostEvents) as ImageView
        var ctvTextPost: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvTextPostEvents) as CustomTextViewMedium
        var tvEventTitle: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventTitle) as CustomTextViewBold

        var linearShowedMoreData: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearShowMoreData) as LinearLayout
        var linearShow: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearShow) as LinearLayout
        var tvResourcesTitle: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvResourcesTitle) as CustomTextViewBold
        var rvResources: RecyclerView = itemView.findViewById(com.ids.inpoint.R.id.rvResources) as RecyclerView
        var tvEventDate: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventDate) as CustomTextViewBold
        var tvEventTime: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventTime) as CustomTextViewBold
        var tvEventLocation: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventLocation) as CustomTextViewBold
        var linearParticipate: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearParticipate) as LinearLayout
        var tvShowMoreLess: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.tvShowMoreLess) as CustomTextViewMedium


        //related to item_news_feed_comment
        var ctvLikesCount: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvLikesCount) as CustomTextViewMedium
        var ctvCommentsCount: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentsCount) as CustomTextViewMedium
        var ivLike: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivLike) as ImageView
        var ivComment: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivComment) as ImageView
        var ivVerified: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivVerified) as ImageView
        var ivShare: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivShare) as ImageView
        var linearLike: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearLike) as LinearLayout
        var linearComment: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearComment) as LinearLayout
        var LinearVerified: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.LinearVerified) as LinearLayout
        var linearShare: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearShare) as LinearLayout
        var rvComments: RecyclerView = itemView.findViewById(com.ids.inpoint.R.id.rvComments) as RecyclerView
        var linearWriteComment: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearWriteComment) as LinearLayout
        var ivPhoto: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivPhoto) as ImageView
        var etComment: EditText = itemView.findViewById(com.ids.inpoint.R.id.etComment) as EditText
        var ivSend: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivSend) as ImageView




        init {
            itemView.setOnClickListener(this)
            ivImagePost.setOnClickListener(this)
            linearLike.setOnClickListener(this)
            linearComment.setOnClickListener(this)
            linearShare.setOnClickListener(this)
            linearParticipate.setOnClickListener(this)
            linearShow.setOnClickListener(this)

            //linearDots.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
