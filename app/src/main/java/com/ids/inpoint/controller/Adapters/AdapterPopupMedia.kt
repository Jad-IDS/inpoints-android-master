package com.ids.inpoint.controller.Adapters



import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.IFragmentImages
import com.ids.inpoint.model.response.Media
import com.ids.inpoint.model.response.ResponseLessonMedia
import com.ids.inpoint.model.response.ResponseMedia
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClient
import kotlinx.android.synthetic.main.tab_media.*


import java.util.ArrayList

class AdapterPopupMedia(private val images: ArrayList<Media>, private val fragmentImages: IFragmentImages) : PagerAdapter(),
    View.OnClickListener {

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(R.layout.item_media, container, false)

        itemView.findViewById<View>(R.id.ivImages).setOnClickListener(this)
        container.addView(itemView)
        try{AppHelper.setImageResize(itemView.context,itemView.findViewById(R.id.ivImages) as ImageView, AppConstants.IMAGES_URL+images[position].fileName)}catch (e:Exception){
            Log.wtf("loading_exception",e.toString())}
        return itemView
    }

    override fun onClick(v: View) {
        fragmentImages.onPageClicked(v)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        Glide.clear(`object`)
    }


}
