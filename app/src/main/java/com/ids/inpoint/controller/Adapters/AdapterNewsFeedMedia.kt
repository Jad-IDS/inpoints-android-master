package com.ids.inpoint.controller.Adapters

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.ids.inpoint.R


import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.response.Media
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_courses.*


import java.util.*


class AdapterNewsFeedMedia(val arrayMedia: ArrayList<Media>, private val itemClickListener: RVOnItemClickListener, from:Int) :
    RecyclerView.Adapter<AdapterNewsFeedMedia.VHprivacy>() {

    var type=from
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_media_news_feed_body, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
     //   holder.tvPrivacy.text=arrayMedia[position].name
        if(arrayMedia[position].fileType== AppConstants.MEDIA_TYPE_YOUTUBE) {
            holder.ivImagePost.visibility=View.GONE
            holder.youtube_layout.visibility=View.VISIBLE

            holder.youtube_layout.id=position+1
          //  setYoutube(holder.itemView.context, com.ids.inpoint.R.id.youtube_layout,arrayMedia[position].fileName!!,holder.youtube_layout)
            setThumb(holder.itemView.context, holder.youtube_layout.id,arrayMedia[position].fileName!!,holder.youtube_layout)

            holder.ivPlayYoutube.setOnClickListener{
                 holder.ivPlayYoutube.visibility=View.GONE
                 setYoutube(holder.itemView.context, holder.youtube_layout.id,arrayMedia[position].fileName!!,holder.youtube_layout)
         }


        }else{
            holder.ivImagePost.visibility=View.VISIBLE
            holder.youtube_layout.visibility=View.GONE
            AppHelper.setImageResizePost(holder.itemView.context, holder.ivImagePost, AppConstants.IMAGES_URL + arrayMedia[position].fileName!!)

        }

    }

    override fun getItemCount(): Int {
        return arrayMedia.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var rlImagePost: RelativeLayout = itemView.findViewById(com.ids.inpoint.R.id.rlImagePost) as RelativeLayout
        var ivImagePost: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivImagePost) as ImageView
        var ivPlayYoutube: ImageView = itemView.findViewById(com.ids.inpoint.R.id.ivPlayYoutube) as ImageView
        // var simpleExoPlayerView: SimpleExoPlayer = itemView.findViewById(com.ids.inpoint.R.id.simpleExoPlayerView) as SimpleExoPlayer
        var youtube_layout: FrameLayout = itemView.findViewById(com.ids.inpoint.R.id.youtube_layout_item) as FrameLayout


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }




    private fun setYoutube(context:Context, id:Int,videoId:String,frame:FrameLayout){


        Log.wtf("set_youtube","true")


        val youtubeId = videoId.substring(videoId.lastIndexOf("/")+1)
        Log.wtf("videoid",youtubeId)
       /* val img_url = "http://img.youtube.com/vi/$youtubeId/0.jpg"
        AppHelper.setViewBackgroundResized(img_url, frame)*/

        var fragmentManager = (context as AppCompatActivity).supportFragmentManager
        var youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        var transaction =     fragmentManager.beginTransaction()
        transaction.add(frame.id, youTubePlayerFragment).commit()

      // fragmentManager.beginTransaction().replace(youTubePlayerFragment.getId(), frame).commit()

        lateinit var youtubePlayer : YouTubePlayer
        youTubePlayerFragment.initialize(context.getString(R.string.GOOGLE_API_KEY), object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    youtubePlayer=player
                    youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    // youtubePlayer.loadVideo(youtubeId)
                    youtubePlayer.loadVideo(youtubeId)
                    youtubePlayer.setFullscreen(true)

                    //    youtubePlayer.setShowFullscreenButton(false)
                    youtubePlayer.setOnFullscreenListener {

                    }

                   youtubePlayer.play()

                   // try{progressBar.visibility=View.GONE}catch (e: java.lang.Exception){}
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
                // YouTube error
                val errorMessage = error.toString()
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                Log.d("errorMessage:", errorMessage)
            }
        })






    }



    private fun setThumb(context:Context, id:Int,videoId:String,frame:FrameLayout) {


        Log.wtf("set_youtube", "true")


        val youtubeId = videoId.substring(videoId.lastIndexOf("/") + 1)
        Log.wtf("videoid", youtubeId)
        val img_url = "http://img.youtube.com/vi/$youtubeId/0.jpg"
        AppHelper.setViewBackgroundResized(img_url, frame)
    }


}
