package com.ids.inpoint.controller.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.IFragmentImages
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.activity_inside_course.ctvInstructorLevel
import kotlinx.android.synthetic.main.activity_inside_course.ivUserInstructor
import kotlinx.android.synthetic.main.activity_inside_course.tvCourseCode
import kotlinx.android.synthetic.main.activity_inside_course.tvCourseDuration
import kotlinx.android.synthetic.main.activity_inside_course.tvCourseName
import kotlinx.android.synthetic.main.activity_inside_course.tvInstructorDetails
import kotlinx.android.synthetic.main.activity_inside_course.tvInstructorName
import kotlinx.android.synthetic.main.activity_inside_lesson.*
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


class ActivityInsideLesson : AppCompactBase(),RVOnItemClickListener , ViewPager.OnPageChangeListener, IFragmentImages {
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


    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }


    lateinit var fragmentManager: FragmentManager

    private var isCompleted=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ids.inpoint.R.layout.activity_inside_lesson)
        init()
        videoTitle.setOnClickListener {
            // startActivity(Intent(activity,ActivityYoutube::class.java))
            startActivity(Intent(this,PlayerActivity::class.java))
        }


    }


    private fun init(){

        btScrollToTop.visibility=View.GONE
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
        getCourseDetails()
        getLessonMedia()
        checkLessonCompleted()
        try{
            tvlessonName.text = MyApplication.selectedLesson.name
        }catch (e:Exception){}
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


        myScroll.viewTreeObserver
            .addOnScrollChangedListener {
                try {
                    if (myScroll.scrollY>0) {
                        btScrollToTop.visibility=View.VISIBLE
                    }else{
                        btScrollToTop.visibility=View.GONE
                    }
                }catch (E:java.lang.Exception){}

            }

        btScrollToTop.setOnClickListener{
            myScroll.smoothScrollTo(0,0)
        }


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






    private fun getCourseDetails(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getOnlineLessonDetails(MyApplication.selectedOnlineCourse.id!!,MyApplication.selectedLesson.id!!
            )?.enqueue(object : Callback<ResponseOnlineCourseDetail> {
                override fun onResponse(call: Call<ResponseOnlineCourseDetail>, response: Response<ResponseOnlineCourseDetail>) {
                    try{
                        onCourseDetailsRetrieved(response.body())
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ResponseOnlineCourseDetail>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })

    }

    private fun onCourseDetailsRetrieved(response: ResponseOnlineCourseDetail?) {
        try{ tvCourseName.text= response!!.name}catch (e:Exception){}
        try{ ivUserInstructor.setImageResource(com.ids.inpoint.R.drawable.avatar)}catch (e:Exception){}
        try{tvInstructorName.text= response!!.instructorName}catch (e:Exception){}
        ctvInstructorLevel.visibility=View.GONE
        try{tvCourseCode.text=response!!.code}catch (e:Exception){}

        try{tvInstructorDetails.text=response!!.categoriesName}catch (e:Exception){}

        try{ wvIntro.loadData(response!!.LessonDescription, "text/html; charset=utf-8", "UTF-8")}catch (e:Exception){}

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



    private fun checkLessonCompleted(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.checkLessonCompleted(MyApplication.selectedLesson.id!!,MyApplication.userLoginInfo.id!!
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{
                        isCompleted=response.body()!!
                        cbCompleted.isChecked=isCompleted
                        setCompletedAction()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })
   }



    private fun saveLessonCompleted(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveLessonCompeted(MyApplication.selectedLesson.id!!,MyApplication.userLoginInfo.id!!,isCompleted
            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                }

            })
    }

private fun onLessonMediaRetreived(body: ArrayList<ResponseLessonMedia>?) {
    //AppHelper.setImageResize(this,ivLessonMedia,AppConstants.IMAGES_URL+body!!.fileName)
    setMediaPager(body!!)
}

    private fun setCompletedAction() {
        cbCompleted.setOnCheckedChangeListener { buttonView, isChecked ->
            isCompleted=isChecked
            saveLessonCompleted()
        }
    }


}






