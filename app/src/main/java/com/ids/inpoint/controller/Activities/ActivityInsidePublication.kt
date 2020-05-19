package com.ids.inpoint.controller.Activities



import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Toast
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.IFragmentImages
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*


import kotlinx.android.synthetic.main.fragment_courses.btIntro
import kotlinx.android.synthetic.main.fragment_courses.videoTitle
import kotlinx.android.synthetic.main.tab_courses_introduction.*
import kotlinx.android.synthetic.main.tab_media.*
import kotlinx.android.synthetic.main.toolbar.linearToolbar
import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import com.ids.inpoint.utils.AppConstants

import com.ids.inpoint.controller.Adapters.AdapterMedia
import kotlinx.android.synthetic.main.activity_inside_publication_course.*


class ActivityInsidePublication : AppCompactBase(),RVOnItemClickListener , ViewPager.OnPageChangeListener, IFragmentImages {
    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
    }

    override fun onPageClicked(v: View) {
    }

    var selectedTab=0
    private var skip=0
    private var take=100




    lateinit var fragmentManager: FragmentManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ids.inpoint.R.layout.activity_inside_publication_course)
        init()


    }


    private fun init(){

        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
        getPublicationDetails()
      //  getLessonMedia()

        btBack.setOnClickListener{super.onBackPressed()}



        btIntro.setOnClickListener{
            selectedTab=0
            resetTabs()
            tab_intro.visibility=View.VISIBLE
            btIntro.setBackgroundResource(com.ids.inpoint.R.drawable.left_corners_secondary_filled)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                btIntro.setTextColor(ContextCompat.getColor(this, com.ids.inpoint.R.color.white_trans));
            else
                btIntro.setTextColor(application!!.resources.getColor(com.ids.inpoint.R.color.white_trans))

        }

        btMedia.setOnClickListener{
            selectedTab=1
            resetTabs()
            tab_media.visibility=View.VISIBLE
            btMedia.setBackgroundResource(com.ids.inpoint.R.drawable.rectangular_secondary)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                btMedia.setTextColor(ContextCompat.getColor(this, com.ids.inpoint.R.color.white_trans));
            else
                btMedia.setTextColor(resources.getColor(com.ids.inpoint.R.color.white_trans))
        }




        linearToolbar.background.alpha=255

    }


    private fun setMediaPager(arrayMedia:ArrayList<ResponseLessonMedia>){
        val adapterImages = AdapterMedia(arrayMedia, this)
        vpLessonsMedia.adapter = adapterImages
        vpLessonsMedia.addOnPageChangeListener(this)
        // vpLessonsMedia.currentItem = intent.getIntExtra(AppConstants.IMAGE_POSITION, 0)
    }


    override fun onItemClicked(view: View, position: Int) {

    }



    private fun resetTabs(){
        tab_intro.visibility=View.GONE
        tab_media.visibility=View.GONE


        btIntro.setBackgroundResource(com.ids.inpoint.R.drawable.left_corners_secondary)

        btMedia.setBackgroundResource(com.ids.inpoint.R.drawable.right_corners_secondary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btIntro.setTextColor(ContextCompat.getColor(this, com.ids.inpoint.R.color.black));
            btMedia.setTextColor(ContextCompat.getColor(this, com.ids.inpoint.R.color.black));

        }
        else {
            btIntro.setTextColor(resources.getColor(com.ids.inpoint.R.color.black))
            btMedia.setTextColor(resources.getColor(com.ids.inpoint.R.color.black))


        }
    }






    private fun getPublicationDetails(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPublicationDetails(MyApplication.selectedOnlinePublication.id!!
            )?.enqueue(object : Callback<ResponseCoursePublication> {
                override fun onResponse(call: Call<ResponseCoursePublication>, response: Response<ResponseCoursePublication>) {
                    try{
                        onPublicationRetrieved(response.body())
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ResponseCoursePublication>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })

    }

    private fun onPublicationRetrieved(response: ResponseCoursePublication?) {


        try{ tvTitleValue.text= response!!.title}catch (e:Exception){}
        try{ tvPublishDateValue.text=AppHelper.formatDate(this,response!!.publishDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}
        try{ tvSourceValue.text= response!!.source}catch (e:Exception){}
        try{ tvCreationDateValue.text=AppHelper.formatDate(this,response!!.creationDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}

        try{ tvCourseValue.text= response!!.courseName}catch (e:Exception){}
        try{ tvLessonValue.text= response!!.lessonName}catch (e:Exception){}
        try{ tvCategoriesValue.text= response!!.categoryName}catch (e:Exception){}
        try{ tvTagsValue.text= response!!.tagName}catch (e:Exception){}
        try{ tvAuthorValue.text= response!!.authorName}catch (e:Exception){}

        try{ wvIntro.loadData(response!!.description, "text/html; charset=utf-8", "UTF-8")}catch (e:Exception){}

          ivLessonMedia.visibility=View.VISIBLE
          AppHelper.setImageResize(this,ivLessonMedia,AppConstants.IMAGES_URL+response!!.fileName)

    }





    private fun getLessonMedia(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getLessonMedia(MyApplication.selectedLesson.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseLessonMedia>> {
                override fun onResponse(call: Call<ArrayList<ResponseLessonMedia>>, response: Response<ArrayList<ResponseLessonMedia>>) {
                    try{
                        onLessonMediaRetreived(response.body())
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseLessonMedia>>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })

    }


    private fun onLessonMediaRetreived(body: ArrayList<ResponseLessonMedia>?) {
        //AppHelper.setImageResize(this,ivLessonMedia,AppConstants.IMAGES_URL+body!![0].fileName)
        setMediaPager(body!!)
    }


}






