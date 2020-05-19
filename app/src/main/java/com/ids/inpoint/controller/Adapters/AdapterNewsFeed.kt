package com.ids.inpoint.controller.Adapters


import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewBold
import com.ids.inpoint.custom.CustomTextViewMedium


import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper

import java.lang.Exception


import java.util.*

import com.ids.inpoint.controller.MyApplication.Companion.videoId
import android.R.id
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.ids.inpoint.R
import kotlinx.android.synthetic.main.footer.*


class AdapterNewsFeed(val newsFeeds: ArrayList<ResponsePost>, private val itemClickListener: RVOnItemClickListener,private val itemSubClick:RVOnSubItemClickListener,private val fragmentManager:FragmentManager) :
    RecyclerView.Adapter<AdapterNewsFeed.VHnewsFeeds>(),RVOnItemClickListener {




    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHnewsFeeds {
        return VHnewsFeeds(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_news_feed, parent, false))
    }

    override fun onBindViewHolder(holder: VHnewsFeeds, position: Int) {
      try{
          AppHelper.setRoundImageResize(holder.itemView.context,holder.ivUser, AppConstants.IMAGES_URL+newsFeeds[position].image!!,false)}catch (e:Exception){
          holder.ivUser.setImageResource(R.drawable.avatar)
      }

        if(newsFeeds[position].divType==AppConstants.TYPE_POST) {
            holder.linearNewsFeedBody.visibility=View.VISIBLE
            holder.linearEventsBody.visibility=View.GONE
            holder.linearShow.visibility=View.GONE
            Log.wtf("news_feed_media_size","position_"+position+"----"+newsFeeds[position].medias!!.size)
            try {
                when {
                    newsFeeds[position].medias!!.size >= 4 -> {
                        holder.linearImages.visibility = View.VISIBLE
                        holder.linearImageTop.visibility = View.VISIBLE
                        holder.linearImageBottom.visibility = View.VISIBLE
                        holder.rlImagePost.visibility = View.VISIBLE
                        holder.rlImagePost2.visibility = View.VISIBLE
                        holder.rlImagePost3.visibility = View.VISIBLE
                        holder.rlImagePost4.visibility = View.VISIBLE
                       if(newsFeeds[position].medias!!.size > 4) {
                           holder.tvMoreMedia.visibility = View.VISIBLE
                           holder.tvMoreMedia.text = "+" + (newsFeeds[position].medias!!.size-4)
                       }else{
                           holder.tvMoreMedia.visibility = View.GONE
                       }
                        if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost.visibility=View.VISIBLE
                            holder.youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost, AppConstants.IMAGES_URL + newsFeeds[position].medias!![0].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                            holder.ivImagePost.visibility=View.GONE
                            holder.youtube_layout.visibility=View.VISIBLE
                            setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout,newsFeeds[position].medias!![0].fileName!!,holder.youtube_layout)
                           }


                        if(newsFeeds[position].medias!![1].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost2.visibility=View.VISIBLE
                            holder.youtube_layout2.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost2, AppConstants.IMAGES_URL + newsFeeds[position].medias!![1].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![1].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost2.visibility=View.GONE
                                holder.youtube_layout2.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout2,newsFeeds[position].medias!![1].fileName!!,holder.youtube_layout2)
                         }


                        if(newsFeeds[position].medias!![2].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost3.visibility=View.VISIBLE
                            holder.youtube_layout3.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost3, AppConstants.IMAGES_URL + newsFeeds[position].medias!![2].fileName!!)
                        }else if(newsFeeds[position].medias!![2].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost3.visibility=View.GONE
                                holder.youtube_layout3.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout3,newsFeeds[position].medias!![2].fileName!!,holder.youtube_layout3)
                         }

                        if(newsFeeds[position].medias!![3].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost4.visibility=View.VISIBLE
                            holder.youtube_layout4.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost4, AppConstants.IMAGES_URL + newsFeeds[position].medias!![3].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![3].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost4.visibility=View.GONE
                                holder.youtube_layout4.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout4,newsFeeds[position].medias!![3].fileName!!,holder.youtube_layout4)
                            }
                    }
                    newsFeeds[position].medias!!.size == 3 -> {
                        holder.linearImages.visibility = View.VISIBLE
                        holder.linearImageTop.visibility = View.VISIBLE
                        holder.linearImageBottom.visibility = View.VISIBLE
                        holder.rlImagePost.visibility = View.VISIBLE
                        holder.rlImagePost2.visibility = View.VISIBLE
                        holder.rlImagePost3.visibility = View.VISIBLE
                        holder.rlImagePost4.visibility = View.GONE
                        holder.tvMoreMedia.visibility = View.GONE
                       // holder.ivImagePost4.visibility = View.GONE

                        if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost.visibility=View.VISIBLE
                            holder.youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost, AppConstants.IMAGES_URL + newsFeeds[position].medias!![0].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost.visibility=View.GONE
                                holder.youtube_layout.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout,newsFeeds[position].medias!![0].fileName!!,holder.youtube_layout)
                            }


                        if(newsFeeds[position].medias!![1].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost2.visibility=View.VISIBLE
                            holder.youtube_layout2.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost2, AppConstants.IMAGES_URL + newsFeeds[position].medias!![1].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![1].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost2.visibility=View.GONE
                                holder.youtube_layout2.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout2,newsFeeds[position].medias!![1].fileName!!,holder.youtube_layout2)
                            }


                        if(newsFeeds[position].medias!![2].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost3.visibility=View.VISIBLE
                            holder.youtube_layout3.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost3, AppConstants.IMAGES_URL + newsFeeds[position].medias!![2].fileName!!)
                        }else if(newsFeeds[position].medias!![2].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                            holder.ivImagePost3.visibility=View.GONE
                            holder.youtube_layout3.visibility=View.VISIBLE
                            setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout3,newsFeeds[position].medias!![2].fileName!!,holder.youtube_layout3)
                        }
                    }
                    newsFeeds[position].medias!!.size == 2 -> {
                        holder.linearImages.visibility = View.VISIBLE
                        holder.linearImageTop.visibility = View.VISIBLE
                        holder.linearImageBottom.visibility = View.GONE
                        holder.rlImagePost.visibility = View.VISIBLE
                        holder.rlImagePost2.visibility = View.VISIBLE


                        if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost.visibility=View.VISIBLE
                            holder.youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost, AppConstants.IMAGES_URL + newsFeeds[position].medias!![0].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost.visibility=View.GONE
                                holder.youtube_layout.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout,newsFeeds[position].medias!![0].fileName!!,holder.youtube_layout)
                            }


                        if(newsFeeds[position].medias!![1].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost2.visibility=View.VISIBLE
                            holder.youtube_layout2.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost2, AppConstants.IMAGES_URL + newsFeeds[position].medias!![1].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![1].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost2.visibility=View.GONE
                                holder.youtube_layout2.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout2,newsFeeds[position].medias!![1].fileName!!,holder.youtube_layout2)
                            }


                    }
                    newsFeeds[position].medias!!.size == 1 -> {
                        holder.linearImages.visibility = View.VISIBLE
                        holder.linearImageTop.visibility = View.VISIBLE
                        holder.linearImageBottom.visibility = View.GONE
                        holder.rlImagePost.visibility = View.VISIBLE
                        holder.rlImagePost2.visibility = View.GONE


                        if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE) {
                            holder.ivImagePost.visibility=View.VISIBLE
                            holder.youtube_layout.visibility=View.GONE
                            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost, AppConstants.IMAGES_URL + newsFeeds[position].medias!![0].fileName!!)
                        }else
                            if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                                holder.ivImagePost.visibility=View.GONE
                                holder.youtube_layout.visibility=View.VISIBLE
                                setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout,newsFeeds[position].medias!![0].fileName!!,holder.youtube_layout)



                           //    (holder.itemView.context as ActivityHome).window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



                            }

                        //  AppHelper.setImage(holder.itemView.context, holder.ivImagePost2, AppConstants.IMAGES_URL+ newsFeeds[position].medias!![1].fileName!!)
                    }
                    else -> {
                        holder.linearImages.visibility = View.GONE
                        holder.linearImageTop.visibility = View.GONE
                        holder.linearImageBottom.visibility = View.GONE
                        holder.rlImagePost.visibility = View.GONE
                        holder.rlImagePost2.visibility = View.GONE

                    }
                }


            } catch (E: Exception) {
            }
        }else{
            holder.linearNewsFeedBody.visibility=View.GONE
            holder.linearEventsBody.visibility=View.VISIBLE
            holder.linearShow.visibility=View.VISIBLE

            try{ holder.tvEventTitle.text=newsFeeds[position].title }catch (e:Exception){}
            try{ holder.tvEventTime.text="Duration: "+ newsFeeds[position].eventDates!![0].fromTime }catch (e:Exception){}

          //  try{ holder.tvEventDate.text=AppHelper.formatDate(holder.itemView.context,newsFeeds[position].eventDates!![0].fromDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}
            try{ holder.tvEventDate.text=newsFeeds[position].eventDates!![0].fromDate }catch (e:Exception){}

            try{ holder.tvEventLocation.text=newsFeeds[position].location }catch (e:Exception){}

            if(newsFeeds[position].isShowMore!!){
                Log.wtf("is_show_more", "1 pos$position")
                holder.linearShowedMoreData.visibility=View.VISIBLE
                holder.tvShowMoreLess.text=holder.itemView.context.getString(R.string.show_less)
            }else{
                Log.wtf("is_show_more", "2 pos$position")
                holder.linearShowedMoreData.visibility=View.GONE
                holder.tvShowMoreLess.text=holder.itemView.context.getString(R.string.show_more)
            }




 /*           try {
                if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE){
                    holder.ivImagePostEvents.visibility=View.VISIBLE
                    holder.youtube_layout_event.visibility=View.GONE

                    AppHelper.setImageResizePost(
                        this,
                        ivImagePostEvents,
                        AppConstants.IMAGES_URL + MyApplication.selectedPost.medias!![0].fileName!!
                    )
                }else{
                    ivImagePostEvents.visibility=View.GONE
                    youtube_layout_event.visibility=View.VISIBLE
                    setYoutube(this,R.id.youtube_layout_event,MyApplication.selectedPost.medias!![0].fileName!!)

                }
            } catch (E: Exception) {
            }
          */




            try{
                holder.rlImageEvent.visibility=View.VISIBLE
                if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_IMAGE){
                    holder.youtube_layout_event.visibility=View.GONE
                    holder.ivImagePostEvents.visibility=View.VISIBLE
               AppHelper.setImageResizePost(
                   holder.itemView.context,
                   holder.ivImagePostEvents,
                AppConstants.IMAGES_URL + newsFeeds[position].medias!![0].fileName!!
            )
                }else if(newsFeeds[position].medias!![0].fileType==AppConstants.MEDIA_TYPE_YOUTUBE) {
                   // holder.ivImagePostEvents.setImageResource(R.drawable.youtube_placeholder)
                    holder.youtube_layout_event.visibility=View.GONE
                    holder.ivImagePostEvents.visibility=View.VISIBLE
                    holder.ivImagePostEvents.setImageResource(com.ids.inpoint.R.drawable.youtube_placeholder)
                }

            }catch (e:Exception){
                holder.rlImageEvent.visibility=View.GONE
                holder.ivImagePostEvents.visibility=View.GONE
            }

            if(newsFeeds[position].medias!!.isEmpty()){
                holder.rlImageEvent.visibility=View.GONE
                holder.ivImagePostEvents.visibility=View.GONE
            }


/*            if(newsFeeds[position].isParticipant!!)
                holder.tvParticipate.text = holder.itemView.context.getString(com.ids.inpoint.R.string.quite)
            else
                holder.tvParticipate.text = holder.itemView.context.getString(com.ids.inpoint.R.string.participate)*/

            if(newsFeeds[position].CanQuit==AppConstants.EVENT_PARTICIPATE_PARTICIPATE) {
                holder.linearParticipate.visibility=View.VISIBLE
                holder.tvParticipate.text =holder.itemView.context.getString(com.ids.inpoint.R.string.participate)
            }
            else  if(newsFeeds[position].CanQuit==AppConstants.EVENT_PARTICIPATE_QUITE) {
                holder.linearParticipate.visibility=View.VISIBLE
                holder.tvParticipate.text =holder.itemView.context.getString(com.ids.inpoint.R.string.quite)
            }
            else  if(newsFeeds[position].CanQuit==AppConstants.EVENT_PARTICIPATE_PENDING){
                holder.linearParticipate.visibility=View.VISIBLE
                holder.tvParticipate.text =holder.itemView.context.getString(com.ids.inpoint.R.string.pending)
            }
            else  if(newsFeeds[position].CanQuit==AppConstants.EVENT_PARTICIPATE_HIDE) {
                holder.linearParticipate.visibility=View.GONE
            }






            try{ holder.ctvTextPostEvents.text = newsFeeds[position].details }catch (e:Exception){}
            holder.tvResourcesTitle.visibility=View.GONE
            holder.rvResources.visibility=View.GONE

        }






  //    if(newsFeeds[position].enableSettings!!)
         holder.linearDots.visibility=View.VISIBLE
     //   else
     //    holder.linearDots.visibility=View.GONE




        try{ holder.ctvName.text = newsFeeds[position].userName }catch (e:Exception){}
        try{ holder.ctvLevel.text = newsFeeds[position].level.toString() }catch (e:Exception){}
        try{ holder.ctvTextPost.text = newsFeeds[position].title }catch (e:Exception){}
      //  try{ holder.ctvUni.text = newsFeeds[position].university }catch (e:Exception){}
       // try{ holder.ctvTimePost.text = newsFeeds[position].publishDate }catch (e:Exception){}
       try{ holder.ctvTimePost.text=AppHelper.formatDate(holder.itemView.context,newsFeeds[position].publishDate!!,"yyyy-MM-dd'T'hh:mm:ss","dd-MM-yyyy hh:mm:a")}catch (e:Exception){}


        try{ holder.ctvLikesCount.text = newsFeeds[position].likeNumber.toString() }catch (e:Exception){}
        try{ holder.ctvCommentsCount.text = newsFeeds[position].commentNumber.toString()+" "+holder.itemView.context.getString(
            com.ids.inpoint.R.string.comments) }catch (e:Exception){}


        if(newsFeeds[position].liked!!)
            holder.ivLike.setImageResource(com.ids.inpoint.R.drawable.like_on)
        else
            holder.ivLike.setImageResource(com.ids.inpoint.R.drawable.like)

        if(newsFeeds[position].showComments!!) {
            holder.linearWriteComment.visibility=View.VISIBLE
            holder.rvComments.visibility=View.VISIBLE
            holder.rvComments.isNestedScrollingEnabled=false
        }else{
            holder.linearWriteComment.visibility=View.GONE
            holder.rvComments.visibility=View.GONE
        }


      //  if(newsFeeds[position].enableSettings!!){
            holder.linearDots.visibility=View.VISIBLE
            try{
            holder.dot1.background.setColorFilter(Color.parseColor(newsFeeds[position].colorHex), PorterDuff.Mode.SRC_ATOP)
            holder.dot2.background.setColorFilter(Color.parseColor(newsFeeds[position].colorHex), PorterDuff.Mode.SRC_ATOP)
            holder.dot3.background.setColorFilter(Color.parseColor(newsFeeds[position].colorHex), PorterDuff.Mode.SRC_ATOP)}
            catch (e:Exception){}
/*        }else{
            holder.linearDots.visibility=View.GONE
        }*/

        try{


       // if(newsFeeds[position].enableCorner!!){
            holder.linearCorner.visibility=View.VISIBLE
            try{ holder.corner1.setBackgroundColor(Color.parseColor(newsFeeds[position].colorHex))}catch (e:Exception){}
            try{ holder.corner2.setBackgroundColor(Color.parseColor(newsFeeds[position].colorHex))}catch (e:Exception){}
                // }else{
        //    holder.linearCorner.visibility=View.INVISIBLE
       // }
    }catch (e:Exception){holder.linearCorner.visibility=View.INVISIBLE}




        if(newsFeeds[position].arrayComments!=null){
            try{
            var adapterComments= AdapterComments(newsFeeds[position].arrayComments!!,this,position,false)
            val glm = GridLayoutManager(holder.itemView.context, 1)
            holder.rvComments.adapter=adapterComments
            holder.rvComments.layoutManager=glm}catch (e:Exception){}
        }


        if(newsFeeds[position].userId==MyApplication.userLoginInfo.id){
            holder.linearUserPostOptions.visibility=View.VISIBLE
            holder.btHidePost.visibility=View.GONE
        }else{
            holder.linearUserPostOptions.visibility=View.GONE
            holder.btHidePost.visibility=View.VISIBLE
        }

        //test
    /*    holder.linearUserPostOptions.visibility=View.VISIBLE
        holder.btHidePost.visibility=View.VISIBLE*/

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


        if(newsFeeds[position].verified!!) {
            AppHelper.setTextColor(holder.itemView.context, holder.tvVerify, R.color.secondary)
            holder.ivVerified.setImageResource(R.drawable.verified_selected)
        }
        else {
            holder.ivVerified.setImageResource(R.drawable.verified)
            AppHelper.setTextColor(holder.itemView.context, holder.tvVerify, R.color.gray_dark)
        }

        if(MyApplication.userLoginInfo.subType==AppConstants.VERIFICATION_TYPE_VERIFIER)
           holder.LinearVerified.visibility=View.VISIBLE
        else
            holder.LinearVerified.visibility=View.GONE


    }


    private fun setYoutube(context:Context, youtube_layout_id:Int,youtubeLink:String,frame:FrameLayout){
        Log.wtf("set_youtube","true")
        val youtubeId = youtubeLink.substring(youtubeLink.lastIndexOf("/")+1)
        val img_url = "http://img.youtube.com/vi/$youtubeId/0.jpg"
        AppHelper.setViewBackgroundResized(img_url, frame)

/*        val youtubeId = youtubeLink.substring(youtubeLink.lastIndexOf("/")+1)

        Log.wtf("youtubeid",youtubeId)
         var youTubePlayerFragment :YouTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance()
        lateinit var youtubePlayer :YouTubePlayer
        // var transaction = itemView.context.childFragmentManager.beginTransaction();
    var transaction = fragmentManager.beginTransaction()
    transaction.add(youtube_layout_id, youTubePlayerFragment).commit();

    youTubePlayerFragment.initialize(context.getString(R.string.GOOGLE_API_KEY), object : YouTubePlayer.OnInitializedListener {

        override fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            player: YouTubePlayer,
            wasRestored: Boolean
        ) {
            if (!wasRestored) {
               val youtubePlayer=player
                youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                youtubePlayer.loadVideo(youtubeId)
              //  youtubePlayer.
                //    youtubePlayer.setShowFullscreenButton(false)
                youtubePlayer.setOnFullscreenListener { //isFullScreen=true
                }

                //youtubePlayer.play()
            }
        }

        override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
            // YouTube error
            val errorMessage = error.toString()
            Log.d("errorMessage:", errorMessage)
        }
    })*/
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
        var btHidePost: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.btHidePost) as LinearLayout
        var btEditPost: TextView = itemView.findViewById(com.ids.inpoint.R.id.btEditPost) as TextView
        var btDeletePost: TextView = itemView.findViewById(com.ids.inpoint.R.id.btDeletePost) as TextView
        var linearUserPostOptions: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearUserPostOptions) as LinearLayout




        //related to item_news_feed_info
        var ivUser: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivUser) as ImageView
        var ctvName: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvName) as CustomTextViewBold
        var ctvLevel: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.ctvLevel) as CustomTextViewBold
        var ctvUni: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvUni) as CustomTextViewMedium
        var ctvTimePost: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvTimePost) as CustomTextViewMedium

        //related to item_news_feed_body
        var linearNewsFeedBody: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearNewsFeedBody) as LinearLayout

        var ivImagePost: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePost) as ImageView
        var ivImagePost2: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePost2) as ImageView
        var ivImagePost3: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePost3) as ImageView
        var ivImagePost4: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePost4) as ImageView

        var rlImagePost: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlImagePost) as RelativeLayout
        var rlImagePost2: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlImagePost2) as RelativeLayout
        var rlImagePost3: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlImagePost3) as RelativeLayout
        var rlImagePost4: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlImagePost4) as RelativeLayout

        var tvMoreMedia: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvMoreMedia) as TextView


        var simpleExoPlayerView: SimpleExoPlayerView = itemView.findViewById(com.ids.inpoint.R.id.simpleExoPlayerView) as SimpleExoPlayerView
        var simpleExoPlayerView2: SimpleExoPlayerView = itemView.findViewById(com.ids.inpoint.R.id.simpleExoPlayerView2) as SimpleExoPlayerView
        var simpleExoPlayerView3: SimpleExoPlayerView = itemView.findViewById(com.ids.inpoint.R.id.simpleExoPlayerView3) as SimpleExoPlayerView
        var simpleExoPlayerView4: SimpleExoPlayerView = itemView.findViewById(com.ids.inpoint.R.id.simpleExoPlayerView4) as SimpleExoPlayerView


        var youtube_layout: FrameLayout = itemView.findViewById(com.ids.inpoint.R.id.youtube_layout) as FrameLayout
        var youtube_layout2: FrameLayout = itemView.findViewById(com.ids.inpoint.R.id.youtube_layout2) as FrameLayout
        var youtube_layout3: FrameLayout = itemView.findViewById(com.ids.inpoint.R.id.youtube_layout3) as FrameLayout
        var youtube_layout4: FrameLayout = itemView.findViewById(com.ids.inpoint.R.id.youtube_layout4) as FrameLayout





        var linearImageTop: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearImageTop) as LinearLayout
        var linearImageBottom: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearImageBottom) as LinearLayout
        var linearImages: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearImages) as LinearLayout


        var ctvTextPost: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvTextPost) as CustomTextViewMedium

        //related to item_news_feed_comment
         var ctvLikesCount: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvLikesCount) as CustomTextViewMedium
         var ctvCommentsCount: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvCommentsCount) as CustomTextViewMedium
         var ivLike: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivLike) as ImageView
         var ivComment: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivComment) as ImageView
         var ivVerified: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivVerified) as ImageView
          var tvVerify: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvVerify) as TextView
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


        //related to item_events_body
        var linearEventsBody: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearEventsBody) as LinearLayout
        var rlImageEvent: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlImageEvent) as RelativeLayout

        var ivImagePostEvents: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePostEvents) as ImageView
        var ctvTextPostEvents: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.ctvTextPostEvents) as CustomTextViewMedium
        var tvEventTitle: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventTitle) as CustomTextViewBold
        var youtube_layout_event: FrameLayout = itemView.findViewById(com.ids.inpoint.R.id.youtube_layout_event) as FrameLayout

        var tvParticipate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvParticipate) as TextView


        var linearShowedMoreData: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearShowMoreData) as LinearLayout
        var linearShow: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearShow) as LinearLayout
        var tvResourcesTitle: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvResourcesTitle) as CustomTextViewBold
        var rvResources: RecyclerView = itemView.findViewById(com.ids.inpoint.R.id.rvResources) as RecyclerView
        var tvEventDate: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventDate) as CustomTextViewBold
        var tvEventTime: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventTime) as CustomTextViewBold
        var tvEventLocation: CustomTextViewBold = itemView.findViewById(com.ids.inpoint.R.id.tvEventLocation) as CustomTextViewBold
        var linearParticipate: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearParticipate) as LinearLayout
        var tvShowMoreLess: CustomTextViewMedium = itemView.findViewById(com.ids.inpoint.R.id.tvShowMoreLess) as CustomTextViewMedium
        var linearLikers: LinearLayout = itemView.findViewById(com.ids.inpoint.R.id.linearLikers) as LinearLayout

        lateinit var youTubePlayerFragment : YouTubePlayerSupportFragment
        lateinit var youtubePlayer : YouTubePlayer


        init {
            itemView.setOnClickListener(this)
            ivImagePost.setOnClickListener(this)
            ivImagePost2.setOnClickListener(this)
            ivImagePost3.setOnClickListener(this)
            ivImagePost4.setOnClickListener(this)
            linearLike.setOnClickListener(this)
            linearComment.setOnClickListener(this)
            linearShare.setOnClickListener(this)
            LinearVerified.setOnClickListener(this)
            ctvCommentsCount.setOnClickListener(this)
            linearLikers.setOnClickListener(this)

            linearShow.setOnClickListener(this)
            linearParticipate.setOnClickListener(this)

            btHidePost.setOnClickListener(this)
            btDeletePost.setOnClickListener(this)
            btEditPost.setOnClickListener(this)

            ivUser.setOnClickListener(this)
            ctvName.setOnClickListener(this)

            //linearDots.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
