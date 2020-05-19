package com.ids.inpoint.controller.Fragments



import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.support.v4.content.PermissionChecker
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterMediaGallery

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.response.ResponseMedia
import com.ids.inpoint.utils.*
import kotlinx.android.synthetic.main.fragment_upload_image.*
import kotlinx.android.synthetic.main.fragment_upload_image.btBack
import kotlinx.android.synthetic.main.fragment_upload_image.ivUserProfile
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentUploadImage : Fragment() ,RVOnItemClickListener {
    lateinit var adapterMediaGallery:AdapterMediaGallery
    var arrayMedia= java.util.ArrayList<ResponseMedia>()
    var selectedImageId=0
    var selecteImage=""
    var InitialSelectedImage=""

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val CHOOSER_CODE = 1004
    private val PERMISSION_WRITE = 1005
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002
    private val IMAGE_DIRECTORY = "/inpoint"
    lateinit var  myFile: File
    lateinit var body1: MultipartBody.Part
    var fromPhone=false
    var fromCamera=false
    var imageFilePath=""


    private var myId=0
    private var myImage=""


    override fun onItemClicked(view: View, position: Int) {
        fromPhone=false
        try{ AppHelper.setRoundImage(activity!!,ivUserProfile,AppConstants.IMAGES_URL+"/"+arrayMedia[position].fileName,false)}catch (E:Exception){}

        selectedImageId=arrayMedia[position].id!!
        selecteImage=arrayMedia[position].fileName!!
        checkToUpload()


        //updateProfileImage(arrayMedia[position].fileName!!)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_upload_image, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        Handler().postDelayed({
            getMediaGallery()
        }, 150)
        super.onResume()


    }

    private fun init(){
        if(MyApplication.isTeamChat){
            myId=MyApplication.selectedStartupTeam.id!!
            myImage=MyApplication.selectedStartupTeam.image!!
        }else{
            myId=MyApplication.userLoginInfo.id!!
            myImage=MyApplication.userLoginInfo.image!!
        }


        try{ AppHelper.setRoundImage(activity!!,ivUserProfile,AppConstants.IMAGES_URL+"/"+ myImage,false)}catch (E:Exception){}
        btBack.setOnClickListener{activity!!.onBackPressed()}
        InitialSelectedImage=myImage
        btUploadImage.setOnClickListener{
           if(!fromPhone) {
               MyApplication.isProfileImageLocal=false
              if(MyApplication.isTeamChat)
                  MyApplication.selectedStartupTeam.image=selecteImage
               else
                  MyApplication.userLoginInfo.image=selecteImage

               updateProfileImage()
           }
            else {
                MyApplication.isProfileImageLocal=true
                uploadImage()
               if(MyApplication.isTeamChat)
                    MyApplication.selectedStartupTeam.image=selecteImage
               else
                   MyApplication.userLoginInfo.image=selecteImage
           }


            btUploadImage.visibility=View.GONE
            InitialSelectedImage=selecteImage
        }
        checkToUpload()
        ivUserProfile.setOnClickListener{openChooser()}

    }


    private fun checkToUpload(){
      // if(!fromPhone) {
           if (selecteImage.isEmpty() || selecteImage == InitialSelectedImage)
               btUploadImage.visibility = View.GONE
           else
               btUploadImage.visibility = View.VISIBLE
    /*   }else{
           uploadImage()
       }*/
    }


    private fun getMediaGallery(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getProfileGallery(myId
            )?.enqueue(object : Callback<ArrayList<ResponseMedia>> {
                override fun onResponse(call: Call<ArrayList<ResponseMedia>>, response: Response<ArrayList<ResponseMedia>>) {
                  try{setMediaGallery(response.body()!!)}
                  catch (E:Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseMedia>>, throwable: Throwable) {
                    Toast.makeText(activity,"failed", Toast.LENGTH_LONG).show()
                }
            })
    }


    private fun setMediaGallery(response :ArrayList<ResponseMedia>){
        arrayMedia.clear()
        arrayMedia=response
        adapterMediaGallery= AdapterMediaGallery(arrayMedia,this,1)
        val glm = GridLayoutManager(activity, 3)
        rvProfileImages.adapter=adapterMediaGallery
        rvProfileImages.layoutManager=glm
    }


    private fun updateProfileImage(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updateProfileImage(selecteImage,myId
            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!MyApplication.isTeamChat)
                       MyApplication.userLoginInfo.image=selecteImage
                    else
                        MyApplication.selectedStartupTeam.image=selecteImage

                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show()
                }
            })
    }








    private fun openChooser(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when {
                PermissionChecker.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED -> {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE); }
                PermissionChecker.checkSelfPermission(this.activity!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED -> {
                    val permissions = arrayOf(Manifest.permission.CAMERA)
                    requestPermissions(permissions, PERMISSION_CODE_CAMERA); }
                PermissionChecker.checkSelfPermission(this.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED -> {
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_WRITE); }
                else -> pickFromChooser()
            }
        }
        else
            pickFromChooser()

    }



    private fun pickFromChooser(){
         selectImage(activity!!)
     }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    private fun pickImageFromCamera() {
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
                    PERMISSION_CODE_CAMERA
                )
            }
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    if (PermissionChecker.checkSelfPermission(this.activity!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.CAMERA)
                        requestPermissions(permissions, PERMISSION_CODE_CAMERA); }
                    else if (PermissionChecker.checkSelfPermission(this.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_WRITE); }
                    else
                        pickFromChooser()
                }
                else{
                    Toast.makeText(activity, getString(R.string.allow_permission_image), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_CAMERA -> {

                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    if (PermissionChecker.checkSelfPermission(this.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_WRITE); }
                    else
                        pickFromChooser()
                }
                else{
                    Toast.makeText(activity, getString(R.string.allow_permission_image), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PERMISSION_CODE_CAMERA){
               Log.wtf("image_file_path",selecteImage)
               val file = File(selecteImage)
               if (file.exists())
                 Log.wtf("image_file_path","exist")
               else
                 Log.wtf("image_file_path","not exist")
               AppHelper.setRoundImage(activity!!, ivUserProfile, selecteImage, true)
            fromPhone = true
            checkToUpload()
        }
      else  if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            if (data == null) {
                Toast.makeText(activity, "Unable to pick image ", Toast.LENGTH_LONG).show()
                return
            }
            if (!data.hasExtra("data")) {
                val selectedImageUri = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity!!.contentResolver.query(selectedImageUri, filePathColumn, null, null, null)

                fromPhone = true
                if (cursor != null) {
                   try {
                       cursor.moveToFirst()
                       val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                       selecteImage = cursor.getString(columnIndex)
                       Log.wtf("image_file_path_1", selecteImage)
                       AppHelper.setRoundImage(activity!!, ivUserProfile, selecteImage, true)
                   }catch (e:Exception){
                       Toast.makeText(activity,getString(R.string.cannot_pick_image),Toast.LENGTH_LONG).show()
                   }
                }
                checkToUpload()
            }
        }
}


    private fun uploadImage() {
        try {
            if (selecteImage.isNotEmpty()) {
                myFile = this.saveBitmapToFile(File(selecteImage))!!

                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile)
                body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, myFile.name, requestFile)
            } else {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "")
                body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, "", requestFile)
            }
        } catch (e: Exception) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, "", requestFile)
        }

        val userId = RequestBody.create(MediaType.parse("text/plain"), myId.toString())
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.uploadProfileImage(
                userId, body1
            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
                }
            })
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


    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(getString(R.string.take_photo),
            getString(R.string.choose_gallery),
            getString(R.string.cancel))

        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.choose_profile_pic))

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.take_photo) -> pickImageFromCamera()
                options[item] == getString(R.string.choose_gallery) -> pickImageFromGallery()
                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
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
        } catch (e: Exception) {
            return null
        }

    }
}
