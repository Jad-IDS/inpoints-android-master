package com.ids.inpoint.controller.Fragments


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.ids.inpoint.controller.Adapters.AdapterCustomSpinner
import com.ids.inpoint.controller.Adapters.AdapterRectangularCard
import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.Action
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.controller.MyApplication.Companion.arrayGeneralLookup
import com.ids.inpoint.controller.MyApplication.Companion.arrayLocationSubLocations
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.*
import com.ids.inpoint.model.response.RequestUser
import com.ids.inpoint.model.response.ResponseLocations
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.google.gson.Gson
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterGeneralSpinner


class FragmentEditProfile : Fragment(), RVOnItemClickListener {

    //<editor-fold desc="User More Info">
    private var dialog: Dialog? = null

    private var gotUserEducation = false
    private var gotUserWork = false
    private var gotUserExtraCertification = false

    //<editor-fold desc="User Education">
    private lateinit var adapterUserEducation: AdapterRectangularCard<UserEducation>
    private lateinit var adapterUserWork: AdapterRectangularCard<UserWork>
    private lateinit var adapterUserExtraCertification: AdapterRectangularCard<UserExtraCertification>

    private var arrayUserEducation: java.util.ArrayList<ItemRectangularCard<UserEducation>> =
        arrayListOf()
    private var arrayUserWork: java.util.ArrayList<ItemRectangularCard<UserWork>> = arrayListOf()
    private var arrayUserExtraCertification: java.util.ArrayList<ItemRectangularCard<UserExtraCertification>> =
        arrayListOf()

    private var arrayYears: java.util.ArrayList<Int> = arrayListOf()
    private lateinit var spFrom: Spinner
    private lateinit var spTo: Spinner

    private lateinit var adapterDegrees: AdapterCustomSpinner
    private var arrayDegrees: java.util.ArrayList<ItemSpinner> = arrayListOf()
    private lateinit var spDegrees: Spinner

    private lateinit var adapterSpecifications: AdapterCustomSpinner
    private var arraySpecifications: java.util.ArrayList<ItemSpinner> = arrayListOf()
    private lateinit var spSpecifications: Spinner

    private lateinit var etSchool: EditText
    private lateinit var etDescription: EditText
    private lateinit var etActivities: EditText
    private lateinit var etGrade: EditText
    //</editor-fold>

    //<editor-fold desc="User Work">
    private lateinit var etCompany: EditText
    private lateinit var etPosition: EditText
    private lateinit var tvFromDate: CustomTextViewMedium
    private lateinit var tvToDate: CustomTextViewMedium

    private lateinit var dateListener: DatePickerDialog.OnDateSetListener
    private var activeCalendar = 0
    private lateinit var fromDateCalendar: Calendar
    private lateinit var toDateCalendar: Calendar
    //</editor-fold>

    //<editor-fold desc="User Extra Certification">
    private lateinit var spYear: Spinner
    private lateinit var etExtraCertDescription: EditText
    //</editor-fold>

    //</editor-fold>

    private var selectedGenderId = 0
    private var selectedNationalityId = 0
    private var selectedLocationId = 0
    private var selectedCityId = 0
    private var isShowToPublic = false

    private lateinit var birthDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var myBithDayCalendar: Calendar
    private var selectedDate = ""

    var arrayParentLocations = java.util.ArrayList<ItemSpinner>()
    lateinit var adapterParentLocations: AdapterGeneralSpinner

    var arrayNationalities = java.util.ArrayList<ItemSpinner>()
    lateinit var adapterNationalities: AdapterGeneralSpinner

    var arrayGenders = java.util.ArrayList<ItemSpinner>()
    lateinit var adapterGenders: AdapterGeneralSpinner

    var arrayCities = java.util.ArrayList<ItemSpinner>()
    lateinit var adapterCities: AdapterGeneralSpinner

    override fun onItemClicked(view: View, position: Int) {
        when {
            view.id == R.id.btDeleteA -> AppHelper.createYesNoDialog(
                activity!!,
                getString(R.string.delete_confirmation)
            ) { deleteUserEducation(arrayUserEducation[position].id) }
            view.id == R.id.btEditA -> openUserEducationDialogue(arrayUserEducation[position].mainObject)
            view.id == R.id.btDeleteB -> AppHelper.createYesNoDialog(
                activity!!,
                getString(R.string.delete_confirmation)
            ) { deleteUserWork(arrayUserWork[position].id) }
            view.id == R.id.btEditB -> openUserWorkDialogue(arrayUserWork[position].mainObject)
            view.id == R.id.btDeleteC -> AppHelper.createYesNoDialog(
                activity!!,
                getString(R.string.delete_confirmation)
            ) { deleteUserExtraCertification(arrayUserExtraCertification[position].id) }
            view.id == R.id.btEditC -> openUserExtraCertificationDialogue(arrayUserExtraCertification[position].mainObject)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_edit_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun init() {
        try {
            etName.setText(MyApplication.userLoginInfo.userName.toString())
        } catch (E: Exception) {
        }
        try {
            if (MyApplication.userLoginInfo.phoneNumber != null && MyApplication.userLoginInfo.phoneNumber != "null")
                etPhone.setText(MyApplication.userLoginInfo.phoneNumber.toString())
        } catch (E: Exception) {
        }

        try{etBio.setText(MyApplication.userLoginInfo.bio)}catch (e:java.lang.Exception){}

        try {
            tvDate.text = AppHelper.formatDate(
                this.activity!!,
                MyApplication.userLoginInfo.dateOfBirth.toString(),
                "yyyy-MM-dd'T'hh:mm:ss.SSS",
                "MM/dd/yyyy"
            )
        } catch (E: Exception) {
        }

        try {
            AppHelper.setRoundImage(
                activity!!, ivUserProfile,
                AppConstants.IMAGES_URL + "/" + MyApplication.userLoginInfo.image!!, false
            )
        } catch (E: Exception) {
        }
        try {
            AppHelper.setImage(
                activity!!,
                coverImage,
                AppConstants.IMAGES_URL + "/" + MyApplication.userLoginInfo.coverImage!!
            )
        } catch (E: Exception) {
        }
        try {
            selectedLocationId = MyApplication.userLoginInfo.location!!
        } catch (Exception: Exception) {
        }
        try {
            selectedCityId = MyApplication.userLoginInfo.city!!
        } catch (e: Exception) {
        }
        try {
            selectedNationalityId = MyApplication.userLoginInfo.nationality!!
        } catch (e: Exception) {
        }
        try {
            selectedGenderId = MyApplication.userLoginInfo.gender!!
        } catch (e: Exception) {
        }


         Log.wtf("array_location","aa"+MyApplication.arrayAllLocations.size)
         Log.wtf("array_general_loockup","aa"+MyApplication.arrayGeneralLookup.size)
        Log.wtf("array_sub_loc","aa"+MyApplication.arrayLocationSubLocations.size)


        setLocationsArray()
        setGenderArray()
        setNationalitiesArray()
        setCitiesArray()


       try{
           val item: ItemSpinner? = arrayNationalities.find { it.id == selectedNationalityId }
           spNationalities.setSelection(arrayNationalities.indexOf(item))
       }catch (e:Exception){}


        try{
            val item: ItemSpinner? = arrayParentLocations.find { it.id == selectedLocationId }
            spLocation.setSelection(arrayParentLocations.indexOf(item))
        }catch (e:Exception){}

        try{
            val item: ItemSpinner? = arrayGenders.find { it.id == selectedGenderId }
            spGenders.setSelection(arrayGenders.indexOf(item))
        }catch (e:Exception){}


        try{
            val item: ItemSpinner? = arrayCities.find { it.id == selectedCityId }
            spGenders.setSelection(arrayCities.indexOf(item))
        }catch (e:Exception){}



        isShowToPublic = MyApplication.userLoginInfo.showToPublic!!
        setShowToPublic()
        myBithDayCalendar = Calendar.getInstance()
        birthDateListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // TODO Auto-generated method stub
                myBithDayCalendar.set(Calendar.YEAR, year)
                myBithDayCalendar.set(Calendar.MONTH, monthOfYear)
                myBithDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                tvDate.text = AppHelper.dateFormatProfile.format(myBithDayCalendar.time)
                selectedDate = AppHelper.dateEvent.format(myBithDayCalendar.time)
            }

        tvDate.setOnClickListener {
            DatePickerDialog(
                activity!!,
                birthDateListener,
                myBithDayCalendar.get(Calendar.YEAR),
                myBithDayCalendar.get(Calendar.MONTH),
                myBithDayCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        ivBack.setOnClickListener { activity!!.onBackPressed() }
        if (!arrayYears.any()) {
            var cal = Calendar.getInstance()
            var currentYear = cal.get(Calendar.YEAR)

            for (i in 1970..currentYear) {
                arrayYears.add(i)
            }
        }
    }

    fun setImages() {

        if (MyApplication.isProfileImageLocal) {
            try {
                AppHelper.setRoundImage(
                    activity!!, ivUserProfile,
                    MyApplication.userLoginInfo.image!!, true
                )
            } catch (E: Exception) {
            }

        } else {
            try {
                AppHelper.setRoundImage(
                    activity!!, ivUserProfile,
                    AppConstants.IMAGES_URL + "/" + MyApplication.userLoginInfo.image!!, false
                )
            } catch (E: Exception) {
            }

        }

        if (MyApplication.isCoverImageLocal) {
            try {
                AppHelper.setImage(
                    activity!!,
                    coverImage,
                    MyApplication.userLoginInfo.coverImage!!,
                    true
                )
            } catch (E: Exception) {
            }

        } else {
            try {
                AppHelper.setImage(
                    activity!!,
                    coverImage,
                    AppConstants.IMAGES_URL + "/" + MyApplication.userLoginInfo.coverImage!!
                )
            } catch (E: Exception) {
            }

        }

    }

    private fun getPositionFromId(adapter: AdapterSpinner, id: Int): Int? {
        var position = 0
        for (i in adapter.itemSpinner.indices) {
            if (adapter.itemSpinner[i].id == id) {
                position = i
                break
            }
        }
        return position

    }

    private fun setProfileInfo() {
        btProfileInfo.setBackgroundResource(R.drawable.rounded_corner_white)
        AppHelper.setTextColor(activity!!, btProfileInfo, R.color.primary)
        btEducationInfo.setBackgroundResource(R.color.transparent)
        AppHelper.setTextColor(activity!!, btEducationInfo, R.color.white)
        linearProfileInfo.visibility = View.VISIBLE
        linearEducationInfo.visibility = View.GONE
    }

    private fun setEducationInfo() {
        if (linearEducationInfo.visibility != View.VISIBLE) {
            btEducationInfo.setBackgroundResource(R.drawable.rounded_corner_white)
            AppHelper.setTextColor(activity!!, btEducationInfo, R.color.primary)

            btProfileInfo.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(activity!!, btProfileInfo, R.color.white)

            linearEducationInfo.visibility = View.VISIBLE
            linearProfileInfo.visibility = View.GONE
            fillEducationInfo()
        }
    }



    private fun setListeners() {
        linearEditPassword.setOnClickListener {
            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.UPDATE_PASSWORD,
                FragmentUpdatePassword(),
                AppConstants.UPDATE_PASSWORD_FRAG
            )
        }

        btUpdateCover.setOnClickListener {
            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.EDIT_COVER_IMAGE,
                FragmentUploadBg(),
                AppConstants.EDIT_COVER_IMAGE_FRAG
            )

        }

        rlUserProfile.setOnClickListener {
            MyApplication.previews_frag = AppConstants.EDIT_PROFILE
            AppHelper.AddFragment(
                this.fragmentManager!!,
                AppConstants.EDIT_PROFILE_IMAGE,
                FragmentUploadImage(),
                AppConstants.EDIT_PROFILE_IMAGE_FRAG
            )
        }

        btProfileInfo.setOnClickListener { setProfileInfo() }
        btEducationInfo.setOnClickListener { setEducationInfo() }


        linearShowToPublic.setOnClickListener {
            isShowToPublic = !isShowToPublic
            setShowToPublic()
        }

        btSaveChanges.setOnClickListener {
            updateUser()
           // updateUsernameBio()
        }
        //<editor-fold desc="User More Info"

        btAddUserEducation.setOnClickListener {
            openUserEducationDialogue()
        }

        btAddUserWork.setOnClickListener {
            openUserWorkDialogue()
        }

        btAddUserExtraCertification.setOnClickListener {
            openUserExtraCertificationDialogue()
        }
        fromDateCalendar = Calendar.getInstance()
        toDateCalendar = Calendar.getInstance()
        dateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            when (activeCalendar) {
                0 ->
                    fillDate(year, monthOfYear, dayOfMonth, fromDateCalendar, tvFromDate)
                1 ->
                    fillDate(year, monthOfYear, dayOfMonth, toDateCalendar, tvToDate)
            }
        }
        //</editor-fold>
    }

    private fun setNationalitiesArray() {

        arrayNationalities.clear()
        for (i in MyApplication.arrayGeneralLookup.indices) {
            if (MyApplication.arrayGeneralLookup[i].parentId == AppConstants.LOOKUP_NATIONALITY_ID)
               if(MyApplication.languageCode==AppConstants.LANG_ENGLISH){
                      arrayNationalities.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,
                                             MyApplication.arrayGeneralLookup[i].valueEn)
                )}else{
                      arrayNationalities.add(ItemSpinner(MyApplication.arrayGeneralLookup[i].id,
                        MyApplication.arrayGeneralLookup[i].valueAr))
               }


        }


        adapterNationalities = AdapterGeneralSpinner(activity!!, R.layout.spinner_text_item, arrayNationalities,AppConstants.LEFT_WHITE)
        adapterNationalities.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spNationalities!!.adapter = adapterNationalities;
        spNationalities!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                var item=adapterNationalities.getItem(position)
                selectedNationalityId=item!!.id!!

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }



    }

    private fun setLocationsArray() {
        arrayParentLocations.clear()
        for (i in MyApplication.arrayAllLocations.indices)
            arrayParentLocations.add(
                ItemSpinner(
                    MyApplication.arrayAllLocations[i].id,
                    MyApplication.arrayAllLocations[i].valueEn
                )
            )


        adapterParentLocations = AdapterGeneralSpinner(activity!!, R.layout.spinner_text_item, arrayParentLocations,AppConstants.LEFT_WHITE)
        adapterParentLocations.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spLocation!!.adapter = adapterParentLocations;
        spLocation!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                var item=adapterParentLocations.getItem(position)
                selectedLocationId=item!!.id!!
                reloadCities()
                adapterCities.notifyDataSetChanged()


            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }

    private fun setGenderArray() {

        arrayGenders.clear()
        arrayGenders.add(ItemSpinner(1, getString(R.string.male)))
        arrayGenders.add(ItemSpinner(2, getString(R.string.female)))
        arrayGenders.add(ItemSpinner(3, getString(R.string.none)))


        adapterGenders = AdapterGeneralSpinner(activity!!, R.layout.spinner_text_item, arrayGenders,AppConstants.LEFT_WHITE)
        adapterGenders.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spGenders!!.adapter = adapterGenders;
        spGenders!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                var item=adapterGenders.getItem(position)
                selectedGenderId=item!!.id!!

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

    }

    private fun setCitiesArray() {

        arrayCities.clear()
        reloadCities()

        adapterCities = AdapterGeneralSpinner(activity!!, R.layout.spinner_text_item, arrayCities,AppConstants.LEFT_WHITE)
        adapterCities.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spCities!!.adapter = adapterCities;
        spCities!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                var item=adapterCities.getItem(position)
                selectedCityId=item!!.id!!

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }


    }

    private fun reloadCities() {
        arrayCities.clear()
        for (i in arrayLocationSubLocations.indices) {
            if (arrayLocationSubLocations[i].parentId == selectedLocationId)
               if(MyApplication.languageCode==AppConstants.LANG_ENGLISH){
                arrayCities.add(
                    ItemSpinner(
                        arrayLocationSubLocations[i].id,
                        arrayLocationSubLocations[i].valueEn
                    )
                )
               }else{
                   arrayCities.add(
                       ItemSpinner(
                           arrayLocationSubLocations[i].id,
                           arrayLocationSubLocations[i].valueAr
                       )
                   )
               }

        }
    }

    private fun updateUser() {
        var updatedUser=RequestUser(
            MyApplication.userLoginInfo.id,
            etBio.text.toString(),
            selectedCityId,
            selectedLocationId,
            selectedNationalityId,
            etPhone.text.toString(),
            MyApplication.languageCode,
            isShowToPublic,
            etName.text.toString(),
            selectedGenderId,
            selectedDate,
            (if (MyApplication.userLoginInfo.subType != null) MyApplication.userLoginInfo.subType else 0)!!,
            (if (MyApplication.userLoginInfo.website != null) MyApplication.userLoginInfo.website else "")!!,
            (if (MyApplication.userLoginInfo.workExperience != null) MyApplication.userLoginInfo.workExperience else 0)!!,
            (if (MyApplication.userLoginInfo.overview != null) MyApplication.userLoginInfo.overview else "")!!,
            (if (MyApplication.userLoginInfo.industry != null) MyApplication.userLoginInfo.industry else 0)!!,
            (if (MyApplication.userLoginInfo.fullAddress != null) MyApplication.userLoginInfo.fullAddress else "")!!,
            (if (MyApplication.userLoginInfo.companySize != null) MyApplication.userLoginInfo.companySize else 0)!!,
            (if (MyApplication.userLoginInfo.specialties != null) MyApplication.userLoginInfo.specialties else 0)!!
 )



        val gson = Gson()
        val json = gson.toJson(updatedUser)
        Log.wtf("json_user",json)


        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updateUser(
                updatedUser
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try {
                        loading.visibility = View.GONE
                        if(response.isSuccessful) {
                            activity!!.onBackPressed()
                            MyApplication.userLoginInfo.userName=etName.text.toString()
                            MyApplication.userLoginInfo.phoneNumber=etPhone.text.toString()
                            MyApplication.userLoginInfo.bio=etBio.text.toString()
                            MyApplication.userLoginInfo.showToPublic=isShowToPublic
                            MyApplication.userLoginInfo.city=selectedCityId
                            MyApplication.userLoginInfo.location=selectedLocationId
                            MyApplication.userLoginInfo.nationality=selectedNationalityId
                            MyApplication.userLoginInfo.dateOfBirth=selectedDate
                            MyApplication.userLoginInfo.gender=selectedGenderId

                        }else
                            AppHelper.createDialog(activity!!,getString(R.string.try_again))

                    } catch (E: Exception) {
                        Toast.makeText(activity, getString(R.string.try_again), Toast.LENGTH_LONG).show()
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    Toast.makeText(activity, getString(R.string.try_again), Toast.LENGTH_LONG).show()
                    loading.visibility = View.GONE
                }
            })
    }

    private fun updateUsernameBio() {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updateUsernameBio(
                etName.text.toString(),
                etBio.text.toString(),
                MyApplication.userLoginInfo.id!!
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try {
                        MyApplication.userLoginInfo.userName = etName.text.toString()
                    } catch (e: Exception) {
                    }
                    try {
                        MyApplication.userLoginInfo.bio = etBio.text.toString()
                    } catch (e: Exception) {
                    }

                    /* try{

                     }catch (E:Exception){

                     }*/
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    // loading.visibility=View.GONE
                }
            })

    }

    private fun setShowToPublic() {
        if (isShowToPublic) {
            ivShowToPublic.setImageResource(R.drawable.visibility_on)
            tvPrivacy.text = getString(R.string.public_view)
        } else {
            ivShowToPublic.setImageResource(R.drawable.visibility_off)
            tvPrivacy.text = getString(R.string.private_view)
        }
    }

    private fun fillEducationInfo() {
        loading.visibility = View.VISIBLE
        getUserEducation()
        getUserWork()
        getUserExtraCertification()
    }

    private fun getUserEducation() {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserEducation(MyApplication.userLoginInfo.id!!)
            ?.enqueue(object : Callback<ArrayList<UserEducation>> {
                override fun onFailure(call: Call<ArrayList<UserEducation>>, t: Throwable) {
                    gotUserEducation = true
                    hideLoading()
                }

                override fun onResponse(
                    call: Call<ArrayList<UserEducation>>,
                    response: Response<ArrayList<UserEducation>>
                ) {
                    onUserEducationReceived(response)
                }
            })
    }

    private fun onUserEducationReceived(response: Response<ArrayList<UserEducation>>) {
        try {
            arrayUserEducation.clear()

            response.body()!!.forEach {
                arrayUserEducation.add(
                    ItemRectangularCard(
                        id = it.id!!,
                        title = it.school!!,
                        subtitle1 = "${it.degreeDescription} - ${it.specificationDescription} - ${it.grade}",
                        subtitle2 = "${it.startYear} - ${it.endYear}",
                        mainObject = it
                    )
                )
            }

            adapterUserEducation =
                AdapterRectangularCard(context!!, arrayUserEducation, this, 1, Action.A)
            val glm = GridLayoutManager(activity, 1)
            rvUserEducation.layoutManager = glm
            rvUserEducation.adapter = adapterUserEducation

        } catch (ex: Exception) {
            var t = ex
        }
        gotUserEducation = true
        hideLoading()
    }

    private fun deleteUserEducation(id: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteUserEducation(id)
            ?.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    gotUserEducation = false
                    getUserEducation()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    gotUserEducation = false
                    getUserEducation()
                }
            })
    }

    private fun getUserWork() {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserWork(MyApplication.userLoginInfo.id!!)
            ?.enqueue(object : Callback<ArrayList<UserWork>> {
                override fun onFailure(call: Call<ArrayList<UserWork>>, t: Throwable) {
                    gotUserWork = true
                    hideLoading()
                }

                override fun onResponse(
                    call: Call<ArrayList<UserWork>>,
                    response: Response<ArrayList<UserWork>>
                ) {
                    onUserWorkReceived(response)
                }
            })
    }

    private fun onUserWorkReceived(response: Response<ArrayList<UserWork>>) {
        try {
            arrayUserWork.clear()

            response.body()!!.forEach {

                var to = ""
                try {
                    to = AppHelper.dateEvent.format(AppHelper.dateEvent.parse(it.to))
                } catch (ex: java.lang.Exception) {
                    to = getString(R.string.present)
                }

                arrayUserWork.add(
                    ItemRectangularCard(
                        id = it.id!!,
                        title = it.company!!,
                        subtitle1 = "${it.position}",
                        subtitle2 = "${AppHelper.dateEvent.format(AppHelper.dateEvent.parse(it.from))} - $to",
                        mainObject = it
                    )
                )
            }

            adapterUserWork = AdapterRectangularCard(context!!, arrayUserWork, this, 2, Action.B)
            val glm = GridLayoutManager(activity, 1)
            rvUserWork.layoutManager = glm
            rvUserWork.adapter = adapterUserWork

        } catch (ex: Exception) {
            var t = ex
        }
        gotUserWork = true
        hideLoading()
    }

    private fun deleteUserWork(id: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteUserWork(id)
            ?.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    gotUserWork = false
                    getUserWork()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    gotUserWork = false
                    getUserWork()
                }
            })
    }

    private fun getUserExtraCertification() {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getUserExtraCertification(MyApplication.userLoginInfo.id!!)
            ?.enqueue(object : Callback<ArrayList<UserExtraCertification>> {
                override fun onFailure(
                    call: Call<ArrayList<UserExtraCertification>>,
                    t: Throwable
                ) {
                    gotUserExtraCertification = true
                    hideLoading()
                }

                override fun onResponse(
                    call: Call<ArrayList<UserExtraCertification>>,
                    response: Response<ArrayList<UserExtraCertification>>
                ) {
                    onUserExtraCertification(response)
                }
            })
    }

    private fun onUserExtraCertification(response: Response<ArrayList<UserExtraCertification>>) {
        try {
            arrayUserExtraCertification.clear()

            response.body()!!.forEach {
                arrayUserExtraCertification.add(
                    ItemRectangularCard(
                        id = it.id!!,
                        title = it.description!!,
                        subtitle1 = "",
                        subtitle2 = "${it.year}",
                        mainObject = it
                    )
                )
            }

            adapterUserExtraCertification =
                AdapterRectangularCard(context!!, arrayUserExtraCertification, this, 3, Action.C)
            val glm = GridLayoutManager(activity, 1)
            rvUserExtraCertification.layoutManager = glm
            rvUserExtraCertification.adapter = adapterUserExtraCertification

            //adapterUserExtraCertification.

        } catch (ex: Exception) {
            var t = ex
        }
        gotUserExtraCertification = true
        hideLoading()
    }

    private fun deleteUserExtraCertification(id: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteUserExtraCertification(id)
            ?.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    gotUserExtraCertification = false
                    getUserExtraCertification()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    gotUserExtraCertification = false
                    getUserExtraCertification()
                }
            })
    }

    private fun openUserEducationDialogue(value: UserEducation? = null) {

        fun fillDegree(value: UserEducation) {
            var degree = arrayDegrees.single {
                it.id == value.degreeId
            }
            spDegrees.setSelection(arrayDegrees.indexOf(degree))
        }

        fun fillSpec(value: UserEducation) {
            var spec = arraySpecifications.single {
                it.id == value.specificationId
            }
            spSpecifications.setSelection(arraySpecifications.indexOf(spec))
        }

        try {
            dialog = Dialog(activity)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.setContentView(R.layout.popup_user_education)
            dialog!!.setCancelable(true)

            spFrom = dialog!!.findViewById(R.id.spFrom) as Spinner
            spFrom.adapter =
                ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, arrayYears)

            spTo = dialog!!.findViewById(R.id.spTo) as Spinner
            spTo.adapter =
                ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, arrayYears)

            if (value == null) {
                getGeneralLookupParentById(AppConstants.DEGREES) {}
            } else {
                getGeneralLookupParentById(AppConstants.DEGREES) { fillDegree(value) }
            }

            var selectedDegree = 0
            spDegrees = dialog!!.findViewById(R.id.spDegrees) as Spinner
            spDegrees.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedDegree = arrayDegrees[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }

            if (value == null) {
                getGeneralLookupParentById(AppConstants.SPECIFICATIONS) {}
            } else {
                getGeneralLookupParentById(AppConstants.SPECIFICATIONS) { fillSpec(value) }
            }

            var selectedSpec = 0
            spSpecifications = dialog!!.findViewById(R.id.spSpecifications) as Spinner
            spSpecifications.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedSpec = arraySpecifications[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }

            etSchool = dialog!!.findViewById(R.id.etSchool) as EditText
            etDescription = dialog!!.findViewById(R.id.etDescription) as EditText
            etActivities = dialog!!.findViewById(R.id.etActivities) as EditText
            etGrade = dialog!!.findViewById(R.id.etGrade) as EditText

            if (value != null) {
                etSchool.setText(value.school)
                etDescription.setText(value.description)

                etActivities.setText(value.activities)
                etGrade.setText(value.grade.toString())

                try {
                    spFrom.setSelection(arrayYears.indexOf(value.startYear!!))
                } catch (ex: java.lang.Exception) {

                }

                try {
                    spTo.setSelection(arrayYears.indexOf(value.endYear!!))
                } catch (ex: java.lang.Exception) {

                }
            }

            var btClose = dialog!!.findViewById(R.id.btClose) as ImageView
            btClose.setOnClickListener {
                dialog!!.dismiss()
            }

            var btCancel = dialog!!.findViewById(R.id.btCancel) as LinearLayout
            btCancel.setOnClickListener {
                dialog!!.dismiss()
            }

            var btSave = dialog!!.findViewById(R.id.btSave) as LinearLayout
            btSave.setOnClickListener {
                var toSave: UserEducation

                if (value == null) {
                    toSave = UserEducation()
                } else {
                    toSave = value
                }

                if (etSchool.text.trim().isEmpty() || etDescription.text.trim().isEmpty()) {
                    AppHelper.createDialog(activity!!, getString(R.string.fill_required_fields))
                } else {
                    toSave.school = etSchool.text.toString()
                    toSave.description = etDescription.text.toString()

                    try {
                        toSave.degreeId = selectedDegree
                    } catch (ex: Exception) {
                        var t = ex
                    }

                    try {
                        toSave.specificationId = selectedSpec
                    } catch (ex: Exception) {
                        var t = ex
                    }

                    toSave.activities = etActivities.text.toString()

                    if (etGrade.text.trim().isNotEmpty()) {
                        toSave.grade = etGrade.text.toString().toInt()
                    }

                    try {
                        toSave.startYear = spFrom.selectedItem as Int
                    } catch (ex: Exception) {
                        var t = ex
                    }

                    try {
                        toSave.endYear = spTo.selectedItem as Int
                    } catch (ex: Exception) {
                        var t = ex
                    }

                    saveUserEducation(toSave, dialog!!)
                }
            }

            dialog!!.show()
        } catch (ex: Exception) {

        }
    }

    private fun openUserWorkDialogue(value: UserWork? = null) {
        try {
            dialog = Dialog(activity)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.setContentView(R.layout.popup_user_work)
            dialog!!.setCancelable(true)

            etCompany = dialog!!.findViewById(R.id.etCompany) as EditText
            etPosition = dialog!!.findViewById(R.id.etPosition) as EditText

            tvFromDate = dialog!!.findViewById(R.id.tvFromDate) as CustomTextViewMedium
            tvFromDate.setOnClickListener {
                activeCalendar = 0
                DatePickerDialog(
                    context,
                    dateListener,
                    fromDateCalendar.get(Calendar.YEAR),
                    fromDateCalendar.get(
                        Calendar.MONTH
                    ),
                    fromDateCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            tvToDate = dialog!!.findViewById(R.id.tvToDate) as CustomTextViewMedium
            tvToDate.setOnClickListener {
                activeCalendar = 1
                DatePickerDialog(
                    context, dateListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(
                        Calendar.MONTH
                    ), toDateCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            if (value != null) {
                etCompany.setText(value.company)
                etPosition.setText(value.position)

                try {
                    var d = AppHelper.dateEvent.parse(value.from)
                    fromDateCalendar.time = d
                    fillDate(
                        fromDateCalendar.get(Calendar.YEAR),
                        fromDateCalendar.get(Calendar.MONTH),
                        fromDateCalendar.get(Calendar.DAY_OF_MONTH),
                        fromDateCalendar,
                        tvFromDate
                    )
                } catch (ex: Exception) {
                }

                try {
                    var d = AppHelper.dateEvent.parse(value.to)
                    toDateCalendar.time = d
                    fillDate(
                        toDateCalendar.get(Calendar.YEAR),
                        toDateCalendar.get(Calendar.MONTH),
                        toDateCalendar.get(Calendar.DAY_OF_MONTH),
                        toDateCalendar,
                        tvToDate
                    )
                } catch (ex: Exception) {
                }
            }

            var btClose = dialog!!.findViewById(R.id.btClose) as ImageView
            btClose.setOnClickListener {
                dialog!!.dismiss()
            }

            var btCancel = dialog!!.findViewById(R.id.btCancel) as LinearLayout
            btCancel.setOnClickListener {
                dialog!!.dismiss()
            }

            var btSave = dialog!!.findViewById(R.id.btSave) as LinearLayout
            btSave.setOnClickListener {
                var toSave: UserWork

                if (value == null) {
                    toSave = UserWork()
                } else {
                    toSave = value
                }

                if (etCompany.text.trim().isEmpty() || etPosition.text.trim().isEmpty() || tvFromDate.text.trim().isEmpty()) {
                    AppHelper.createDialog(activity!!, getString(R.string.fill_required_fields))
                } else {
                    toSave.company = etCompany.text.toString()
                    toSave.position = etPosition.text.toString()
                    toSave.from = tvFromDate.text.toString()
                    toSave.to = tvToDate.text.toString()

                    saveUserWork(toSave, dialog!!)
                }
            }

            dialog!!.show()
        } catch (ex: Exception) {
        }
    }

    private fun openUserExtraCertificationDialogue(value: UserExtraCertification? = null) {
        try {
            dialog = Dialog(activity)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.setContentView(R.layout.popup_user_extra_certification)
            dialog!!.setCancelable(true)

            etExtraCertDescription = dialog!!.findViewById(R.id.etExtraCertDescription) as EditText
            spYear = dialog!!.findViewById(R.id.spYear) as Spinner
            spYear.adapter =
                ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, arrayYears)

            if (value != null) {
                etExtraCertDescription.setText(value.description)
                try {
                    spYear.setSelection(arrayYears.indexOf(value.year!!))
                } catch (ex: java.lang.Exception) {

                }
            }

            var btClose = dialog!!.findViewById(R.id.btClose) as ImageView
            btClose.setOnClickListener {
                dialog!!.dismiss()
            }

            var btCancel = dialog!!.findViewById(R.id.btCancel) as LinearLayout
            btCancel.setOnClickListener {
                dialog!!.dismiss()
            }

            var btSave = dialog!!.findViewById(R.id.btSave) as LinearLayout
            btSave.setOnClickListener {

                var toSave: UserExtraCertification

                if (value == null) {
                    toSave = UserExtraCertification()
                } else {
                    toSave = value
                }

                if (etExtraCertDescription.text.trim().isEmpty()) {
                    AppHelper.createDialog(activity!!, getString(R.string.fill_required_fields))
                } else {
                    toSave.description = etExtraCertDescription.text.toString()

                    try {
                        toSave.year = spYear.selectedItem as Int
                    } catch (ex: Exception) {
                        var t = ex
                    }

                    saveUserExtraCertification(toSave, dialog!!)
                }
            }

            dialog!!.show()
        } catch (ex: Exception) {

        }
    }

    private fun getGeneralLookupParentById(parentId: Int, doAction: () -> Unit) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getGeneralLookupParentById(
                parentId
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseLocations>>,
                    response: Response<ArrayList<ResponseLocations>>
                ) {
                    try {
                        when (parentId) {
                            AppConstants.DEGREES -> {
                                onDegreesRetrieved(response.body(), doAction)
                            }
                            AppConstants.SPECIFICATIONS -> {
                                onSpecificationsRetrieved(response.body(), doAction)
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseLocations>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun onDegreesRetrieved(
        body: java.util.ArrayList<ResponseLocations>?,
        doAction: () -> Unit
    ) {
        arrayDegrees.clear()

        body?.forEach {
            arrayDegrees.add(
                ItemSpinner(
                    id = it.id,
                    name = if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) it.valueEn else it.valueAr
                )
            )
        }

        adapterDegrees =
            AdapterCustomSpinner(context!!, arrayDegrees, AppConstants.SPINNER_TYPE_TEXT)

        spDegrees.adapter = adapterDegrees

        doAction()
        loading.visibility = View.GONE
    }

    private fun onSpecificationsRetrieved(
        body: java.util.ArrayList<ResponseLocations>?,
        doAction: () -> Unit
    ) {
        arraySpecifications.clear()

        body?.forEach {
            arraySpecifications.add(
                ItemSpinner(
                    id = it.id,
                    name = if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) it.valueEn else it.valueAr
                )
            )
        }

        adapterSpecifications =
            AdapterCustomSpinner(context!!, arraySpecifications, AppConstants.SPINNER_TYPE_TEXT)

        spSpecifications.adapter = adapterSpecifications

        doAction()
        loading.visibility = View.GONE
    }

    private fun saveUserEducation(toSave: UserEducation, dialog: Dialog) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.postUserEducation(
                toSave
            )?.enqueue(object : Callback<Any?> {
                override fun onResponse(
                    call: Call<Any?>,
                    response: Response<Any?>
                ) {
                    dialog.dismiss()
                    gotUserEducation = false
                    getUserEducation()
                }

                override fun onFailure(
                    call: Call<Any?>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun saveUserWork(toSave: UserWork, dialog: Dialog) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.postUserWork(
                toSave
            )?.enqueue(object : Callback<Any?> {
                override fun onResponse(
                    call: Call<Any?>,
                    response: Response<Any?>
                ) {
                    dialog.dismiss()
                    gotUserWork = false
                    getUserWork()
                }

                override fun onFailure(
                    call: Call<Any?>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun saveUserExtraCertification(toSave: UserExtraCertification, dialog: Dialog) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.postUserExtraCertification(
                toSave
            )?.enqueue(object : Callback<Any?> {
                override fun onResponse(
                    call: Call<Any?>,
                    response: Response<Any?>
                ) {
                    dialog.dismiss()
                    gotUserExtraCertification = false
                    getUserExtraCertification()
                }

                override fun onFailure(
                    call: Call<Any?>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun fillDate(
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        activeCalendar: Calendar,
        activeTextView: TextView
    ) {
        activeCalendar.set(Calendar.YEAR, year)
        activeCalendar.set(Calendar.MONTH, monthOfYear)
        activeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        activeTextView.text = AppHelper.dateEvent.format(activeCalendar.time)
    }

    private fun hideLoading() {
        if (loading.visibility == View.VISIBLE && gotUserEducation && gotUserWork && gotUserExtraCertification) {
            loading.visibility = View.GONE
        }
    }
}