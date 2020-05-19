package com.ids.inpoint.controller.Activities




import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentManager
import android.support.v4.content.FileProvider
import android.support.v4.content.PermissionChecker
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.google.gson.Gson
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterImagesPicked

import com.ids.inpoint.controller.Adapters.AdapterTeamFiles
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.controller.MyApplication.Companion.arrayFiles
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.PostMedia

import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import kotlinx.android.synthetic.main.activity_team_files.*

import kotlinx.android.synthetic.main.loading_trans.*
import kotlinx.android.synthetic.main.toolbar_general.*


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


class ActivityTeamFiles : AppCompactBase(),RVOnItemClickListener {

    private var adapterFiles: AdapterTeamFiles? = null

    private var arrayFilesFiltered = java.util.ArrayList<ResponseTeamFile>()
    private var downloadManager: DownloadManager? = null
    private var currentPosition = 0
    val PERMISSION_WRITE = 3425
    var selectedUrl=""

    var arrayMedia=java.util.ArrayList<PostMedia>()
    var arrayPostTypes= java.util.ArrayList<ItemSpinner>()
    lateinit var adapterPickedImages: AdapterImagesPicked
    lateinit var  myFile: File
    lateinit var body1: MultipartBody.Part
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>
    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val FILE_PICK_CODE = 1004
    private val AUDIO_PICK_CODE = 1005
    private val VIDEO_PICK_CODE = 1009
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002

    private val PERMISSION_CODE_AUDIO = 1006
    private val PERMISSION_CODE_FILE = 1007
    private val PERMISSION_CODE_VIDEO = 1008
    private var fileId=0
    private var fileName=""
    private val IMAGE_DIRECTORY = "/inpoint"
    var selecteImage=""


    override fun onItemClicked(view: View, position: Int) {
        when {
            view.id== R.id.btDownload -> {
                selectedUrl=AppConstants.IMAGES_URL+adapterFiles!!.arrayFiles[position].name
                startDownload(selectedUrl);
            }
            view.id==R.id.btEdit -> {
                editSelectedFile(adapterFiles!!.arrayFiles[position].id!!,position)
            }
            view.id==R.id.btDelete -> {
                deleteSelectedFile(adapterFiles!!.arrayFiles[position].id!!,position)
            }
            view.id==R.id.btRemove ->{
                arrayMedia.removeAt(position)
                adapterPickedImages.notifyDataSetChanged()
                checkArray()
            }
            view.id==R.id.linearPickedImage ->{
                /*          MyApplication.imageType=AppConstants.BOTTOM_SHEET_IMAGE_TYPE_MEDIA_POST
                          MyApplication.selectedMedia=adapterPickedImages.itemMedia[position]
                          val bottomSheetFragment = FragmentImageBottomSheet()
                          this.fragmentManager.let { bottomSheetFragment.show(it, bottomSheetFragment.tag) }*/
            }
        }
    }

    private fun deleteSelectedFile(id:Int,position: Int){
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(getString(R.string.delete_confirmation)+" \n "+ adapterFiles!!.arrayFiles[position].description)
            .setCancelable(true)
            .setNegativeButton(getString(R.string.no)) {
                    dialog, _ -> dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                deleteMyFile(adapterFiles!!.arrayFiles[position].id!!,position)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun editSelectedFile(id:Int,position: Int){
        val builder = AlertDialog.Builder(this)
        val edittext = EditText(this)
        edittext.setSingleLine();
        edittext.setBackgroundResource(R.drawable.bg_recangular_border_gray)
        edittext.setText(adapterFiles!!.arrayFiles[position].description)
        edittext.setPadding(20,20,20,20)
        var params =
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.leftMargin=20
        params.rightMargin=20
        params.topMargin=10
        params.bottomMargin=10

        //  AppHelper.setMargins(this,edittext,120,80,120,80)
        edittext.layoutParams=params
        builder.setView(edittext)
        builder
            .setMessage("Edit File Description")
            .setCancelable(true)
            .setNegativeButton(getString(R.string.cancel)) {
                    dialog, _ -> dialog.cancel()
            }
            .setPositiveButton(getString(R.string.save_changes)) { dialog, _ ->
                if(edittext.text.toString().isNotEmpty()) {
                    fileId=id
                    fileName=edittext.text.toString()
                    arrayMedia.clear()
                    saveFile()
                    dialog.cancel()
                }else{
                    Toast.makeText(applicationContext,getString(R.string.file_name_required),Toast.LENGTH_LONG).show()
                }
            }
        val alert = builder.create()
        alert.show()
    }


    private lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ids.inpoint.R.layout.activity_team_files)
        init()

    }


    private fun init(){
        supportActionBar!!.hide()
        fragmentManager = supportFragmentManager
        btBack.visibility= View.VISIBLE
        setlisteners()
        getTeamFiles()
        setPickedImages()

        btCancelFiles.setOnClickListener{
            arrayMedia.clear()
            etFilesDescription.setText("")
            linearSelectedFiles.visibility=View.GONE
        }

        btSaveChanges.setOnClickListener{
            when {
                arrayMedia.size==0 -> AppHelper.createDialog(this,getString(R.string.pick_file))
                etFilesDescription.text.toString().isEmpty() -> AppHelper.createDialog(this,getString(R.string.enter_file_desc))
                else -> {fileName=etFilesDescription.text.toString()
                    saveFile()}
            }
        }
        checkArray()
        btUploadFiles.setOnClickListener{
            selectFile(this)
        }


        etSearchFile!!.addTextChangedListener(object: TextWatcher {override fun afterTextChanged(s: Editable?) {

        }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                filterFiles(s.toString())
            }

        })

        btClose.setOnClickListener{
            btClose.visibility=View.GONE
            etSearchFile.setText("")
            adapterFiles!!.notifyDataSetChanged()
            try{hideKeyboard(it) }catch (e: java.lang.Exception){}
        }

/*        try{
            AppHelper.setRoundImageResize(this,ivToolbarProfile,AppConstants.IMAGES_URL+"/"+ MyApplication.userLoginInfo.image!!,
                MyApplication.isProfileImageLocal) }catch (E:java.lang.Exception){}*/

    }


    private fun filterFiles(word:String){
        if(arrayFiles.size>0 && word.isNotEmpty()){
            btClose.visibility=View.VISIBLE
            arrayFilesFiltered.clear()
            for(i in arrayFiles.indices){
                if(arrayFiles[i].name!!.contains(word) || arrayFiles[i].description!!.contains(word))
                    arrayFilesFiltered.add(arrayFiles[i])
            }
        }else {
            arrayFilesFiltered.clear()
            btClose.visibility=View.GONE
            arrayFilesFiltered.addAll(arrayFiles)
        }
        adapterFiles!!.notifyDataSetChanged()
    }

    private fun setlisteners(){
        btBack.setOnClickListener{this.onBackPressed()}

    }


    private fun checkArray(){
        if(arrayMedia.size>0){
            linearSelectedFiles.visibility=View.VISIBLE
        }else
            linearSelectedFiles.visibility=View.GONE
    }


    private fun getTeamFiles(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamFiles(
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseTeamFile>> {
                override fun onResponse(call: Call<ArrayList<ResponseTeamFile>>, response: Response<ArrayList<ResponseTeamFile>>) {
                    try{
                        onFilesRetrieved(response.body())

                    }catch (E: Exception){}
                }
                override fun onFailure(call: Call<ArrayList<ResponseTeamFile>>, throwable: Throwable) {
                }
            })
    }


    private fun onFilesRetrieved(body: ArrayList<ResponseTeamFile>?) {
        try {
            arrayMedia.clear()
            adapterPickedImages.notifyDataSetChanged()
            checkArray()
        }catch (e:Exception){}

        arrayFiles.clear()
        arrayFilesFiltered.clear()
        arrayFiles.addAll(body!!)
        arrayFilesFiltered.addAll(body)


        adapterFiles = AdapterTeamFiles(arrayFilesFiltered, this, 1)
        val glm = GridLayoutManager(this, 1)
        rvFiles.adapter = adapterFiles
        rvFiles.layoutManager = glm
    }


    private fun deleteMyFile(id:Int,position:Int){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteFile(
                id
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try{
                        if(response.isSuccessful){
                            arrayFilesFiltered.removeAt(position)
                            adapterFiles!!.notifyDataSetChanged()
                        }else{
                            //  AppHelper.createDialog(this@ActivityTeamFiles,response.errorBody()!!.string().replace("\"", ""))
                            AppHelper.createDialog(this@ActivityTeamFiles,getString(R.string.cannot_delete_file))

                        }


                    }catch (E: Exception){
                        AppHelper.createDialog(this@ActivityTeamFiles,getString(R.string.cannot_delete_file))

                    }
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                }
            })
    }

    override
    fun onResume() {
        super.onResume()
        downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        this.registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        this.registerReceiver(
            onNotificationClick,
            IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        )
    }

    override
    fun onPause() {
        super.onPause()
        this.unregisterReceiver(onComplete)
        this.unregisterReceiver(onNotificationClick)
    }


    override
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickAudioFile()
                }
                else{
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_VIDEO -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickVideoFile()
                }
                else{
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_FILE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickFile()
                }
                else{
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromCamera()
                }
                else{
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_WRITE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        startDownload(selectedUrl)
                    else
                        Toast.makeText(
                            application,
                            getString(R.string.download_failed),
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }




    }



    fun startDownload(url: String) {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (!AppHelper.hasPermissions(this, *permissions))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, PERMISSION_WRITE)
            }
            else {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs()
                val urlSplit = url.trim { it <= ' ' }.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                Toast.makeText(application, getString(R.string.downloading), Toast.LENGTH_LONG).show()
                //lastDownload =
                downloadManager!!.enqueue(
                    DownloadManager.Request(Uri.parse(url))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(urlSplit[urlSplit.size - 1])
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            urlSplit[urlSplit.size - 1]
                        )
                )
            }
    }

    internal var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            Toast.makeText(ctxt, getString(R.string.download_complete), Toast.LENGTH_LONG).show()
        }
    }

    internal var onNotificationClick: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            Toast.makeText(ctxt, getString(R.string.downloading), Toast.LENGTH_LONG).show()
        }
    }

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    fun onPageSelected(position: Int) {
        currentPosition = position

    }

    fun onPageScrollStateChanged(state: Int) {

    }

    fun onPageClicked(v: View) {

    }





    private fun selectFile(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.choose_gallery),
            getString(R.string.choose_audio),
            getString(R.string.choose_video),
            getString(R.string.choose_other_file),
            getString(R.string.cancel))

        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.choose_audio))

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.take_photo) -> openCamera()
                options[item] == getString(R.string.choose_gallery) -> openGallery()
                options[item] == getString(R.string.choose_audio) -> openAudio()
                options[item] == getString(R.string.choose_video) -> openVideo()
                options[item] == getString(R.string.choose_other_file) -> openFile()
                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun setPickedImages(){
        adapterPickedImages= AdapterImagesPicked(arrayMedia,this,AppConstants.IMAGES_PICKED)
        val glm = GridLayoutManager(this, 3)
        rvSelectedMedia.adapter=adapterPickedImages
        rvSelectedMedia.layoutManager=glm
        rvSelectedMedia.visibility=View.VISIBLE
        rvSelectedMedia.isNestedScrollingEnabled=false
    }


    private fun openCamera(){
        //   etVideoUrl.visibility=View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
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
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE); }
            else
                pickImageFromGallery()
        }
        else
            pickImageFromGallery()

    }




    private fun openAudio(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE_AUDIO); }
            else
                pickAudioFile()
        }
        else
            pickAudioFile()

    }




    private fun openVideo(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE_AUDIO); }
            else
                pickVideoFile()
        }
        else
            pickVideoFile()

    }


    private fun openFile(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE_FILE); }
            else
                pickFile()
        }
        else
            pickFile()

    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }



    private fun pickAudioFile() {
        val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        // intent.type = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, AUDIO_PICK_CODE)


    }


    private fun pickVideoFile() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        // intent.type = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, VIDEO_PICK_CODE)

    }

    private fun pickFile() {
        //val intent = Intent(Intent.ACTION_GET_CONTENT)
        //intent.type = "*/*"
        //startActivityForResult(intent, FILE_PICK_CODE)

        //  openFile("*/*")
        val intent = Intent()
        //sets the select file to all types of files
        intent.type = "*/*"
        //allows to select data and return it
        intent.action = Intent.ACTION_GET_CONTENT
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), FILE_PICK_CODE)

    }


    fun openFile(mimeType: String) {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = mimeType
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // special intent for Samsung file manager
        val sIntent = Intent("com.sec.android.app.myfiles.PICK_DATA")
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", mimeType)
        sIntent.addCategory(Intent.CATEGORY_DEFAULT)

        val chooserIntent: Intent
        if (packageManager.resolveActivity(sIntent, 0) != null) {
            // it is device with Samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file")
        }

        try {
            startActivityForResult(chooserIntent, FILE_PICK_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "No suitable File Manager was found.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun pickImageFromCamera(){

        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )
        if (pictureIntent.resolveActivity(packageManager) != null) {
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }// Error occurred while creating the File

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "com.ids.inpoint.provider", photoFile)
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && (requestCode == IMAGE_PICK_CODE || requestCode == FILE_PICK_CODE || requestCode == AUDIO_PICK_CODE  || requestCode == VIDEO_PICK_CODE)){

            if (data == null) {

                Toast.makeText(applicationContext, getString(R.string.unable_to_pick), Toast.LENGTH_LONG).show()
                return
            }
            if (!data.hasExtra("data")) {
                var selectedImageUri = data.data
                var filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                var cursor:Cursor?=null
                when (requestCode) {
                    AUDIO_PICK_CODE -> {

                        filePathColumn = arrayOf(MediaStore.Audio.Media.DATA)
                        cursor = contentResolver.query(selectedImageUri, null, null, null, null)

                    }
                    FILE_PICK_CODE -> {





                        filePathColumn = arrayOf(MediaStore.Files.FileColumns.MIME_TYPE)
                        cursor = contentResolver.query(selectedImageUri, filePathColumn, null, null, null)


                        //testing
                        /*       selecteImage = "test"
                               arrayMedia.add(PostMedia(-1,selecteImage,AppConstants.FILE_TYPE,true,selectedImageUri,null))
                               adapterPickedImages.notifyDataSetChanged()
                               checkArray()*/
                        //testing
                    }

                    VIDEO_PICK_CODE -> {
                        filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
                        cursor = contentResolver.query(selectedImageUri, filePathColumn, null, null, null)

                    }
                    else -> {
                        filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        cursor = contentResolver.query(selectedImageUri, filePathColumn, null, null, null)
                    }
                }
                // fromPhone = true
                if (cursor != null) {
                    try{

                        if(requestCode == AUDIO_PICK_CODE) {
                            cursor.moveToFirst()
                            selecteImage = cursor.getString(cursor.getColumnIndex("_data"))
                        }
                        else if(requestCode==VIDEO_PICK_CODE) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
                            selecteImage = cursor.getString(columnIndex)
                        }
                        else if(requestCode==FILE_PICK_CODE) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            selecteImage = cursor.getString(columnIndex)
                            //  Toast.makeText(applicationContext,selectedImageUri.toString(),Toast.LENGTH_LONG).show()
                        }
                        else {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            selecteImage = cursor.getString(columnIndex)
                        }



                        Log.wtf("image_file_path_1", selecteImage)
                        var type=0
                        if(requestCode == AUDIO_PICK_CODE)
                            type=AppConstants.AUDIO_TYPE
                        else if(requestCode==FILE_PICK_CODE)
                            type=AppConstants.FILE_TYPE
                        else if(requestCode==VIDEO_PICK_CODE)
                            type=AppConstants.VIDEO_TYPE
                        else
                            type=AppConstants.IMAGES_TYPE_GALLERY

                        arrayMedia.add(PostMedia(-1,selecteImage,type,true,selectedImageUri,null))
                        adapterPickedImages.notifyDataSetChanged()
                        checkArray()
                    }catch (e: Exception){
                        Toast.makeText(applicationContext,"cannot pick file",Toast.LENGTH_LONG).show()
                    }
                }else{
                    // Toast.makeText(applicationContext,"cannot pick file",Toast.LENGTH_LONG).show()

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

            arrayMedia.add(PostMedia(-1,selecteImage,AppConstants.IMAGES_TYPE_CAMERA,true,null,null))
            //saveImage(thumbnail)
            adapterPickedImages.notifyDataSetChanged()
            checkArray()
        }
    }





    fun getRealPathFromURI(context: Context, uri: Uri): String {
        val result: String
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor == null) {
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
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
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        selecteImage = image.absolutePath
        return image
    }



    private fun saveFile(){
        loading.visibility=View.VISIBLE
        arrayBody= arrayListOf()
        try {
            if (arrayMedia.size>0) {
                for(i in arrayMedia.indices) {
                    if(arrayMedia[i].id==null || arrayMedia[i].id==0 ||arrayMedia[i].id==-1) {
                        myFile = this.saveBitmapToFile(File(arrayMedia[i].fileName))!!
                        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile)
                        body1 = MultipartBody.Part.createFormData((i+1).toString(), myFile.name, requestFile)
                        arrayBody.add(body1)
                        Log.wtf("save_post_body", myFile.name)
                    }

                }
            }
        } catch (e: java.lang.Exception) {

        }

        val jsonString = RequestBody.create(MediaType.parse("text/plain"), Gson().toJson(getRequestParameter()))
        Log.wtf("save_post_json_rb",jsonString.toString())
        Log.wtf("save_post_json",Gson().toJson(getRequestParameter()))

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveFile(jsonString,arrayBody
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{
                        if(response.isSuccessful){
                            fileName=""
                            fileId=0
                            try{ getTeamFiles()}catch (e:Exception){}

                        }
                    }


                    catch (e:java.lang.Exception){

                        Toast.makeText(applicationContext,getString(R.string.failed),Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.VISIBLE
                    Toast.makeText(applicationContext,getString(R.string.failed),Toast.LENGTH_LONG).show()
                }
            })
    }



    private fun getRequestParameter(): RequestSaveFile {

        return RequestSaveFile(
            fileId,
            MyApplication.selectedStartupTeam.id!!,
            fileName
        )
    }


    fun getVideoPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }

}
