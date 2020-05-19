package com.ids.inpoint.controller.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener

import kotlinx.android.synthetic.main.fragment_fragment_news_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import android.widget.*

import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnSubItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.popup_verify.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*






class FragmentNewsFeed_ : Fragment(),RVOnItemClickListener, RVOnSubItemClickListener {
    override fun onSubItemClicked(view: View, position: Int, parentPosition: Int) {
        Toast.makeText(activity,"aaaaa",Toast.LENGTH_LONG).show()

    }


    var arraySpinnerCategories= java.util.ArrayList<ItemSpinner>()
    lateinit var adapterSpinnerTypes:AdapterTypes
    var showFilter=false
    private var isLoading = false
    private var dialog: Dialog? = null



    private lateinit var fromdatelistener: DatePickerDialog.OnDateSetListener
    private lateinit var fromDateCalendar: Calendar
    private var selectedFromDate = ""

    private lateinit var toDateDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var toDateCalendar: Calendar
    private var selectedToDate = ""


    var dateRange=""
    var selectedTypes=""
    var selectedCategories=""
    var selectedByName=""

    var selectedByTitle=""
    var commonFrag:FragmentCommonNewsEvents?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_fragment_news_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setPagination()


    }

    private fun init(){
        childFragmentManager.beginTransaction()
            .replace(com.ids.inpoint.R.id.container_news_feed, FragmentCommonNewsEvents(), AppConstants.COMMOMN_NEWS_FEED)
            .commit()


        Handler().postDelayed({
            commonFrag = childFragmentManager.findFragmentByTag(AppConstants.COMMOMN_NEWS_FEED) as? FragmentCommonNewsEvents
            commonFrag!!.getPostType(false)
            commonFrag!!.getcategories(false,0)

            // commonFrag!!.testToast()
        }, 200)



        linearSearch.setOnClickListener{
            try{(activity!! as ActivityHome).openSearch(AppConstants.NEWS)}catch (e:Exception){ }
        }
        linearToolbar.background.alpha=255

        linearWritePost.setOnClickListener{

            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).writePost()}catch (e:Exception){ }
        }

        linearCreateEvent.setOnClickListener{

            MyApplication.isEventEdit=false
            MyApplication.isPostEdit=false
            try{(activity!! as ActivityHome).createEvent()}catch (e:Exception){ }
        }


        linearPost.setOnClickListener{
            if(showFilter)
                linearFilter.visibility=View.GONE
            else
                linearFilter.visibility=View.VISIBLE

            showFilter=!showFilter

        }


        if(MyApplication.isProfileImageLocal)
            try{AppHelper.setRoundImage(activity!!,ivToolbarProfile,MyApplication.userLoginInfo.image!!,true) }catch (E:java.lang.Exception){}
        else
            try{AppHelper.setRoundImage(activity!!,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+MyApplication.userLoginInfo.image!!,false) }catch (E:java.lang.Exception){}




        srlNewsFeed.setOnRefreshListener {
            commonFrag!!.skip=0
            commonFrag!!.adapternewsfeed=null
            resetFilters()
            commonFrag!!.getAllPosts()
            srlNewsFeed
        }




        ivToolbarProfile.setOnClickListener(View.OnClickListener { v ->
            try{(activity!! as ActivityHome).goProfile()}catch (e:Exception){ }
        })

        ivChat.setOnClickListener{ try{(activity!! as ActivityHome).goToUserChat(AppConstants.NEWS)}catch (e:Exception){ }}

        ivNotification.setOnClickListener{ try{(activity!! as ActivityHome).goNotification(AppConstants.NEWS)}catch (e:Exception){ }}

    }
    fun reloadData(){
        commonFrag!!.skip=0
        commonFrag!!.adapternewsfeed=null
        resetFilters()
        commonFrag!!.getAllPosts()
    }
    override fun onResume() {

        Handler().postDelayed({
            showFilter=false
            commonFrag!!.getAllPosts()
        }, 400)
        super.onResume()
    }



    override fun onItemClicked(view: View, position: Int) {


    }







    private fun setPagination() {


        svScrollNews.viewTreeObserver
            .addOnScrollChangedListener {
                try {
                    if (svScrollNews.getChildAt(0).bottom <= svScrollNews.height + svScrollNews.scrollY) {
                        //scroll view is at bottom
                        if (!commonFrag!!.isLoading) {
                            commonFrag!!.isLoading = true
                            commonFrag!!.skip += commonFrag!!.take
                            commonFrag!!.getAllPosts()
                        }
                    }
                }catch (E:java.lang.Exception){}

            }




    }




    fun showPopupSpinnerCategory() {

        dialog = Dialog(activity, com.ids.inpoint.R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(com.ids.inpoint.R.layout.popup_verify)
        dialog!!.setCancelable(true)
        commonFrag!!.rvVerifyCategories = dialog!!.findViewById<View>(com.ids.inpoint.R.id.rvVerifyCategories) as RecyclerView
        var tvSubmit = dialog!!.findViewById<View>(com.ids.inpoint.R.id.tvSubmit) as TextView
        tvSubmit.text = getString(com.ids.inpoint.R.string.dialog_ok)
        commonFrag!!.btVerify = dialog!!.findViewById<View>(com.ids.inpoint.R.id.btVerify) as LinearLayout
        commonFrag!!.arraypostCategories.clear()

        if(MyApplication.arrayCategories.size>0) {
            //resetCategories()
            commonFrag!!.setCategories(arrayListOf())
        }
        else {
            commonFrag!!.getcategories(true, 0)
        }
        btVerify!!.setOnClickListener{
            setFilterCategories()
            dialog!!.dismiss()
        }



        dialog!!.show();

    }


    fun showPopupSpinnerTypes() {

        dialog = Dialog(activity, com.ids.inpoint.R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(com.ids.inpoint.R.layout.popup_verify)
        dialog!!.setCancelable(true)
        commonFrag!!.rvVerifyCategories = dialog!!.findViewById<View>(com.ids.inpoint.R.id.rvVerifyCategories) as RecyclerView
        var tvSubmit = dialog!!.findViewById<View>(com.ids.inpoint.R.id.tvSubmit) as TextView
        var tvSelectTitle = dialog!!.findViewById<View>(com.ids.inpoint.R.id.tvSelectTitle) as TextView
        tvSelectTitle.text = getString(com.ids.inpoint.R.string.select_types)
        tvSubmit.text = getString(com.ids.inpoint.R.string.dialog_ok)
        commonFrag!!.btVerify = dialog!!.findViewById<View>(com.ids.inpoint.R.id.btVerify) as LinearLayout
        commonFrag!!.arraypostCategories.clear()

        if(MyApplication.arrayTypes.size>0) {
            //resetCategories()
            setSpinnerTypes()
        }
        else {
            commonFrag!!.getPostType(true)
        }
        btVerify!!.setOnClickListener{
            setFilterTypes()
            dialog!!.dismiss()
        }

        dialog!!.show();

    }


    private fun setFilterCategories() {
        try{tvSelectedCategories.text=getSelectedCategoriesFilter()}catch (e:Exception){}
    }

    private fun setFilterTypes() {
        try{tvSelectedTypes.text=getSelectedSpinnerTypes()}catch (e:Exception){}
    }

    private fun resetTypes(){
        for (i in MyApplication.arrayTypes.indices)
            MyApplication.arrayTypes[i].isSelected=false
    }






    private fun getSelectedCategoriesFilter(): String? {
        var categories=""
        for (i in commonFrag!!.adapterCategories!!.categories.indices){
            if(commonFrag!!.adapterCategories!!.categories[i].isVerified!!){
                if(categories=="") {
                    categories=commonFrag!!.adapterCategories!!.categories[i].valueEn.toString()
                    selectedCategories=commonFrag!!.adapterCategories!!.categories[i].id.toString()
                }else{
                    categories=categories+","+commonFrag!!.adapterCategories!!.categories[i].valueEn.toString()
                    selectedCategories=selectedCategories+","+commonFrag!!.adapterCategories!!.categories[i].id.toString()
                }
            }

        }
        return categories
    }



    private fun getSelectedSpinnerTypes(): String? {
        var types=""
        for (i in adapterSpinnerTypes.itemSpinner.indices){
            if(adapterSpinnerTypes.itemSpinner[i].isSelected!!){
                if(types=="") {
                    types=adapterSpinnerTypes.itemSpinner[i].valueEn.toString()
                    selectedCategories=adapterSpinnerTypes.itemSpinner[i].id.toString()
                }else{
                    types=types+","+adapterSpinnerTypes.itemSpinner[i].valueEn.toString()
                    selectedCategories=selectedCategories+","+adapterSpinnerTypes.itemSpinner[i].id.toString()
                }
            }

        }
        return types
    }


    private fun initFilters(){


        fromDateCalendar = Calendar.getInstance()
        fromdatelistener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            fromDateCalendar.set(Calendar.YEAR, year)
            fromDateCalendar.set(Calendar.MONTH, monthOfYear)
            fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tvFromDate.text = AppHelper.dateFormatProfile.format(fromDateCalendar.time)
            selectedFromDate = AppHelper.dateFormatProfile.format(fromDateCalendar.time)
        }
        tvFromDate.setOnClickListener{
            DatePickerDialog(activity!!, fromdatelistener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }


        toDateCalendar = Calendar.getInstance()
        toDateDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            toDateCalendar.set(Calendar.YEAR, year)
            toDateCalendar.set(Calendar.MONTH, monthOfYear)
            toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tvToDate.text = AppHelper.dateFormatProfile.format(toDateCalendar.time)
            selectedToDate = AppHelper.dateFormatProfile.format(toDateCalendar.time)
        }
        tvToDate.setOnClickListener{
            DatePickerDialog(activity!!, toDateDateListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        arraySpinnerCategories.clear()
        for (i in MyApplication.arrayCategories.indices)
            arraySpinnerCategories.add(ItemSpinner(MyApplication.arrayCategories[i].id,MyApplication.arrayCategories[i].valueEn,MyApplication.arrayCategories[i].iconPath))


        linearSelectedCategories.setOnClickListener{showPopupSpinnerCategory()}
        linearSelectedTypes.setOnClickListener{showPopupSpinnerTypes()}
        rbAll.isChecked=true


        btClear.setOnClickListener{
            resetFilters()
            commonFrag!!.getAllPosts()
        }
        btApply.setOnClickListener{filterNewsFeeds()}


    }


    private fun filterNewsFeeds(){
        commonFrag!!.skip=0
        commonFrag!!.dateFeeds=""
        commonFrag!!.adapternewsfeed=null
        linearFilter.visibility=View.GONE
        showFilter=false
        selectedByTitle=etFilterTitle.text.toString()
        selectedByName=etFilterUsername.text.toString()
        var dateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date())
        if(tvFromDate.text.toString().isEmpty() && tvToDate.text.toString().isEmpty())
            dateRange= "01/01/1991-$dateNow"
        else if(tvFromDate.text.toString().isEmpty() && tvToDate.text.toString().isNotEmpty())
            dateRange="01/01/1991"+"-"+tvToDate.text.toString()
        else if(tvFromDate.text.toString().isEmpty() && tvToDate.text.toString().isEmpty())
            dateRange=tvFromDate.text.toString()+"-"+dateNow
        else
            dateRange=tvFromDate.text.toString()+"-"+tvToDate.text.toString()

        Log.wtf("date_range",dateRange)
        commonFrag!!.applyFilter=true
        commonFrag!!.getAllPosts()
    }

    private fun resetFilters(){
        commonFrag!!.skip=0
        commonFrag!!.dateFeeds=""
        commonFrag!!.adapternewsfeed=null
        linearFilter.visibility=View.GONE
        showFilter=false
        selectedByTitle=""
        selectedByName=""
        dateRange=""
        commonFrag!!.applyFilter=false
        etFilterUsername.setText("")
        etFilterTitle.setText("")
        tvFromDate.text = ""
        tvToDate.text=""
        rbAll.isChecked=true
        commonFrag!!.resetCategories()
        resetTypes()


    }


    private fun setSpinnerTypes(){

        adapterSpinnerTypes= AdapterTypes(MyApplication.arrayTypes,this,AppConstants.SPINNER_POST_PRIVACY)
        val glm = GridLayoutManager(activity, 1)
        commonFrag!!.rvVerifyCategories!!.adapter=adapterSpinnerTypes
        commonFrag!!.rvVerifyCategories!!.layoutManager=glm
    }




}
