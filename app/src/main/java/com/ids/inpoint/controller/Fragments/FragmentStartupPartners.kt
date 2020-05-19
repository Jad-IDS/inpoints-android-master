package com.ids.inpoint.controller.Fragments



import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.PermissionChecker
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
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
import com.ids.inpoint.controller.Activities.ActivityInsideComment
import com.ids.inpoint.controller.Activities.ActivityProfile
import com.ids.inpoint.controller.Adapters.*

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_startup_partners.*
import kotlinx.android.synthetic.main.fragment_startup_profile_info.*
import kotlinx.android.synthetic.main.fragment_startup_profile_product.*
import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.popup_partner_organization.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentStartupPartners : Fragment() ,RVOnItemClickListener {



    private var adapterPartners: AdapterStartupUsers?=null
    var arrayPartners= java.util.ArrayList<ResponseUser>()


    private var adapterSponsors: AdapterStartupUsers?=null
    var arraySponsors= java.util.ArrayList<ResponseUser>()


    private var adapterInvestors: AdapterStartupUsers?=null
    var arrayInvestors= java.util.ArrayList<ResponseUser>()


    private var dialog: Dialog? = null
    var btCloseDialog:LinearLayout ?= null
    var etUser:EditText ?= null
    var etWebsite:EditText ?= null
    var btCancel:TextView ?= null
    var btSaveChanges:LinearLayout ?= null
    var rvAllUsers:RecyclerView ?= null
    var linearImage:LinearLayout ?= null
    var linearWebsite:LinearLayout ?= null
    var ivPickUserImage:ImageView ?= null
    var selectedImage=""
    var selectedPartnerIds=-1
    var selectedInvestorIds=-1
    var selectedSponsorIds=-1
    var selecteOrganizationId=-1
    var userImage=""
    var arrayFilteredUsers= java.util.ArrayList<ResponseUser>()
    private var  pickImageType=0
    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002
    private val PERMISSION_GALLERY_WRITE = 1004
    var selecteImage=""
    lateinit var adapterAllUsers:AdapterAllUsers
    lateinit var adapterAllInvestor:AdapterAllUsers
    lateinit var adapterAllSponsor:AdapterAllUsers
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>
    lateinit var  myFile: File
    lateinit var body1: MultipartBody.Part

    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.linearItemUser) {
            selectedPartnerIds=adapterAllUsers.arrayUsers[position].id!!
           // arrayPartners.add(adapterAllUsers.arrayUsers[position])
            userImage=adapterAllUsers.arrayUsers[position].image!!
            etUser!!.setText(adapterAllUsers.arrayUsers[position].userName.toString())
            rvAllUsers!!.visibility=View.GONE
            linearImage!!.visibility=View.GONE
            linearWebsite!!.visibility=View.GONE
        }else  if(view.id==R.id.linearItemUserInvestor) {
            selectedInvestorIds=adapterAllInvestor.arrayUsers[position].id!!
         //   arrayInvestors.add(adapterAllInvestor.arrayUsers[position])
            userImage=adapterAllInvestor.arrayUsers[position].image!!
            etUser!!.setText(adapterAllInvestor.arrayUsers[position].userName.toString())
            rvAllUsers!!.visibility=View.GONE
            linearImage!!.visibility=View.GONE
            linearWebsite!!.visibility=View.GONE
        }
        else  if(view.id==R.id.linearItemUserSponsor) {
            userImage=adapterAllSponsor.arrayUsers[position].image!!
            selectedSponsorIds=adapterAllSponsor.arrayUsers[position].id!!
          //  arraySponsors.add(adapterAllSponsor.arrayUsers[position])
            etUser!!.setText(adapterAllSponsor.arrayUsers[position].userName.toString())
            rvAllUsers!!.visibility=View.GONE
            linearImage!!.visibility=View.GONE
            linearWebsite!!.visibility=View.GONE
        }else if(view.id==R.id.btDeleteA){
             deleteConfirm(activity!!,adapterPartners!!.arrayUsers[position].id!!)
        }else if(view.id==R.id.btDeleteB){
            deleteConfirm(activity!!,adapterInvestors!!.arrayUsers[position].id!!)
        }else if(view.id==R.id.btDeleteC){
            deleteConfirm(activity!!,adapterSponsors!!.arrayUsers[position].id!!)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_startup_partners, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getStartupUsers()
        getAllUsers(false,0)


    }

    private fun init(){
        rvPartners.isNestedScrollingEnabled=false
        rvSponsors.isNestedScrollingEnabled=false
        rvInvestors.isNestedScrollingEnabled=false

        linearAddPartners.setOnClickListener{showPoupPartners(AppConstants.PICK_IMAGE_PARTNER)}
        linearAddSponsor.setOnClickListener{showPoupPartners(AppConstants.PICK_IMAGE_SPONSOR)}
        linearAddInvestors.setOnClickListener{showPoupPartners(AppConstants.PICK_IMAGE_INVESTOR)}


    }



    private fun getStartupUsers(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupPartners(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                    try{ setInfo(response.body()!!)}catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setInfo(response: ArrayList<ResponseUser>) {

        arrayInvestors.clear()
        arrayPartners.clear()
        arraySponsors.clear()

        for (i in response.indices){
            when {
                response[i].Type==AppConstants.STARTUP_TYPE_PARTNER -> arrayPartners.add(response[i])
                response[i].Type==AppConstants.STARTUP_TYPE_INVESTOR -> arrayInvestors.add(response[i])
                response[i].Type==AppConstants.STARTUP_TYPE_SPONSOR -> arraySponsors.add(response[i])
            }
        }
        setpartners()
        setInvestors()
        setSponsors()




    }

    private fun setpartners(){
        if(arrayPartners.size==0){
           // li.visibility=View.GONE
            rvPartners.visibility=View.GONE
        }else {
           // tvPartnersTitle.visibility=View.VISIBLE
            rvPartners.visibility=View.VISIBLE
            adapterPartners =
                AdapterStartupUsers(arrayPartners, this, AppConstants.STARTUP_TYPE_PARTNER)
            val glm = GridLayoutManager(activity, 1)
            rvPartners!!.adapter = adapterPartners
            rvPartners!!.layoutManager = glm
        }
    }


    private fun setInvestors(){
        if(arrayInvestors.size==0){
           // tvInvestorstitle.visibility=View.GONE
            rvInvestors.visibility=View.GONE
        }else {
          //  tvInvestorstitle.visibility = View.VISIBLE
            rvInvestors.visibility = View.VISIBLE
            adapterInvestors= AdapterStartupUsers(arrayInvestors,this,AppConstants.STARTUP_TYPE_INVESTOR)
            val glm = GridLayoutManager(activity, 1)
            rvInvestors!!.adapter=adapterInvestors
            rvInvestors!!.layoutManager=glm}
    }


    private fun setSponsors(){
        if(arraySponsors.size==0){
           // tvSponsorsTitle.visibility=View.GONE
            rvSponsors.visibility=View.GONE
        }else {
            //tvSponsorsTitle.visibility = View.VISIBLE
            rvSponsors.visibility = View.VISIBLE
            adapterSponsors= AdapterStartupUsers(arraySponsors,this,AppConstants.STARTUP_TYPE_SPONSOR)
            val glm = GridLayoutManager(activity, 1)
            rvSponsors!!.adapter=adapterSponsors
            rvSponsors!!.layoutManager=glm}
    }





    private fun showPoupPartners(type:Int) {
        selectedPartnerIds=-1
        selectedInvestorIds=-1
        selectedSponsorIds=-1
        selectedImage=""
        userImage=""
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
        linearWebsite = dialog!!.findViewById<View>(R.id.linearWebsite) as LinearLayout

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

            var myImage=""
            myImage = if(selecteImage.isEmpty())
                userImage
            else
                selecteImage

            if(type==AppConstants.PICK_IMAGE_PARTNER){
                if(etUser!!.text.toString().isNotEmpty()){
                    saveStartupUser(ResponseUser(0,selectedPartnerIds,etUser!!.text.toString(),myImage,etWebsite!!.text.toString(),true,AppConstants.STARTUP_TYPE_PARTNER,MyApplication.selectedStartupTeam.id!!))
                    dialog!!.dismiss()


                }
                else
                    Toast.makeText(activity,getString(R.string.enter_partner), Toast.LENGTH_LONG).show()

            }else if(type==AppConstants.PICK_IMAGE_INVESTOR){

                if(etUser!!.text.toString().isNotEmpty()){
                    saveStartupUser(ResponseUser(0,selectedInvestorIds,etUser!!.text.toString(),myImage,etWebsite!!.text.toString(),true,AppConstants.STARTUP_TYPE_INVESTOR,MyApplication.selectedStartupTeam.id!!))
                    dialog!!.dismiss()
                }
               else
                    Toast.makeText(activity,getString(R.string.enter_investor), Toast.LENGTH_LONG).show()



            }
            else if(type==AppConstants.PICK_IMAGE_SPONSOR){

                if( etUser!!.text.toString().isNotEmpty()){
                    saveStartupUser(ResponseUser(0,selectedSponsorIds,etUser!!.text.toString(),myImage,etWebsite!!.text.toString(),true,AppConstants.STARTUP_TYPE_SPONSOR,MyApplication.selectedStartupTeam.id!!))
                    dialog!!.dismiss()
                }
               else
                    Toast.makeText(activity,getString(R.string.enter_sponsors), Toast.LENGTH_LONG).show()



            }


        }

        if(MyApplication.arrayUsers.size==0)
            getAllUsers(true,type)
        else {
            if(type==AppConstants.PICK_IMAGE_PARTNER)
                setAllUsers()
            else  if(type==AppConstants.PICK_IMAGE_SPONSOR)
                setAllUsersSponsor()
            else  if(type==AppConstants.PICK_IMAGE_INVESTOR)
                setAllUsersInvestors()

        }
        etUser!!.addTextChangedListener(object:
            TextWatcher {override fun afterTextChanged(s: Editable?) {



        }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUsers(s.toString(),type)
            }

        })
        dialog!!.show();

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
            else if(type==AppConstants.PICK_IMAGE_INVESTOR)
                adapterAllInvestor.notifyDataSetChanged()
            else if(type==AppConstants.PICK_IMAGE_SPONSOR)
                adapterAllSponsor.notifyDataSetChanged()



            if(word.isEmpty() || arrayFilteredUsers.size==0) {
                rvAllUsers!!.visibility = View.GONE
                if(type==AppConstants.PICK_IMAGE_PARTNER)
                    selectedPartnerIds=-1
                else
                    selecteOrganizationId=-1

                linearImage!!.visibility=View.VISIBLE
                linearWebsite!!.visibility=View.VISIBLE
            }else
                rvAllUsers!!.visibility=View.VISIBLE

        }
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
                            else if(type==AppConstants.PICK_IMAGE_INVESTOR)
                                setAllUsersInvestors()
                            else if(type==AppConstants.PICK_IMAGE_SPONSOR)
                                setAllUsersSponsor()
                        }

                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseUser>>, throwable: Throwable) {
                }
            })

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

    private fun openCamera(){
        //   etVideoUrl.visibility=View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (PermissionChecker.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED){
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (PermissionChecker.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE); }
            else if (PermissionChecker.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED){
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
                    if (PermissionChecker.checkSelfPermission(
                            this.activity!!,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED){
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


                        if(pickImageType==AppConstants.PICK_IMAGE_PARTNER || pickImageType==AppConstants.PICK_IMAGE_SPONSOR || pickImageType==AppConstants.PICK_IMAGE_INVESTOR){
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
             if(pickImageType==AppConstants.PICK_IMAGE_PARTNER || pickImageType==AppConstants.PICK_IMAGE_SPONSOR || pickImageType==AppConstants.PICK_IMAGE_INVESTOR ){
                try{AppHelper.setRoundImage(activity!!, ivPickUserImage!!, selecteImage, true)}catch (e:java.lang.Exception){}

            }
        }
    }



    private fun setAllUsers() {
        arrayFilteredUsers.clear()
        arrayFilteredUsers.addAll(MyApplication.arrayUsers)
        adapterAllUsers= AdapterAllUsers(arrayFilteredUsers,this,AppConstants.PICK_IMAGE_PARTNER)
        val glm = GridLayoutManager(activity, 1)
        rvAllUsers!!.adapter=adapterAllUsers
        rvAllUsers!!.layoutManager=glm
    }


    private fun setAllUsersSponsor() {
        arrayFilteredUsers.clear()
        arrayFilteredUsers.addAll(MyApplication.arrayUsers)
        adapterAllSponsor= AdapterAllUsers(arrayFilteredUsers,this,AppConstants.PICK_IMAGE_SPONSOR)
        val glm = GridLayoutManager(activity, 1)
        rvAllUsers!!.adapter=adapterAllSponsor
        rvAllUsers!!.layoutManager=glm
    }


    private fun setAllUsersInvestors() {
        arrayFilteredUsers.clear()
        arrayFilteredUsers.addAll(MyApplication.arrayUsers)
        adapterAllInvestor= AdapterAllUsers(arrayFilteredUsers,this,AppConstants.PICK_IMAGE_INVESTOR)
        val glm = GridLayoutManager(activity, 1)
        rvAllUsers!!.adapter=adapterInvestors
        rvAllUsers!!.layoutManager=glm
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








    private fun saveStartupUser(user: ResponseUser){
        var withImage=false
        arrayBody= arrayListOf()
        try {
            if (selecteImage.isNotEmpty() && (user.id==null || user.id==0 ||user.id==-1) ) {
                 myFile = this.saveBitmapToFile(File(selecteImage))!!
                 val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile)
                 body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, myFile.name, requestFile)
                 withImage=true

            }else{
                withImage=false
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "")
                body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, "", requestFile)
            }
        } catch (e: java.lang.Exception) {
            withImage=false
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, "", requestFile)
        }

        val jsonString = RequestBody.create(MediaType.parse("text/plain"), Gson().toJson(user))
        Log.wtf("save_post_json_rb",Gson().toJson(user))

       if(withImage){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveStartupdetails(jsonString,body1
            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                    try {
                        getStartupUsers()
                    }catch (e:java.lang.Exception){}
                }
                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                }
            })
        }else{
           RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
               ?.saveStartupdetails(jsonString
               )?.enqueue(object : Callback<ResponseUser> {
                   override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                       try {
                           getStartupUsers()
                       }catch (e:java.lang.Exception){}
                   }
                   override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                   }
               })

       }
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




    fun deleteConfirm(c: Activity,id: Int) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(getString(R.string.delete_confirmation))
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(c.getString(R.string.yes)){dialog, _->
                 deleteStartupUser(id)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun deleteStartupUser(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupUser(id)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{getStartupUsers()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }

}
