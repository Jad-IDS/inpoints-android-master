package com.ids.inpoint.controller.Fragments


import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.google.gson.Gson
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.PostVideo
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add_event.*

import kotlinx.android.synthetic.main.fragment_post.btAddVideo
import kotlinx.android.synthetic.main.fragment_post.etPostTitle
import kotlinx.android.synthetic.main.fragment_post.etVideoUrl
import kotlinx.android.synthetic.main.fragment_post.ivUserProfile
import kotlinx.android.synthetic.main.fragment_post.linearPhoto
import kotlinx.android.synthetic.main.fragment_post.linearPrivacy
import kotlinx.android.synthetic.main.fragment_post.linearUsersSpinner
import kotlinx.android.synthetic.main.fragment_post.linearVideo
import kotlinx.android.synthetic.main.fragment_post.linearVideoLinks
import kotlinx.android.synthetic.main.fragment_post.rvMediaImages
import kotlinx.android.synthetic.main.fragment_post.rvPostPrivacy
import kotlinx.android.synthetic.main.fragment_post.rvUsers
import kotlinx.android.synthetic.main.fragment_post.rvVideoLinks
import kotlinx.android.synthetic.main.fragment_post.scroll
import kotlinx.android.synthetic.main.fragment_post.spVideoType
import kotlinx.android.synthetic.main.fragment_post.tvPrivacySelected
import kotlinx.android.synthetic.main.fragment_post.tvUserSelected
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar_general.*
import kotlinx.android.synthetic.main.toolbar_general.btBack
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentCreateEvent : Fragment() , RVOnItemClickListener {
    lateinit var adapterPrivacy:AdapterSpinner
    lateinit var adapterUsers:AdapterSpinner
    lateinit var adapterAllUsers:AdapterAllUsers
    lateinit var adapterAllUsersOrganizer:AdapterAllUsers
    lateinit var adapterSelectedPartners:AdapterSelectedUsers
    lateinit var adapterSelectedOrganizer:AdapterSelectedUsers
    private var  pickImageType=0

    lateinit var adapterPickedImages:AdapterImagesPicked
    var arrayFilteredUsers= java.util.ArrayList<ResponseUser>()

    var arraySelectedPartners= java.util.ArrayList<ResponseUser>()
    var arraySelectedOrganizer= java.util.ArrayList<ResponseUser>()

    var arrayPartnersToSend= java.util.ArrayList<ResponseUser>()
    var arrayUserToSend= java.util.ArrayList<ResponseUser>()

    var arraySelectedFunding= java.util.ArrayList<Funding>()
    lateinit var adapterSelectedFunding:AdapterFunding

    var arraySelectedDates= java.util.ArrayList<Dates>()
    lateinit var adapterSelectedDates:AdapterDates

    var arraySelectedResources= java.util.ArrayList<Resources>()
    lateinit var adapterSelectedResources:AdapterSelectedResources

    private lateinit var fromTimeDialog: TimePickerDialog
    private var fromTime = ""
    private lateinit var fromTimeCalendar: Calendar

    private lateinit var toTimeDialog: TimePickerDialog
    private var toTime = ""
    private lateinit var toTimeCalendar: Calendar

    lateinit var adapterLinks:AdapterVideos
    var arraySelectedLinks=java.util.ArrayList<PostVideo>()

    var selectedPartnerIds=-1
    var selecteOrganizationId=-1


    var selectedVideoTypeId=0
    var postId=0
    var selectedVideoType=""
    var selectedVideoLink=""
    var selecteImage=""

    var selectedEventType=""
    var selectedEventTypeId=0

    private lateinit var fromdatelistener: DatePickerDialog.OnDateSetListener
    private lateinit var fromDateCalendar: Calendar
    private var selectedFromDate = ""

    private lateinit var toDateDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var toDateCalendar: Calendar
    private var selectedToDate = ""

    private var selectedFromTime=""
    private var selectedToTime=""

    private var itemSpinner1: ItemSpinner?=null
    var isPrivacyOpen=false
    var isSpUsersOpen=false
    var arrayMedia=java.util.ArrayList<PostMedia>()
    var arrayPostTypes= java.util.ArrayList<ItemSpinner>()
    var arrayUsers= java.util.ArrayList<ItemSpinner>()
    var selectedPostTypeId=0;
    var selectedUserId=0;

    private var dialog: Dialog? = null
    lateinit var  myFile: File
    lateinit var body1: MultipartBody.Part
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>

    var btCloseDialog:LinearLayout ?= null
    var etUser:EditText ?= null
    var etWebsite:EditText ?= null

    var etDescription:EditText ?= null
    var etAmount:EditText ?= null
    var spResourcesTypes:Spinner ?= null

    var rvAllUsers:RecyclerView ?= null
    var linearImage:LinearLayout ?= null
    var ivPickUserImage:ImageView ?= null
    var btCancel:TextView ?= null
    var btSaveChanges:LinearLayout ?= null

    var tvFromDate:TextView ?= null
    var tvToDate:TextView ?= null
    var tvFromTime:TextView ?= null
    var tvToTime:TextView ?= null

    var selectedImage=""


    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.IdSpinnerPrivacy) {
            selectedPostTypeId=adapterPrivacy.itemSpinner[position].id!!
            tvPrivacySelected.text = adapterPrivacy.itemSpinner[position].name
            rvPostPrivacy.visibility = View.GONE
            isPrivacyOpen = false
            checkEvent()
        }else   if(view.id==R.id.IdSpinnerUsers) {
            selectedUserId=adapterUsers.itemSpinner[position].id!!
            tvUserSelected.text = adapterUsers.itemSpinner[position].name
            rvUsers.visibility = View.GONE
            isSpUsersOpen = false
        }else  if(view.id==R.id.linearItemUser) {
            selectedPartnerIds=adapterAllUsers.arrayUsers[position].id!!
            arraySelectedPartners.add(adapterAllUsers.arrayUsers[position])
            etUser!!.setText(adapterAllUsers.arrayUsers[position].userName.toString())
            rvAllUsers!!.visibility=View.GONE
            linearImage!!.visibility=View.GONE
        }else  if(view.id==R.id.linearItemUserOrganizer) {
            selecteOrganizationId=adapterAllUsersOrganizer.arrayUsers[position].id!!
            arraySelectedOrganizer.add(adapterAllUsersOrganizer.arrayUsers[position])
            etUser!!.setText(adapterAllUsersOrganizer.arrayUsers[position].userName.toString())
            rvAllUsers!!.visibility=View.GONE
            linearImage!!.visibility=View.GONE
        }




        else if(view.id==R.id.btRemove){
            arrayMedia.removeAt(position)
            adapterPickedImages.notifyDataSetChanged()
        }else if(view.id==R.id.linearPickedImage){
            MyApplication.imageType=AppConstants.BOTTOM_SHEET_IMAGE_TYPE_MEDIA_POST
            MyApplication.selectedMedia=adapterPickedImages.itemMedia[position]
            val bottomSheetFragment = FragmentImageBottomSheet()
            this.fragmentManager.let { bottomSheetFragment.show(it, bottomSheetFragment.tag) }
        }else if(view.id==R.id.btRemoveLink){
            arraySelectedLinks.removeAt(position)
            adapterLinks.notifyDataSetChanged()
        }else if(view.id==R.id.ivRemoveUser){
            arraySelectedPartners.removeAt(position)
            adapterSelectedPartners.notifyDataSetChanged()
            if(arraySelectedPartners.size==0)
                rvPartners.visibility=View.GONE
        }else if(view.id==R.id.ivRemoveUserOrganizer){
            arraySelectedOrganizer.removeAt(position)
            adapterSelectedOrganizer.notifyDataSetChanged()
            if(arraySelectedOrganizer.size==0)
                rvOrganization.visibility=View.GONE
        }else if(view.id==R.id.ivRemoveFunding){
            arraySelectedFunding.removeAt(position)
            adapterSelectedFunding.notifyDataSetChanged()
            if(arraySelectedFunding.size==0)
                rvFundation.visibility=View.GONE
        }else if(view.id==R.id.ivEditFunding){
            showPopupFundings(position,
                adapterSelectedFunding.arrayFundings[position].Description!!, adapterSelectedFunding.arrayFundings[position].Amount!!
            )
        }
        else if(view.id==R.id.ivRemoveResources){
            arraySelectedResources.removeAt(position)
            adapterSelectedResources.notifyDataSetChanged()
            if(arraySelectedResources.size==0)
                rvResources.visibility=View.GONE
        }else if(view.id==R.id.ivEditResources){
            showPopupResources(position,
                adapterSelectedResources.arrayResources[position].Type!!, adapterSelectedResources.arrayResources[position].Description!!
            )
        }
        else if(view.id==R.id.ivRemovDates){
            arraySelectedDates.removeAt(position)
            adapterSelectedDates.notifyDataSetChanged()
            if(arraySelectedDates.size==0)
                rvDates.visibility=View.GONE
        }else if(view.id==R.id.ivEditDates){
            showPopupDates(position,
                adapterSelectedDates.arrayDates[position].FromDate!!,
                adapterSelectedDates.arrayDates[position].ToDate!!,
                adapterSelectedDates.arrayDates[position].FromTime!!,
                adapterSelectedDates.arrayDates[position].ToTime!!
            )
        }
    }

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002
    private val PERMISSION_GALLERY_WRITE = 1004
    private val IMAGE_DIRECTORY = "/inpoint"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_add_event, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }



    private fun init() {
        selectedUserId=MyApplication.userLoginInfo.id!!
        getPostType()
        getAllUsers(false,0)
        if(MyApplication.isProfileImageLocal)
            try{ AppHelper.setRoundImageResize(activity!!,ivUserProfile,MyApplication.userLoginInfo.image!!,true)}catch (E: java.lang.Exception){}
        else
            try{ AppHelper.setRoundImageResize(activity!!,ivUserProfile, AppConstants.IMAGES_URL+"/"+MyApplication.userLoginInfo.image!!,false)}catch (E: java.lang.Exception){}

        tvToolbarTitle.text = getString(R.string.create_an_event)
        tvToolbarTitle.visibility=View.GONE
       // tvToolbarTitle.setOnClickListener{sendPost()}

        btBack.setOnClickListener{
            activity!!.onBackPressed()
            //  try{(activity!! as ActivityHome).removeFrag(AppConstants.POST_FRAG)}catch (e:Exception){ }
        }

        linearPrivacy.setOnClickListener{
            if(isPrivacyOpen){
                rvPostPrivacy.visibility=View.GONE
                isPrivacyOpen=false
            }else{
                rvPostPrivacy.visibility=View.VISIBLE
                isPrivacyOpen=true
            }
        }

        linearUsersSpinner.setOnClickListener{
            if(isSpUsersOpen){
                rvUsers.visibility=View.GONE
                isSpUsersOpen=false
            }else{
                rvUsers.visibility=View.VISIBLE
                isSpUsersOpen=true
            }
        }


        linearPhoto.setOnClickListener{

            linearPhoto.setBackgroundResource(R.drawable.rectangular_post_active)
            linearVideo.setBackgroundResource(R.drawable.rectangular_post_button)
            //   etVideoUrl.visibility=View.GONE
            pickImageType=AppConstants.PICK_IMAGE_EVENT
            selectImage(activity!!)
            // scroll()
        }

        linearVideo.setOnClickListener{

            linearPhoto.setBackgroundResource(R.drawable.rectangular_post_button)
            linearVideo.setBackgroundResource(R.drawable.rectangular_post_active)
            //   etVideoUrl.visibility=View.GONE
            linearVideoLinks.visibility=View.VISIBLE
            scroll()
        }


        setUsersSpinner()
        setPickedImages()
        setVideoTypeSpinner()
        setVideoLinksRecycler()

        cbPublic.isChecked=true
        if(MyApplication.isPostEdit){
            Handler().postDelayed({
               setData()
            }, 200)
        }




        btAddVideo.setOnClickListener{
            if(etVideoUrl.text.toString().isEmpty())
                Toast.makeText(activity,"Please enter video link",Toast.LENGTH_LONG).show()
            else{
                arraySelectedLinks.add(PostVideo(0,selectedVideoTypeId,etVideoUrl.text.toString(),selectedVideoType))
                adapterLinks.notifyDataSetChanged()
                try{view?.let { activity?.hideKeyboard(it) }}catch (e: java.lang.Exception){}
                etVideoUrl.setText("")
                scroll()
            }
        }



        linearPartners.setOnClickListener{showPoupPartners(AppConstants.PICK_IMAGE_PARTNER)}
        linearOrganization.setOnClickListener{showPoupPartners(AppConstants.PICK_IMAGE_ORGANIZATION)}
        linearFundation.setOnClickListener{showPopupFundings(-1,"","")}
        linearDates.setOnClickListener{

            var dateNow = AppHelper.dateEvent.format(Date())
            val currentTime = SimpleDateFormat("HH:mm",  Locale.US).format(Date())
            showPopupDates(-1,dateNow,dateNow,currentTime,currentTime)
        }
        linearResources.setOnClickListener{showPopupResources(-1,AppConstants.EVENT_TYPE_COMPLETED,"")}

        btAdd.setOnClickListener{
            if(etPostTitle.text.toString().isEmpty())
                AppHelper.createDialog(activity!!,getString(R.string.enter_event_title))
            else if(etLocation.text.toString().isEmpty())
                AppHelper.createDialog(activity!!,getString(R.string.enter_location))
            else if(arraySelectedPartners.size==0)
                AppHelper.createDialog(activity!!,getString(R.string.enter_partner))

            else if(arraySelectedResources.size==0 && selectedPostTypeId==AppConstants.EVENT_TYPE_NEEDED)
                AppHelper.createDialog(activity!!,getString(R.string.enter_resources))
            else if(arraySelectedFunding.size==0 && selectedPostTypeId==AppConstants.EVENT_TYPE_NEEDED)
                AppHelper.createDialog(activity!!,getString(R.string.enter_funding))

            else if(arraySelectedOrganizer.size==0)
                AppHelper.createDialog(activity!!,getString(R.string.enter_organizers))
            else if(arraySelectedDates.size==0)
                AppHelper.createDialog(activity!!,getString(R.string.enter_dates))
            else
                saveEvent()
        }
    }


    private fun setVideoTypeSpinner(){
        val types = arrayOf(getString(R.string.youtube),getString(R.string.vimeo))
        //val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, types)
        val adapter = ArrayAdapter(activity, R.layout.spinner_text_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spVideoType.adapter = adapter;
        spVideoType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                if(position==0){
                    selectedVideoTypeId=AppConstants.MEDIA_TYPE_YOUTUBE
                    selectedVideoType=getString(R.string.youtube)
                }else if(position==1){
                    selectedVideoTypeId=AppConstants.MEDIA_TYPE_VIMEO
                    selectedVideoType=getString(R.string.vimeo)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }


    private fun setVideoLinksRecycler(){
        adapterLinks= AdapterVideos(arraySelectedLinks,this)
        val glm = GridLayoutManager(activity, 1)
        rvVideoLinks.adapter=adapterLinks
        rvVideoLinks.layoutManager=glm
        rvVideoLinks.isNestedScrollingEnabled=false
    }

    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.choose_gallery),
            getString(R.string.cancel)
           )

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.take_photo) -> openCamera()
                options[item] == getString(R.string.choose_gallery) -> openGallery()
                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun checkEvent(){
        if(selectedPostTypeId==AppConstants.EVENT_TYPE_NEEDED){
            linearFundation.visibility=View.VISIBLE
            linearResources.visibility=View.VISIBLE
            rvFundation.visibility=View.VISIBLE
            rvResources.visibility=View.VISIBLE
        }else{
            linearFundation.visibility=View.GONE
            linearResources.visibility=View.GONE
            rvFundation.visibility=View.GONE
            rvResources.visibility=View.GONE
        }
    }

    private fun scroll(){
        try{scroll.post { scroll.fullScroll(View.FOCUS_DOWN)  }}
        catch (e:Exception) {}
    }


    private fun setPickedImages(){
        adapterPickedImages= AdapterImagesPicked(arrayMedia,this,AppConstants.IMAGES_PICKED)
        val glm = GridLayoutManager(activity, 3)
        rvMediaImages.adapter=adapterPickedImages
        rvMediaImages.layoutManager=glm
        rvMediaImages.visibility=View.VISIBLE
        rvMediaImages.isNestedScrollingEnabled=false
    }

    private fun openCamera(){
        //   etVideoUrl.visibility=View.GONE
        scroll()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(this.activity!!,Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.CAMERA)
                requestPermissions(permissions, PERMISSION_CODE_CAMERA); }
            else
                pickImageFromCamera()
        }
        else
            pickImageFromCamera()
    }


    private fun openGallery(){
        //    etVideoUrl.visibility=View.GONE
        scroll()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(this.activity!!,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE); }
            else if (checkSelfPermission(this.activity!!,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_GALLERY_WRITE); }
            else
                pickImageFromGallery()
        }
        else
            pickImageFromGallery()

    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    private fun pickImageFromCamera(){

        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )
        if (pictureIntent.resolveActivity(activity!!.packageManager) != null) {
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }// Error occurred while creating the File

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity!!, "com.ids.inpoint.provider", photoFile)
                pictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoURI
                )
                startActivityForResult(
                    pictureIntent,
                    CAMERA_CODE
                )
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    if (checkSelfPermission(this.activity!!,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_GALLERY_WRITE); }
                    else
                        pickImageFromGallery()
                }
                else{
                    Toast.makeText(activity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_GALLERY_WRITE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(activity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromCamera()
                }
                else{
                    Toast.makeText(activity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){

            if (data == null) {
                Toast.makeText(activity, getString(R.string.unable_to_pick), Toast.LENGTH_LONG).show()
                return
            }
            if (!data.hasExtra("data")) {
                val selectedImageUri = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity!!.contentResolver.query(selectedImageUri, filePathColumn, null, null, null)

                // fromPhone = true
                if (cursor != null) {
                   try{
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    selecteImage = cursor.getString(columnIndex)
                    Log.wtf("image_file_path_1", selecteImage)

                    if(pickImageType==AppConstants.PICK_IMAGE_EVENT){
                    arrayMedia.add(PostMedia(-1,selecteImage,AppConstants.IMAGES_TYPE_GALLERY,true,selectedImageUri,null))
                    adapterPickedImages.notifyDataSetChanged()
                    }
                    else if(pickImageType==AppConstants.PICK_IMAGE_PARTNER || pickImageType==AppConstants.PICK_IMAGE_ORGANIZATION){
                       try{AppHelper.setRoundImage(activity!!, ivPickUserImage!!, selecteImage, true)}catch (e:java.lang.Exception){}
                    }
                 }catch (e: java.lang.Exception){
                       Toast.makeText(activity,getString(R.string.cannot_delete_image),Toast.LENGTH_LONG).show()
                   }
                }
                // checkToUpload()
            }






        }else  if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CODE){
//           val thumbnail = data!!.extras!!.get("data") as Bitmap
            Log.wtf("image_file_path",selecteImage)
            val file = File(selecteImage)
            if (file.exists())
                Log.wtf("image_file_path","exist")
            else
                Log.wtf("image_file_path","not exist")
            if(pickImageType==AppConstants.PICK_IMAGE_EVENT) {
                arrayMedia.add(PostMedia(-1, selecteImage, AppConstants.IMAGES_TYPE_CAMERA, true, null, null))
                //saveImage(thumbnail)
                adapterPickedImages.notifyDataSetChanged()
            }else if(pickImageType==AppConstants.PICK_IMAGE_PARTNER || pickImageType==AppConstants.PICK_IMAGE_ORGANIZATION){
                try{AppHelper.setRoundImage(activity!!, ivPickUserImage!!, selecteImage, true)}catch (e:java.lang.Exception){}

            }
        }
    }



    private fun getPostType(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPostTypes(AppConstants.TYPE_POST
            )?.enqueue(object : Callback<ArrayList<ResponsePostType>> {
                override fun onResponse(call: Call<ArrayList<ResponsePostType>>, response: Response<ArrayList<ResponsePostType>>) {
                    try{ onPostTypesRetrieved(response.body())}catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponsePostType>>, throwable: Throwable) {
                }
            })

    }

    private fun onPostTypesRetrieved(response: ArrayList<ResponsePostType>?) {

        arrayPostTypes.clear()


/*        for (i in response!!.indices) {
            if(MyApplication.languageCode==AppConstants.LANG_ENGLISH)
                arrayPostTypes.add(ItemSpinner(response[i].id, response[i].valueEn))
            else
                arrayPostTypes.add(ItemSpinner(response[i].id, response[i].valueAr))
        }*/

        arrayPostTypes.add(ItemSpinner(AppConstants.EVENT_TYPE_NEEDED,getString(R.string.funding_needed)))
        arrayPostTypes.add(ItemSpinner(AppConstants.EVENT_TYPE_COMPLETED,getString(R.string.funding_completed)))


        adapterPrivacy= AdapterSpinner(arrayPostTypes,this,AppConstants.SPINNER_POST_PRIVACY)
        val glm = GridLayoutManager(activity, 1)
        rvPostPrivacy.adapter=adapterPrivacy
        rvPostPrivacy.layoutManager=glm




        if(arrayPostTypes.size>0){
            if(MyApplication.isPostEdit){
                selectedPostTypeId = arrayPostTypes[getPositionFromId(MyApplication.selectedPost.type!!)].id!!
                tvPrivacySelected.text = arrayPostTypes[getPositionFromId(MyApplication.selectedPost.type!!)].name
            }else {
                selectedPostTypeId = arrayPostTypes[0].id!!
                tvPrivacySelected.text = arrayPostTypes[0].name
            }
            checkEvent()
        }


    }

    private fun setUsersSpinner() {
        arrayUsers.clear()
    /*    arrayUsers.add(ItemSpinner(MyApplication.userLoginInfo.id,MyApplication.userLoginInfo.userName))
        arrayUsers.add(ItemSpinner(-1,"user2"))
*/
        for(i in MyApplication.arrayUserTeams.indices){
            arrayUsers.add(ItemSpinner(MyApplication.arrayUserTeams[i].teamId,MyApplication.arrayUserTeams[i].name))
        }



        adapterUsers= AdapterSpinner(arrayUsers,this,AppConstants.SPINNER_POST_USERS)
        val glm = GridLayoutManager(activity, 1)
        rvUsers.adapter=adapterUsers
        rvUsers.layoutManager=glm


        if(arrayUsers.size>0){
            if(MyApplication.isPostEdit){
                selectedUserId = arrayUsers[getUserPositionFromId(MyApplication.selectedPost.userId!!)].id!!
                tvUserSelected.text = arrayUsers[getUserPositionFromId(MyApplication.selectedPost.userId!!)].name
            }else {
                selectedUserId = arrayUsers[0].id!!
                tvUserSelected.text = arrayUsers[0].name
            }
        }
    }


    private fun getRequestParameter():RequestEvent{
        var date = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

        var postArrayMedia= arrayListOf<RequestMedia>()
        for (i in arrayMedia.indices){
            if(arrayMedia[i].id!=-1)
                postArrayMedia.add(RequestMedia(arrayMedia[i].id,arrayMedia[i].fileName!!.substring(arrayMedia[i].fileName!!.lastIndexOf("/")+1),AppConstants.MEDIA_TYPE_IMAGE))
            else
                postArrayMedia.add(RequestMedia(arrayMedia[i].fileName!!.substring(arrayMedia[i].fileName!!.lastIndexOf("/")+1),AppConstants.MEDIA_TYPE_IMAGE))

        }


        for (i in arraySelectedLinks.indices)
            postArrayMedia.add(RequestMedia(arraySelectedLinks[i].MediaId,arraySelectedLinks[i].link,arraySelectedLinks[i].typeId))

        var id=null


        var request=RequestEvent(postId,
            etPostTitle.text.toString(),
            selectedPostTypeId,
            etDetails.text.toString(),
            etLocation.text.toString(),
            date,
            MyApplication.userLoginInfo.id,
            AppConstants.TYPE_EVENTS,
            //cbPublic.isChecked,
            true,
            cbPublic.isChecked,
            postArrayMedia,
            arraySelectedFunding,
            arraySelectedResources,
            getArrayPartnersOrganizers(arraySelectedOrganizer),
            getArrayPartnersOrganizers(arraySelectedPartners),
            arraySelectedDates

            )

        return request
    }

     private fun getArrayPartnersOrganizers(array:ArrayList<ResponseUser>):ArrayList<ResponseUser>?{
         arrayUserToSend.clear()
         arrayUserToSend.addAll(array)
         for(i in arrayUserToSend.indices){
             var id=arrayUserToSend[i].id
             arrayUserToSend[i].UserId=id
             arrayUserToSend[i].IsNew = id == -1
             if(arrayUserToSend[i].Website==null)
                 arrayUserToSend[i].Website=""
         }
         return arrayUserToSend
     }


    private fun createDialog(c: Activity, message: String) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ ->
                dialog.cancel()
                activity!!.onBackPressed()

            }
        val alert = builder.create()
        alert.show()
    }



    private fun getPositionFromId(id:Int): Int {
        var position=0
        for (i in arrayPostTypes.indices){
            if(MyApplication.selectedPost.type==id){
                position=i
                break
            }
        }
        return position
    }


    private fun getUserPositionFromId(id:Int): Int {
        var position=0
        for (i in arrayUsers.indices){
            if(MyApplication.selectedPost.userId==id){
                position=i
                break
            }
        }
        return position
    }


    fun saveBitmapToFile(file: File): File? {
        try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image

            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)

            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)

            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            return file
        } catch (e: java.lang.Exception) {
            return null
        }

    }



    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        selecteImage = image.absolutePath
        return image
    }

   private fun showPoupPartners(type:Int) {
        selectedPartnerIds=-1
        selecteOrganizationId=-1
        selectedImage=""
        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_partner_organization)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        rvAllUsers = dialog!!.findViewById<View>(R.id.rvAllUsers) as RecyclerView
        linearImage = dialog!!.findViewById<View>(R.id.linearImage) as LinearLayout
        ivPickUserImage = dialog!!.findViewById<View>(R.id.ivPickUserImage) as ImageView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout

        etUser = dialog!!.findViewById<View>(R.id.etUser) as EditText
        etWebsite = dialog!!.findViewById<View>(R.id.etWebsite) as EditText

        var tvPopupTitle= dialog!!.findViewById<View>(R.id.tvPopupTitle) as TextView
        var tvPopupTopTitle= dialog!!.findViewById<View>(R.id.tvPopupTopTitle) as TextView

       if(type==AppConstants.PICK_IMAGE_ORGANIZATION){
           etUser!!.hint = getString(R.string.organaization)
           tvPopupTitle.text=getString(R.string.organaization)
           tvPopupTopTitle.text=getString(R.string.organaization)
       }

        ivPickUserImage!!.setOnClickListener{
           pickImageType=type
           selectImage(activity!!)
        }



       btSaveChanges!!.setOnClickListener {

          if(type==AppConstants.PICK_IMAGE_PARTNER){
           if(selectedPartnerIds==-1 && etUser!!.text.toString().isNotEmpty()){
               arraySelectedPartners.add(ResponseUser(-1,etUser!!.text.toString(),selectedImage,etWebsite!!.text.toString(),true))
               setSelectedPartners()
               dialog!!.dismiss()
           }
           else if(etUser!!.text.toString().isNotEmpty()){
               if(etWebsite!!.text.toString().isNotEmpty()){
                   for(i in arraySelectedPartners.indices){
                       if(arraySelectedPartners[i].id==selectedPartnerIds) {
                           arraySelectedPartners[i].Website = etWebsite!!.text.toString()
                           arraySelectedPartners[i].id=selectedPartnerIds
                           arraySelectedPartners[i].IsNew=false
                       }
                   }
                   setSelectedPartners()
               }else
                   setSelectedPartners()
               dialog!!.dismiss()
           }else
               Toast.makeText(activity,getString(R.string.enter_partner),Toast.LENGTH_LONG).show()

           }else{

              if(selecteOrganizationId==-1 && etUser!!.text.toString().isNotEmpty()){
                  arraySelectedOrganizer.add(ResponseUser(-1,etUser!!.text.toString(),selectedImage,etWebsite!!.text.toString(),true))
                  setSelectedOrganizer()
                  dialog!!.dismiss()
              }
              else if(etUser!!.text.toString().isNotEmpty()){
                  if(etWebsite!!.text.toString().isNotEmpty()){
                      for(i in arraySelectedOrganizer.indices){
                          if(arraySelectedOrganizer[i].id==selecteOrganizationId) {
                              arraySelectedOrganizer[i].Website = etWebsite!!.text.toString()
                              arraySelectedOrganizer[i].id=selecteOrganizationId
                              arraySelectedOrganizer[i].IsNew=false
                          }
                      }
                      setSelectedOrganizer()
                  }else
                      setSelectedOrganizer()
                  dialog!!.dismiss()
              }else
                  Toast.makeText(activity,getString(R.string.enter_organizers),Toast.LENGTH_LONG).show()



          }


       }

       if(MyApplication.arrayUsers.size==0)
           getAllUsers(true,type)
       else {
           if(type==AppConstants.PICK_IMAGE_PARTNER)
              setAllUsers()
           else
               setAllUsersOrganizer()
       }
       etUser!!.addTextChangedListener(object: TextWatcher {override fun afterTextChanged(s: Editable?) {



       }

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterUsers(s.toString(),type)
           }

       })
        dialog!!.show();

    }



    private fun addOldUser(){
       setSelectedPartners()
    }


    private fun filterUsers(word:String,type: Int){
        if(MyApplication.arrayUsers.size>0) {
            arrayFilteredUsers.clear()
            for (i in MyApplication.arrayUsers.indices) {
               if(MyApplication.arrayUsers[i].userName!!.contains(word) && MyApplication.arrayUsers[i].id!=MyApplication.userLoginInfo.id)
                   arrayFilteredUsers.add(MyApplication.arrayUsers[i])
            }

            if(type==AppConstants.PICK_IMAGE_PARTNER)
                adapterAllUsers.notifyDataSetChanged()
            else
                adapterAllUsersOrganizer.notifyDataSetChanged()


            if(word.isEmpty() || arrayFilteredUsers.size==0) {
                rvAllUsers!!.visibility = View.GONE
                if(type==AppConstants.PICK_IMAGE_PARTNER)
                   selectedPartnerIds=-1
                else
                    selecteOrganizationId=-1

                linearImage!!.visibility=View.VISIBLE
            }else
                rvAllUsers!!.visibility=View.VISIBLE

        }
    }



   private fun showPopupFundings(position: Int,description:String,amount:String) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_funding)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout
        etDescription = dialog!!.findViewById<View>(R.id.etDescription) as EditText
        etAmount = dialog!!.findViewById<View>(R.id.etAmount) as EditText

       if(position!=-1){
           etDescription!!.setText(description)
           etAmount!!.setText(amount)
       }

       btSaveChanges!!.setOnClickListener {
           when {
               etAmount!!.text.toString().isEmpty() -> Toast.makeText(activity,"Please enter amount",Toast.LENGTH_LONG).show()
               etDescription!!.text.toString().isEmpty() -> Toast.makeText(activity,"Please enter description",Toast.LENGTH_LONG).show()
               else -> {
                   if(position==-1) {
                       arraySelectedFunding.add(Funding(0, etDescription!!.text.toString(), etAmount!!.text.toString()))

                   }else{
                       arraySelectedFunding[position].Description= etDescription!!.text.toString()
                       arraySelectedFunding[position].Amount= etAmount!!.text.toString()
                       adapterSelectedFunding.notifyDataSetChanged()
                  }
                   setSelectedFundings()
                   dialog!!.dismiss()
               }
           }


       }






        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }


    private fun showPopupResources(position: Int,type:Int,description:String) {


        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_resources)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout
        etDescription = dialog!!.findViewById<View>(R.id.etDescription) as EditText
        spResourcesTypes= dialog!!.findViewById<View>(R.id.spResourcesTypes) as Spinner

        if(position!=-1){
            etDescription!!.setText(description)
            selectedEventTypeId=type
/*            if(type==AppConstants.EVENT_TYPE_COMPLETED)
                spResourcesTypes!!.setSelection(0)
            else
                spResourcesTypes!!.setSelection(1)*/
        }else
            selectedEventTypeId=AppConstants.EVENT_TYPE_COMPLETED

        setResourcesTypeSpinner(type)
        btSaveChanges!!.setOnClickListener {
            when {
                etDescription!!.text.toString().isEmpty() -> Toast.makeText(activity,getString(R.string.enter_description),Toast.LENGTH_LONG).show()
                else -> {
                    Log.wtf("event_id_final",selectedEventTypeId.toString())
                    if(position==-1) {
                        arraySelectedResources.add(Resources(0, etDescription!!.text.toString(), selectedEventTypeId))

                    }else{
                        arraySelectedResources[position].Description= etDescription!!.text.toString()
                        arraySelectedResources[position].Type= selectedEventTypeId
                        adapterSelectedResources.notifyDataSetChanged()
                    }
                    setSelectedResources()
                    dialog!!.dismiss()
                }
            }


        }

        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    private fun showPopupDates(position: Int,fromDate:String,toDate:String,fromTime:String,toTime:String) {



        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_date)
        dialog!!.setCancelable(true)
        btCloseDialog = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        btCloseDialog!!.setOnClickListener { dialog!!.dismiss() }
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as TextView
        btSaveChanges = dialog!!.findViewById<View>(R.id.btSaveChanges) as LinearLayout

        tvFromTime = dialog!!.findViewById<View>(R.id.tvFromTime) as TextView
        tvToTime = dialog!!.findViewById<View>(R.id.tvToTime) as TextView
        tvFromDate = dialog!!.findViewById<View>(R.id.tvFromDate) as TextView
        tvToDate = dialog!!.findViewById<View>(R.id.tvToDate) as TextView
        setDateTimeListeners(position,fromDate,toDate,fromTime,toTime)
        tvFromTime!!.setOnClickListener{fromTimeDialog.show()}
        tvToTime!!.setOnClickListener{toTimeDialog.show()}


        btSaveChanges!!.setOnClickListener {
            when {


                tvFromDate!!.text.toString().isEmpty() -> Toast.makeText(activity,"Please enter From date",Toast.LENGTH_LONG).show()
                tvToDate!!.text.toString().isEmpty() -> Toast.makeText(activity,"Please enter To date",Toast.LENGTH_LONG).show()
                tvFromTime!!.text.toString().isEmpty() -> Toast.makeText(activity,"Please enter From time",Toast.LENGTH_LONG).show()
                tvToTime!!.text.toString().isEmpty() -> Toast.makeText(activity,"Please enter To Time",Toast.LENGTH_LONG).show()

                else -> {
                    if(position==-1) {
                        arraySelectedDates.add(Dates(0, tvFromDate!!.text.toString(), tvToDate!!.text.toString(),selectedFromTime,selectedToTime))
                   }else{
                        arraySelectedDates[position].FromDate= tvFromDate!!.text.toString()
                        arraySelectedDates[position].ToDate= tvToDate!!.text.toString()
                        arraySelectedDates[position].FromTime= selectedFromTime
                        arraySelectedDates[position].ToTime= selectedToTime
                        adapterSelectedDates.notifyDataSetChanged()
                    }
                    setSelectedDates()
                    dialog!!.dismiss()
                }
            }


        }

        btCancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }



    private fun setDateTimeListeners(position: Int,fromDate:String,toDate:String,fromTime:String,toTime:String){


        fromDateCalendar = Calendar.getInstance()
        fromdatelistener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            fromDateCalendar.set(Calendar.YEAR, year)
            fromDateCalendar.set(Calendar.MONTH, monthOfYear)
            fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tvFromDate!!.text = AppHelper.dateEvent.format(fromDateCalendar.time)
            selectedFromDate = AppHelper.dateEvent.format(fromDateCalendar.time)
        }
        tvFromDate!!.setOnClickListener{
            DatePickerDialog(activity!!, fromdatelistener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }


        toDateCalendar = Calendar.getInstance()
        toDateDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            toDateCalendar.set(Calendar.YEAR, year)
            toDateCalendar.set(Calendar.MONTH, monthOfYear)
            toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tvToDate!!.text = AppHelper.dateEvent.format(toDateCalendar.time)
            selectedToDate = AppHelper.dateEvent.format(toDateCalendar.time)
        }
        tvToDate!!.setOnClickListener{
            DatePickerDialog(activity!!, toDateDateListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }



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

                tvFromTime!!.text = hoursShown+":"+minutesShown+" "+amPm
                selectedFromTime= hoursShown+":"+minutesShown
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

                tvToTime!!.text = hoursShown+":"+minutesShown+" "+amPm
                selectedToTime= hoursShown+":"+minutesShown
            }, toTimeCalendar.get(Calendar.HOUR_OF_DAY),  toTimeCalendar.get(Calendar.MINUTE), false
        )

    //  if(position!=-1){
          tvFromDate!!.text=fromDate
          tvToDate!!.text=toDate
          tvFromTime!!.text=fromTime
          tvToTime!!.text=toTime
          selectedToTime=toTime
          selectedFromTime=fromTime
    //  }


    }

    private fun setResourcesTypeSpinner(type:Int){
        var firstime=true
        val types = arrayOf(getString(R.string.type_completed),getString(R.string.type_needed))
        val adapter = ArrayAdapter(activity, R.layout.spinner_text_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spResourcesTypes!!.adapter = adapter;
        spResourcesTypes!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                Log.wtf("position_event",position.toString())

               if(!firstime) {
                   if (position == 0) {
                       selectedEventTypeId = AppConstants.EVENT_TYPE_COMPLETED
                       selectedEventType = getString(R.string.type_completed)
                   } else if (position == 1) {
                       selectedEventTypeId = AppConstants.EVENT_TYPE_NEEDED
                       selectedEventType = getString(R.string.type_needed)
                   }
               }
                firstime=false
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

        if(type==AppConstants.EVENT_TYPE_COMPLETED)
            spResourcesTypes!!.setSelection(0)
        else
            spResourcesTypes!!.setSelection(1)



    }


    private fun setData(){
        try{etPostTitle.setText(MyApplication.selectedPost.title)}catch (e:java.lang.Exception){}
        try{etDetails.setText(MyApplication.selectedPost.details)}catch (e:java.lang.Exception){}
        try{etLocation.setText(MyApplication.selectedPost.location)}catch (e:java.lang.Exception){}
        try{cbPublic.isChecked=MyApplication.selectedPost.isPublic!!}catch (e:java.lang.Exception){}
        postId=MyApplication.selectedPost.id!!
        for (i in MyApplication.selectedPost.medias!!.indices){
            if(MyApplication.selectedPost.medias!![i].fileType==AppConstants.MEDIA_TYPE_IMAGE)
                arrayMedia.add(PostMedia(MyApplication.selectedPost.medias!![i].mediaId,MyApplication.selectedPost.medias!![i].fileName ,MyApplication.selectedPost.medias!![i].fileType,false,null,null))
            else
                arraySelectedLinks.add(PostVideo(MyApplication.selectedPost.medias!![i].mediaId!!,MyApplication.selectedPost.medias!![i].fileType,MyApplication.selectedPost.medias!![i].fileName,""))
        }
        adapterPickedImages.notifyDataSetChanged()
        if(arraySelectedLinks.size>0){
            adapterLinks.notifyDataSetChanged()
            linearVideoLinks.visibility=View.VISIBLE
        }

        getEventFunds()
        getOrganizers()
        getPartners()
        getResourcesData()
        getDates()

    }

    private fun setAllUsers() {
        arrayFilteredUsers.clear()
        arrayFilteredUsers.addAll(MyApplication.arrayUsers)
        adapterAllUsers= AdapterAllUsers(arrayFilteredUsers,this,AppConstants.PICK_IMAGE_PARTNER)
        val glm = GridLayoutManager(activity, 1)
        rvAllUsers!!.adapter=adapterAllUsers
        rvAllUsers!!.layoutManager=glm
    }

    private fun setAllUsersOrganizer() {
        arrayFilteredUsers.clear()
        arrayFilteredUsers.addAll(MyApplication.arrayUsers)
        adapterAllUsersOrganizer= AdapterAllUsers(arrayFilteredUsers,this,AppConstants.PICK_IMAGE_ORGANIZATION)
        val glm = GridLayoutManager(activity, 1)
        rvAllUsers!!.adapter=adapterAllUsersOrganizer
        rvAllUsers!!.layoutManager=glm
    }


    private fun setSelectedPartners() {

        adapterSelectedPartners= AdapterSelectedUsers(arraySelectedPartners,this,AppConstants.PICK_IMAGE_PARTNER)
        val glm = GridLayoutManager(activity, 1)
        rvPartners!!.adapter=adapterSelectedPartners
        rvPartners!!.layoutManager=glm
        if(arraySelectedPartners.size>0){
            rvPartners.visibility=View.VISIBLE
        }else
            rvPartners.visibility=View.GONE
    }


    private fun setSelectedFundings() {

        adapterSelectedFunding= AdapterFunding(arraySelectedFunding,this,0)
        val glm = GridLayoutManager(activity, 1)
        rvFundation!!.adapter=adapterSelectedFunding
        rvFundation!!.layoutManager=glm
        if(arraySelectedFunding.size>0){
            rvFundation.visibility=View.VISIBLE
        }else
            rvFundation.visibility=View.GONE
    }



    private fun setSelectedDates() {

        adapterSelectedDates= AdapterDates(arraySelectedDates,this,0)
        val glm = GridLayoutManager(activity, 1)
        rvDates!!.adapter=adapterSelectedDates
        rvDates!!.layoutManager=glm
        if(arraySelectedDates.size>0){
            rvDates.visibility=View.VISIBLE
        }else
            rvDates.visibility=View.GONE
    }

    private fun setSelectedResources() {

        adapterSelectedResources= AdapterSelectedResources(arraySelectedResources,this,0)
        val glm = GridLayoutManager(activity, 1)
        rvResources!!.adapter=adapterSelectedResources
        rvResources!!.layoutManager=glm
        if(arraySelectedResources.size>0){
            rvResources.visibility=View.VISIBLE
        }else
            rvResources.visibility=View.GONE
    }

    private fun setSelectedOrganizer() {

        adapterSelectedOrganizer= AdapterSelectedUsers(arraySelectedOrganizer,this,AppConstants.PICK_IMAGE_ORGANIZATION)
        val glm = GridLayoutManager(activity, 1)
        rvOrganization!!.adapter=adapterSelectedOrganizer
        rvOrganization!!.layoutManager=glm
        if(arraySelectedOrganizer.size>0){
            rvOrganization.visibility=View.VISIBLE
        }else
            rvOrganization.visibility=View.GONE
    }


    private fun getAllUsers(action:Boolean,type:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getAllUsers()?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                    try {
                        MyApplication.arrayUsers.clear()
                        MyApplication.arrayUsers.addAll(response.body()!!)
                        if(action) {
                            if(type==AppConstants.PICK_IMAGE_PARTNER)
                              setAllUsers()
                            else
                                setAllUsersOrganizer()
                        }

                      }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                }
            })

    }







    private fun saveEvent(){
        loading.visibility=View.VISIBLE
        arrayBody= arrayListOf()
        try {
            if (arrayMedia.size>0) {
                for(i in arrayMedia.indices) {
                    if(arrayMedia[i].id==null || arrayMedia[i].id==0 ||arrayMedia[i].id==-1) {
                        myFile = this.saveBitmapToFile(File(arrayMedia[i].fileName))!!
                        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile)
                        body1 = MultipartBody.Part.createFormData(ApiParameters.MEDIA, myFile.name, requestFile)
                        arrayBody.add(body1)
                        Log.wtf("save_post_body", myFile.name)
                    }

                }
            }

        } catch (e: java.lang.Exception) {

        }

        val jsonString = RequestBody.create(MediaType.parse("text/plain"), Gson().toJson(getRequestParameter()))
        Log.wtf("save_event_json_rb",jsonString.toString())
        Log.wtf("save_event_json",Gson().toJson(getRequestParameter()))

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveEvent(jsonString,arrayBody
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{ onEventSaved()}catch (e:java.lang.Exception){Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.VISIBLE
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onEventSaved(){

            if(MyApplication.isPostEdit)
                createDialog(activity!!, "Edited successfully")
            else
                createDialog(activity!!, "Added successfully")

        //  Toast.makeText(activity,response!!.title,Toast.LENGTH_LONG).show()
    }


    private fun getEventFunds(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getEventFunding(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<ArrayList<Funding>> {
                override fun onResponse(call: Call<ArrayList<Funding>>, response: Response<ArrayList<Funding>>) {
                    try{
                        arraySelectedFunding.clear()
                        arraySelectedFunding.addAll(response.body()!!)
                        setSelectedFundings()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<Funding>>, throwable: Throwable) {
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getOrganizers(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getOrganizers(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                    try{
                        arraySelectedOrganizer.clear()
                        arraySelectedOrganizer.addAll(response.body()!!)
                        setSelectedOrganizer()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun getPartners(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getPartners(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                    try{
                        arraySelectedPartners.clear()
                        arraySelectedPartners.addAll(response.body()!!)
                        setSelectedPartners()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getDates(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getEventDates(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<ArrayList<Dates>> {
                override fun onResponse(call: Call<ArrayList<Dates>>, response: Response<ArrayList<Dates>>) {
                    try{
                        arraySelectedDates.clear()
                        arraySelectedDates.addAll(response.body()!!)
                        setSelectedDates()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<Dates>>, throwable: Throwable) {
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun getResourcesData(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getResources(MyApplication.selectedPost.id!!
            )?.enqueue(object : Callback<ArrayList<Resources>> {
                override fun onResponse(call: Call<ArrayList<Resources>>, response: Response<ArrayList<Resources>>) {
                    try{
                        arraySelectedResources.clear()
                        arraySelectedResources.addAll(response.body()!!)
                        setSelectedResources()
                    }catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<Resources>>, throwable: Throwable) {
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })

    }

}