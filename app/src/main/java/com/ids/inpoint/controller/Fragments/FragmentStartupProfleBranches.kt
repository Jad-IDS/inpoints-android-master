package com.ids.inpoint.controller.Fragments



import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityInsideComment
import com.ids.inpoint.controller.Activities.ActivityProfile
import com.ids.inpoint.controller.Adapters.*

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.Days
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragement_startup_profile_branches.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_startup_profile_info.*
import kotlinx.android.synthetic.main.fragment_startup_profile_product.*
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class FragmentStartupProfleBranches : Fragment() ,RVOnItemClickListener {



    private var adapterBranch: AdapterStartupBranches?=null
    var arrayBranches= java.util.ArrayList<ResponseBranch>()

    private var adapterOpeningHours: AdapterOpeningHours?=null
    var arrayOpeningHour= java.util.ArrayList<ResponseOpeningHour>()

    var arrayDays= java.util.ArrayList<Days>()

    private var etName: EditText?= null
    private var etPhone: EditText?= null
    private var etLocation: EditText?= null
    private var etDescription: EditText?= null
    private var btCancel: LinearLayout?= null
    private var btSave: LinearLayout?= null
    private var btClose: LinearLayout?= null
    private var tvFromTime: TextView?= null
    private var tvToTime: TextView?= null
    private var dialog: Dialog? = null
    private var spDays: Spinner?= null
    private var selectedFromTime=""
    private var selectedToTime=""

    private lateinit var fromTimeDialog: TimePickerDialog
    private var fromTime = ""
    private lateinit var fromTimeCalendar: Calendar

    private lateinit var toTimeDialog: TimePickerDialog
    private var toTime = ""
    private lateinit var toTimeCalendar: Calendar
    private var selectedDay=""


    override fun onItemClicked(view: View, position: Int) {
        when {
            view.id==R.id.btDeleteA -> {
              deleteConfirm(activity!!,position,AppConstants.ADAPTER_BRANCH,adapterBranch!!.items[position].id!!)
            }
            view.id==R.id.btEditA -> {
                addEditBranch(adapterBranch!!.items[position].id!!,
                    adapterBranch!!.items[position].name!!,
                    adapterBranch!!.items[position].phone!!,
                    adapterBranch!!.items[position].location!!,
                    adapterBranch!!.items[position].description!!

                )
            }
            view.id==R.id.btDeleteB -> {
                deleteConfirm(activity!!,position,AppConstants.ADAPTER_OPENNING_HOUR,adapterOpeningHours!!.items[position].id!!)
            }
            view.id==R.id.btEditB -> {
                addEditOpningHour(adapterOpeningHours!!.items[position].id!!,
                    adapterOpeningHours!!.items[position].day!!,
                    adapterOpeningHours!!.items[position].fromTime!!,
                    adapterOpeningHours!!.items[position].toTime!!

                )
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragement_startup_profile_branches, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getStartupBranches()
        getStartupOpeningHour()

    }

    private fun init(){
          linearBranches.setOnClickListener{
              addEditBranch(0,"","","","")
          }
          linearOpeningHour.setOnClickListener{
            addEditOpningHour(0,"","","")
         }

    }



    private fun getStartupBranches(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupBranches(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ArrayList<ResponseBranch>> {
                override fun onResponse(call: Call<ArrayList<ResponseBranch>>, response: Response<ArrayList<ResponseBranch>>) {
                    try{ setInfoBranches(response.body()!!)}catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseBranch>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setInfoBranches(response: ArrayList<ResponseBranch>) {

        arrayBranches.clear()
        arrayBranches.addAll(response)

        adapterBranch= AdapterStartupBranches(activity!!,arrayBranches,this)
        val glm = GridLayoutManager(activity, 1)
        rvBranch!!.adapter=adapterBranch
        rvBranch!!.layoutManager=glm

    }



    private fun getStartupOpeningHour(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupOpeningHour(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ArrayList<ResponseOpeningHour>> {
                override fun onResponse(call: Call<ArrayList<ResponseOpeningHour>>, response: Response<ArrayList<ResponseOpeningHour>>) {
                    try{ setInfoOpeningHour(response.body()!!)}catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseOpeningHour>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setInfoOpeningHour(response: ArrayList<ResponseOpeningHour>) {

        arrayOpeningHour.clear()
        arrayOpeningHour.addAll(response)

        adapterOpeningHours= AdapterOpeningHours(activity!!,arrayOpeningHour,this)
        val glm = GridLayoutManager(activity, 1)
        rvOpeningHour!!.adapter=adapterOpeningHours
        rvOpeningHour!!.layoutManager=glm

    }


    fun addEditBranch(id:Int,name:String,phone:String,location:String,description:String) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_startup_add_branch)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        etName = dialog!!.findViewById<View>(R.id.etName) as EditText
        etPhone = dialog!!.findViewById<View>(R.id.etPhone) as EditText
        etLocation = dialog!!.findViewById<View>(R.id.etLocation) as EditText
        etDescription = dialog!!.findViewById<View>(R.id.etDescription) as EditText
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as LinearLayout
        btSave = dialog!!.findViewById<View>(R.id.btSave1) as LinearLayout
        btClose = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        //btSave!!.setOnClickListener{Toast.makeText(activity,"aaaa",Toast.LENGTH_LONG).show()}
        btSave!!.setOnClickListener{
            if(etName!!.text.toString().isNotEmpty()
                && etPhone!!.text.toString().isNotEmpty()
                && etLocation!!.text.toString().isNotEmpty()
                && etDescription!!.text.toString().isNotEmpty()
            ){
               var branch=ResponseBranch(id,MyApplication.selectedStartupTeam.id,etName!!.text.toString()
                       ,etPhone!!.text.toString()
                       ,etLocation!!.text.toString()
                       ,etDescription!!.text.toString())
                saveStartupBranch(branch)
            }else
                AppHelper.createDialog(activity!!,getString(R.string.check_empty_fields))
        }

        if(id!=0){
            etName!!.setText(name)
            etPhone!!.setText(phone)
            etLocation!!.setText(location)
            etDescription!!.setText(description)

        }

        btCancel!!.setOnClickListener{dialog!!.dismiss()}
        btClose!!.setOnClickListener{dialog!!.dismiss()}
        dialog!!.show()

    }


    fun addEditOpningHour(id:Int,day:String,from:String,to:String) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_startup_openinghour)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        spDays = dialog!!.findViewById<View>(R.id.spDays) as Spinner
        setResourcesTypeSpinner()

        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as LinearLayout
        btSave = dialog!!.findViewById<View>(R.id.btSave) as LinearLayout
        btClose = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        tvFromTime = dialog!!.findViewById<View>(R.id.tvFrom) as TextView
        tvToTime = dialog!!.findViewById<View>(R.id.tvTo) as TextView
        setTimes()
        tvFromTime!!.setOnClickListener{fromTimeDialog.show()}
        tvToTime!!.setOnClickListener{toTimeDialog.show()}

        btCancel!!.setOnClickListener{dialog!!.dismiss()}
        btClose!!.setOnClickListener{dialog!!.dismiss()}
        if(id!=0){
            selectedFromTime=from
            selectedToTime=to
            selectedDay=day
            Log.wtf("selected_day",getSelectionFromName(day).toString()+"aaaa")
            try{spDays!!.setSelection(getSelectionFromName(day)!!)}catch (e:Exception){
                spDays!!.setSelection(0)
            }
            tvFromTime!!.text=from
            tvToTime!!.text=to
        }

        btSave!!.setOnClickListener{

            if(selectedFromTime.isNotEmpty() && selectedToTime.isNotEmpty()){
                var oppeningHour=ResponseOpeningHour(id,MyApplication.selectedStartupTeam.id!!,selectedFromTime,selectedToTime,selectedDay)
                saveStartupOpenningHour(oppeningHour)
            }else
                AppHelper.createDialog(activity!!,getString(R.string.check_empty_fields))

        }
        dialog!!.show()

    }

    private fun getSelectionFromName(name:String):Int?{
        var position=0
        for(i in arrayDays.indices){
            if(arrayDays[i].sentName!!.trim().toLowerCase()==name.trim().toLowerCase()) {
                position = i
                break
            }
        }
        return position
    }


    private fun setResourcesTypeSpinner(){

        setDays()
        val adapter = SpinAdapter(activity!!, R.layout.spinner_text_item, arrayDays)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spDays!!.adapter = adapter;
        spDays!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
              var day=adapter.getItem(position)
                selectedDay=day!!.sentName!!
           }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }

    private fun setTimes(){
        fromTimeCalendar = Calendar.getInstance()
        fromTimeDialog = TimePickerDialog(activity!!,
            TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minutes ->
                val time = String.format("%02d:%02d", hourOfDay, minutes)
                val amPm: String
                val selected_hours: Int
                if (hourOfDay >= 12) {
                    amPm = "PM";
                    selected_hours=hourOfDay-12

                } else {
                    selected_hours=hourOfDay
                    amPm = "AM";
                }
                var minutesShown=minutes.toString();
                var hoursShown=selected_hours.toString()
                if(minutes.toString().length==1)
                    minutesShown= "0$minutes"

                if(selected_hours.toString().length==1)
                    hoursShown= "0$selected_hours"

                tvFromTime!!.text = "$hoursShown:$minutesShown $amPm"
                selectedFromTime= "$hourOfDay:$minutesShown"
            }, fromTimeCalendar.get(Calendar.HOUR_OF_DAY),  fromTimeCalendar.get(Calendar.MINUTE), false
        )



        toTimeCalendar = Calendar.getInstance()
        toTimeDialog = TimePickerDialog(activity!!,
            TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minutes ->
                val time = String.format("%02d:%02d", hourOfDay, minutes)
                val amPm: String
                val selected_hours: Int
                if (hourOfDay >= 12) {
                    amPm = "PM";
                    selected_hours=hourOfDay-12

                } else {
                    selected_hours=hourOfDay
                    amPm = "AM";
                }
                var minutesShown=minutes.toString();
                var hoursShown=selected_hours.toString()
                if(minutes.toString().length==1)
                    minutesShown= "0$minutes"

                if(selected_hours.toString().length==1)
                    hoursShown= "0$selected_hours"

                tvToTime!!.text = "$hoursShown:$minutesShown $amPm"
                selectedToTime= "$hourOfDay:$minutesShown:00"
            }, toTimeCalendar.get(Calendar.HOUR_OF_DAY),  toTimeCalendar.get(Calendar.MINUTE), false
        )
    }

    private fun setDays(){
        arrayDays.clear()
        arrayDays.add(Days(0,getString(R.string.monday),AppConstants.MONDAY))
        arrayDays.add(Days(1,getString(R.string.tuesday),AppConstants.TUESDAY))
        arrayDays.add(Days(2,getString(R.string.wednesday),AppConstants.WEDNESDAY))
        arrayDays.add(Days(3,getString(R.string.thursday),AppConstants.THURSDAY))
        arrayDays.add(Days(4,getString(R.string.friday),AppConstants.FRIDAY))
        arrayDays.add(Days(5,getString(R.string.saturday),AppConstants.SATURDAY))
        arrayDays.add(Days(6,getString(R.string.sunday),AppConstants.SUNDAY))
    }


    private fun saveStartupBranch(branch: ResponseBranch){
        try{dialog!!.dismiss()}catch (e:Exception){}
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveStartupBranch(branch)?.enqueue(object : Callback<ResponseBranch> {
                override fun onResponse(call: Call<ResponseBranch>, response: Response<ResponseBranch>) {
                    try{
                        getStartupBranches()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<ResponseBranch>, throwable: Throwable) {
                }
            })

    }


    private fun saveStartupOpenningHour(openingHour: ResponseOpeningHour){
        try{dialog!!.dismiss()}catch (e:Exception){}
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveStartupOpenningHour(openingHour)?.enqueue(object : Callback<ResponseOpeningHour> {
                override fun onResponse(call: Call<ResponseOpeningHour>, response: Response<ResponseOpeningHour>) {
                    try{
                        getStartupOpeningHour()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<ResponseOpeningHour>, throwable: Throwable) {
                }
            })

    }


    private fun deleteOpenningHour(id: Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupOpeningHour(id)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{
                        getStartupOpeningHour()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE

                }
            })

    }


    private fun deleteStartupBranch(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupBranch(id)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{getStartupBranches()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }



    fun deleteConfirm(c: Activity, position:Int, type:Int,id: Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_confirmation))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                if(type==AppConstants.ADAPTER_BRANCH){

                 deleteStartupBranch(id)
                }else{
                    deleteOpenningHour(id)
                }
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

}
