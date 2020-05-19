package com.ids.inpoint.controller.Fragments



import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_add_event.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_startup_profile_info.*
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class FragmentStartupProfileInfo : Fragment() ,RVOnItemClickListener {



    private var adapterSocialMedia: AdapterSocialMedia?=null
    private var arraySocialMedia= java.util.ArrayList<ResponseSocialMedia>()
    private var arrayAllSocialMedia= java.util.ArrayList<ItemSpinner>()

    private var arrayTypes= java.util.ArrayList<ItemSpinner>()
    private var arrayIndustry= java.util.ArrayList<ItemSpinner>()
    private var arrayLocation= java.util.ArrayList<ItemSpinner>()
    private var arrayCities= java.util.ArrayList<ItemSpinner>()
    private var arrayCompanySize= java.util.ArrayList<ItemSpinner>()
    private var arraySpeciality= java.util.ArrayList<ItemSpinner>()


    lateinit var adapterCities: AdapterGeneralSpinner
    lateinit var adapterLocations: AdapterGeneralSpinner
    lateinit var adapterIndustries: AdapterGeneralSpinner
    lateinit var adapterCompanySizes: AdapterGeneralSpinner
    lateinit var adapterSpecialities: AdapterGeneralSpinner
    lateinit var adapterTypes: AdapterGeneralSpinner

    private var selectedTypeId=0
    private var selectedIndustryId=0
    private var selectedLocationId=0
    private var selectedCityId=0
    private var selectedCompanyId=0
    private var selectedSpecialityId=0

    private var selectedTypeValue=""
    private var selectedIndustryValue=""
    private var selectedLocationValue=""
    private var selectedCityValue=""
    private var selectedCompanyValue=""
    private var selectedSpecialityValue=""

    private var selectedAddSocialMediaId=0


    private lateinit var foundDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var foundDayCalendar: Calendar
    private var selectedFoundDate = ""

    private var dialog: Dialog? = null
    var btCloseDialog:LinearLayout ?= null
    var btCancel:TextView ?= null
    var btSaveChanges:LinearLayout ?= null
    var spSocialMediaPopup:Spinner ?= null
    var etLink:EditText ?= null
    override fun onItemClicked(view: View, position: Int) {
      if(view.id==R.id.btDelete){
          deleteConfirm(activity!!,adapterSocialMedia!!.arraySocialMedias[position].id!!)
      }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_startup_profile_info, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getStartupProfile()
        getAllSocialMedia()
        getSocialMedia()

    }

    private fun init(){
      setFieldsEditable(false)
        btEditProfile.setOnClickListener{
            btSave.visibility=View.VISIBLE
            setFieldsEditable(true)
            tvNameValue.requestFocus()
            try{tvNameValue.setSelection(tvNameValue.text.length)}catch (e:Exception){}
        }

        btSave.setOnClickListener{
            updateStartupProfile()
            //updateProfileInfo()
        }


        foundDayCalendar = Calendar.getInstance()
        foundDateListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // TODO Auto-generated method stub
                foundDayCalendar.set(Calendar.YEAR, year)
                foundDayCalendar.set(Calendar.MONTH, monthOfYear)
                foundDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                tvFoundedValue.text = AppHelper.dateEvent.format(foundDayCalendar.time)
                selectedFoundDate = AppHelper.dateEvent.format(foundDayCalendar.time)
            }

        tvFoundedValue.setOnClickListener {
            DatePickerDialog(
                activity!!,
                foundDateListener,
                foundDayCalendar.get(Calendar.YEAR),
                foundDayCalendar.get(Calendar.MONTH),
                foundDayCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btAddSocialMedia.setOnClickListener{
            showPopupSocialMedia()
        }

    }



    private fun getStartupProfile(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserDetails(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ResponseStartupProfile> {
                override fun onResponse(call: Call<ResponseStartupProfile>, response: Response<ResponseStartupProfile>) {

                    try{

                        setInfo(response.body()!!)

                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseStartupProfile>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setInfo(response: ResponseStartupProfile) {
        setSpinner(spLocations,arrayLocation,AppConstants.SPINNER_LOCATIONS,0)
        setSpinner(spTypes,arrayTypes,AppConstants.SPINNER_TYPES,AppConstants.LOOKUP_TYPES)
        setSpinner(spIndustry,arrayIndustry,AppConstants.SPINNER_INDUSTRIES,AppConstants.LOOKUP_INDUSTRY)
        setSpinner(spCompanySize,arrayCompanySize,AppConstants.SPINNER_COMPANY_SIZES,AppConstants.LOOKUP_COMPANY_SIZE)
        setSpinner(spSpeciality,arraySpeciality,AppConstants.SPINNER_SPECIALITIES,AppConstants.LOOKUP_SPECIALITY)

        try{tvNameValue.setText(response.userName)}catch (e:Exception){}
        try{tvBioValue.setText(response.bio)}catch (e:Exception){}
        try{tvPhoneValue.setText(response.phoneNumber)}catch (e:Exception){}

        try{tvWebsiteValue.setText(response.website)}catch (e:Exception){}
        try{cbShowPhone.isChecked=response.showToPublic!!}catch (e:Exception){}
        try{
            tvFoundedValue.text =AppHelper.formatDate(activity!!,response.dateOfBirth!!,"yyyy-MM-dd'T'hh:mm:ss","yyyy-MM-dd")
            selectedFoundDate=tvFoundedValue.text.toString()
        }catch (e:Exception){}


        try{tvAddressValue.setText(response.fullAddress)}catch (e:Exception){}
        try{tvWorkExpValue.setText(response.workExperience.toString())}catch (e:Exception){}
        try{tvOverviewValue.setText(response.overview.toString())}catch (e:Exception){}



        try{selectedTypeId=response.subType!!}catch (e:Exception){}
        try{selectedIndustryId=response.industry!!}catch (e:Exception){}
        try{selectedLocationId=response.location!!}catch (e:Exception){}
        try{selectedCompanyId=response.companySize!!}catch (e:Exception){}
        try{selectedSpecialityId=response.specialties!!}catch (e:Exception){}



        try{spLocations.setSelection(getPositionFromId(selectedLocationId,arrayLocation)!!)}catch (e:Exception){}
        try{spTypes.setSelection(getPositionFromId(selectedTypeId,arrayTypes)!!)}catch (e:Exception){}
        try{spCompanySize.setSelection(getPositionFromId(selectedCompanyId,arrayCompanySize)!!)}catch (e:Exception){}
        try{spSpeciality.setSelection(getPositionFromId(selectedSpecialityId,arraySpeciality)!!)}catch (e:Exception){}
        try{spTypes.setSelection(getPositionFromId(selectedTypeId,arrayTypes)!!)}catch (e:Exception){}


        setCitiesArray()
        for(i in arrayCities.indices)
            Log.wtf("array_cities_$i",arrayCities[i].id.toString()+"    name    -"+arrayCities[i].name)
        try{selectedCityId=response.city!!}catch (e:Exception){}
        Log.wtf("selected_city",selectedCityId.toString())
        Log.wtf("selected_city_position",getPositionFromId(selectedCityId,arrayCities).toString())
        Handler().postDelayed({
            try{spCity.setSelection(getPositionFromId(selectedCityId,arrayCities)!!)}catch (e:Exception){}
        }, 150)


    }


    private fun getSocialMedia(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupSocialMedias(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ArrayList<ResponseSocialMedia>> {
                override fun onResponse(call: Call<ArrayList<ResponseSocialMedia>>, response: Response<ArrayList<ResponseSocialMedia>>) {

                    try{

                        setSocialMedia(response.body()!!)

                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseSocialMedia>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }



    private fun setSocialMedia(response: ArrayList<ResponseSocialMedia>) {

        arraySocialMedia.clear()
        arraySocialMedia.addAll(response)
        adapterSocialMedia= AdapterSocialMedia(arraySocialMedia,this,AppConstants.TYPE_CAN_DELETE)
        val glm = GridLayoutManager(activity, 1)
        rvSocialMedia!!.adapter=adapterSocialMedia
        rvSocialMedia!!.layoutManager=glm


    }


    private fun getAllSocialMedia(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupAllSocialMedias()?.enqueue(object : Callback<ArrayList<ResponseSocialMedia>> {
                override fun onResponse(call: Call<ArrayList<ResponseSocialMedia>>, response: Response<ArrayList<ResponseSocialMedia>>) {

                    try{setPopupMediaAdapter(response.body()!!)}catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseSocialMedia>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun setFieldsEditable(editable: Boolean){
        tvNameValue.isEnabled=editable
        tvBioValue.isEnabled=editable
        spTypes.isEnabled=editable
        spIndustry.isEnabled=editable
        tvWebsiteValue.isEnabled=editable
        tvFoundedValue.isEnabled=editable
        spLocations.isEnabled=editable
        spCity.isEnabled=editable
        spCompanySize.isEnabled=editable
        spSpeciality.isEnabled=editable
        tvAddressValue.isEnabled=editable
        tvWorkExpValue.isEnabled=editable
        tvOverviewValue.isEnabled=editable
        tvPhoneValue.isEnabled=editable
        cbShowPhone.isEnabled=editable


    }



    fun deleteConfirm(c: Activity, id: Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_confirmation))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                deleteStartupProduct(id)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun deleteStartupProduct(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupProfileLink(id)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{
                        getSocialMedia()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }



    private fun setSpinner(spinner:Spinner,arrayItems:ArrayList<ItemSpinner>,type:Int,lookupType: Int){
        if(type==AppConstants.SPINNER_LOCATIONS)
            setLocationsArray()
        else if(type==AppConstants.SPINNER_CITIES)
            setCitiesArray()
        else
            setArray(type,lookupType)
        val adapter = AdapterGeneralSpinner(activity!!, R.layout.spinner_text_item, arrayItems,AppConstants.LEFT_SECONDARY)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter;
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                try{
                var item=adapter.getItem(position)
                Log.wtf("selection_type",type.toString())

                when (type) {

                    AppConstants.SPINNER_TYPES -> {
                        selectedTypeId=item!!.id!!
                        selectedTypeValue=item.name!!
                        Log.wtf("selected_value",selectedTypeValue)
                        Log.wtf("selected_value_id",selectedTypeId.toString())
                    }
                    AppConstants.SPINNER_COMPANY_SIZES -> {
                        selectedCompanyId=item!!.id!!
                        selectedCompanyValue=item.name!!
                        Log.wtf("selected_value",selectedCompanyValue)
                        Log.wtf("selected_value_id",selectedCompanyId.toString())
                    }
                    AppConstants.SPINNER_SPECIALITIES -> {
                        selectedSpecialityId=item!!.id!!
                        selectedSpecialityValue=item.name!!
                        Log.wtf("selected_value",selectedSpecialityValue)
                        Log.wtf("selected_value_id",selectedSpecialityId.toString())
                    }
                    AppConstants.SPINNER_INDUSTRIES -> {
                        selectedIndustryId=item!!.id!!
                        selectedIndustryValue=item.name!!
                        Log.wtf("selected_value",selectedIndustryValue)
                        Log.wtf("selected_value_id",selectedIndustryId.toString())
                    }
                    AppConstants.SPINNER_LOCATIONS -> {
                        selectedLocationId=item!!.id!!
                        selectedLocationValue=item.name!!
                        Log.wtf("selected_value",selectedLocationValue)
                        Log.wtf("selected_value_id",selectedLocationId.toString())
                        setCitiesArray()
                        setSpinner(spCity,arrayCities,AppConstants.SPINNER_CITIES,0)
                  }
                    AppConstants.SPINNER_CITIES -> {
                        selectedCityId=item!!.id!!
                        selectedCityValue=item.name!!
                        Log.wtf("selected_value",selectedCityValue)
                        Log.wtf("selected_value_id",selectedCityId.toString())

                    }
                }
            }catch (e:Exception){}}

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        when (type) {
            AppConstants.SPINNER_TYPES -> adapterTypes=adapter
            AppConstants.SPINNER_COMPANY_SIZES -> adapterCompanySizes=adapter
            AppConstants.SPINNER_SPECIALITIES -> adapterSpecialities=adapter
            AppConstants.SPINNER_INDUSTRIES -> adapterIndustries=adapter
            AppConstants.SPINNER_LOCATIONS -> adapterLocations=adapter
            AppConstants.SPINNER_CITIES -> adapterCities=adapter
        }
    }




    private fun getPositionFromId(id:Int,array:ArrayList<ItemSpinner>):Int?{
        var position=0
        for(i in array.indices){
            if(array[i].id==id){
                position=i
                break
            }

        }
        return position

    }




    private fun setArray(type:Int,lookupType:Int) {
        when (type) {
            AppConstants.SPINNER_TYPES -> arrayTypes.clear()
            AppConstants.SPINNER_COMPANY_SIZES -> arrayCompanySize.clear()
            AppConstants.SPINNER_SPECIALITIES -> arraySpeciality.clear()
            AppConstants.SPINNER_INDUSTRIES -> arrayIndustry.clear()

        }
        for (i in MyApplication.arrayGeneralLookup.indices) {
            if (MyApplication.arrayGeneralLookup[i].parentId == lookupType)
                if(MyApplication.languageCode==AppConstants.LANG_ENGLISH){
                    when (type) {
                        AppConstants.SPINNER_TYPES -> arrayTypes.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueEn))
                        AppConstants.SPINNER_COMPANY_SIZES ->  arrayCompanySize.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueEn))
                        AppConstants.SPINNER_SPECIALITIES ->  arraySpeciality.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueEn))
                        AppConstants.SPINNER_INDUSTRIES ->  arrayIndustry.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueEn))

                    }


                    }
                else{
                    when (type) {
                        AppConstants.SPINNER_TYPES ->  arrayTypes.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueAr))
                        AppConstants.SPINNER_COMPANY_SIZES ->  arrayCompanySize.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueAr))
                        AppConstants.SPINNER_SPECIALITIES ->  arraySpeciality.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueAr))
                        AppConstants.SPINNER_INDUSTRIES ->  arrayIndustry.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,MyApplication.arrayGeneralLookup[i].valueAr))
                    }


                }


        }

    }




    private fun setLocationsArray() {
        arrayLocation.clear()
        for (i in MyApplication.arrayAllLocations.indices)
            arrayLocation.add(
                ItemSpinner(
                    MyApplication.arrayAllLocations[i].id,
                    MyApplication.arrayAllLocations[i].valueEn
                )
            )
        if(arrayLocation.size>0)
            selectedLocationId=arrayLocation[0].id!!

    }

    private fun setCitiesArray() {

        arrayCities.clear()
        reloadCities()

    }


    private fun reloadCities() {


        for (i in MyApplication.arrayLocationSubLocations.indices) {
            if (MyApplication.arrayLocationSubLocations[i].parentId == selectedLocationId)
                if(MyApplication.languageCode==AppConstants.LANG_ENGLISH){
                    arrayCities.add(
                        ItemSpinner(
                            MyApplication.arrayLocationSubLocations[i].id,
                            MyApplication.arrayLocationSubLocations[i].valueEn
                        )
                    )
                }else{
                    arrayCities.add(
                        ItemSpinner(
                            MyApplication.arrayLocationSubLocations[i].id,
                            MyApplication.arrayLocationSubLocations[i].valueAr
                        )
                    )
                }

        }

    }




    private fun updateStartupProfile(){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updateStartupProfile(
                MyApplication.selectedStartupTeam.id!!,
                tvBioValue.text.toString(),
                selectedCityId,
                selectedLocationId,
                tvPhoneValue.text.toString(),
                cbShowPhone.isChecked,
                tvNameValue.text.toString(),
                selectedFoundDate,
                selectedTypeId,
                tvWebsiteValue.text.toString(),
                tvWorkExpValue.text.toString().toInt(),
                tvOverviewValue.text.toString(),
                selectedIndustryId,
                tvAddressValue.text.toString(),
                selectedCompanyId,
                selectedSpecialityId

            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    if(response.isSuccessful)
                        createDialog(activity!!,getString(R.string.edited_successfully))
                    else
                        createDialog(activity!!,getString(R.string.try_again))
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }


    private fun createDialog(c: Activity, message: String) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ ->
                dialog.cancel()
                if(message==getString(R.string.edited_successfully) || message==getString(R.string.added_successfully) ) {
                    getStartupProfile()
                    getSocialMedia()
                }
                //activity!!.onBackPressed()

            }
        val alert = builder.create()
        alert.show()
    }



    private fun showPopupSocialMedia() {


        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_social_media)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout
        etLink = dialog!!.findViewById<View>(R.id.etLink) as EditText
        spSocialMediaPopup= dialog!!.findViewById<View>(R.id.spSocialMedia) as Spinner
        getAllSocialMedia()
        btSaveChanges!!.setOnClickListener {
            when {
                etLink!!.text.toString().isEmpty() -> Toast.makeText(activity,getString(R.string.enter_link),
                    Toast.LENGTH_LONG).show()
                else -> {
                    saveSocialMedia(etLink!!.text.toString())
                    dialog!!.dismiss()
                }
            }


        }

        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }




    private fun setPopupMediaAdapter(response: ArrayList<ResponseSocialMedia>){
        arrayAllSocialMedia.clear()
        for (i in response.indices) {
                    arrayAllSocialMedia.add(
                        ItemSpinner(
                            response[i].id,
                            response[i].description
                        )
                    )


        }
        val adapter = AdapterGeneralSpinner(activity!!, R.layout.spinner_text_item, arrayAllSocialMedia,AppConstants.LEFT_SECONDARY)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spSocialMediaPopup!!.adapter = adapter;
        spSocialMediaPopup!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                var item=adapter.getItem(position)
                selectedAddSocialMediaId=item!!.id!!

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }




    }


    private fun saveSocialMedia(link:String){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveSocialMedia(
                link,
                selectedAddSocialMediaId,
                MyApplication.selectedStartupTeam.id!!


            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    if(response.isSuccessful)
                        createDialog(activity!!,getString(R.string.added_successfully))
                    else
                        createDialog(activity!!,getString(R.string.try_again))
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }

}
