package com.ids.inpoint.controller.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterImagesPicked
import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.AdapterVideos
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.PostVideo
import com.ids.inpoint.model.response.RequestMedia
import com.ids.inpoint.model.response.RequestPost
import com.ids.inpoint.model.response.ResponsePost
import com.ids.inpoint.model.response.ResponsePostType
import com.ids.inpoint.utils.*
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard

import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.ivUserProfile
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


class FragmentPost : Fragment() , RVOnItemClickListener {
    lateinit var adapterPrivacy:AdapterSpinner
    lateinit var adapterUsers:AdapterSpinner

    lateinit var adapterPickedImages:AdapterImagesPicked

    lateinit var adapterLinks:AdapterVideos
    var arraySelectedLinks=java.util.ArrayList<PostVideo>()

    var selectedVideoTypeId=0
    var postId=0
    var selectedVideoType=""
    var selectedVideoLink=""
    var selecteImage=""

    private var itemSpinner1: ItemSpinner?=null
    var isPrivacyOpen=false
    var isSpUsersOpen=false
    var arrayMedia=java.util.ArrayList<PostMedia>()
    var arrayPostTypes= java.util.ArrayList<ItemSpinner>()
    var arrayUsers= java.util.ArrayList<ItemSpinner>()
    var selectedPostTypeId=0;
    var selectedUserId=0;

    lateinit var  myFile: File
    lateinit var body1: MultipartBody.Part
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>

    override fun onItemClicked(view: View, position: Int) {
       if(view.id==R.id.IdSpinnerPrivacy) {
           selectedPostTypeId=adapterPrivacy.itemSpinner[position].id!!
           tvPrivacySelected.text = adapterPrivacy.itemSpinner[position].name
           rvPostPrivacy.visibility = View.GONE
           isPrivacyOpen = false
       }else   if(view.id==R.id.IdSpinnerUsers) {
           selectedUserId=adapterUsers.itemSpinner[position].id!!
           tvUserSelected.text = adapterUsers.itemSpinner[position].name
           rvUsers.visibility = View.GONE
           isSpUsersOpen = false
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
        inflater.inflate(R.layout.fragment_post, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }



    private fun init() {
        selectedUserId=MyApplication.userLoginInfo.id!!
        getPostType()
        if(MyApplication.isProfileImageLocal)
            try{ AppHelper.setRoundImageResize(activity!!,ivUserProfile,MyApplication.userLoginInfo.image!!,true)}catch (E: java.lang.Exception){}
        else
            try{ AppHelper.setRoundImageResize(activity!!,ivUserProfile, AppConstants.IMAGES_URL+"/"+ MyApplication.userLoginInfo.image!!,false)}catch (E: java.lang.Exception){}

   /*     tvToolbarTitle.text = getString(R.string.post)
        tvToolbarTitle.setOnClickListener{sendPost()}*/
        tvToolbarTitle.visibility=View.GONE
        btAdd.setOnClickListener{sendPost()}
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

        if(MyApplication.isPostEdit){
            Handler().postDelayed({
                setData()
            }, 200)
        }




        btAddVideo.setOnClickListener{
            if(etVideoUrl.text.toString().isEmpty())
                Toast.makeText(activity,getString(R.string.enter_video_link),Toast.LENGTH_LONG).show()
            else{
                arraySelectedLinks.add(PostVideo(0,selectedVideoTypeId,etVideoUrl.text.toString(),selectedVideoType))
                adapterLinks.notifyDataSetChanged()
                try{view?.let { activity?.hideKeyboard(it) }}catch (e: java.lang.Exception){}
                etVideoUrl.setText("")
                scroll()
            }
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
        val options = arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.choose_gallery), getString(R.string.cancel))

        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.choose_your_profile))

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.take_photo) -> openCamera()
                options[item] == getString(R.string.choose_gallery) -> openGallery()
                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
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
                    arrayMedia.add(PostMedia(-1,selecteImage,AppConstants.IMAGES_TYPE_GALLERY,true,selectedImageUri,null))
                    adapterPickedImages.notifyDataSetChanged()
                   }catch (e: java.lang.Exception){
                        Toast.makeText(activity,getString(R.string.cannot_pick_image),Toast.LENGTH_LONG).show()
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

            arrayMedia.add(PostMedia(-1,selecteImage,AppConstants.IMAGES_TYPE_CAMERA,true,null,null))
            //saveImage(thumbnail)
            adapterPickedImages.notifyDataSetChanged()
        }
    }



    fun Uri.getName(context: Context): String {
        val returnCursor = context.contentResolver.query(this, null, null, null, null)
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val fileName = returnCursor.getString(nameIndex)
        returnCursor.close()
        return fileName
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
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
           for (i in response!!.indices) {
               if(MyApplication.languageCode==AppConstants.LANG_ENGLISH)
                  arrayPostTypes.add(ItemSpinner(response[i].id, response[i].valueEn))
               else
                  arrayPostTypes.add(ItemSpinner(response[i].id, response[i].valueAr))
           }

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
        }


    }

    private fun setUsersSpinner() {
        arrayUsers.clear()

        for(i in MyApplication.arrayUserTeams.indices){
            arrayUsers.add(ItemSpinner(MyApplication.arrayUserTeams[i].teamId,MyApplication.arrayUserTeams[i].name))
        }


        //arrayUsers.add(ItemSpinner(-1,"user2"))

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


    private fun getRequestParameter():RequestPost{
        var date = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

        var postArrayMedia= arrayListOf<RequestMedia>()
        for (i in arrayMedia.indices)
            postArrayMedia.add(RequestMedia(arrayMedia[i].id,arrayMedia[i].fileName!!.substring(arrayMedia[i].fileName!!.lastIndexOf("/")+1),AppConstants.MEDIA_TYPE_IMAGE))

        for (i in arraySelectedLinks.indices)
            postArrayMedia.add(RequestMedia(arraySelectedLinks[i].MediaId,arraySelectedLinks[i].link,arraySelectedLinks[i].typeId))

        var id=null


        var request=RequestPost(postId,
            etPostTitle.text.toString(),
            selectedPostTypeId,
            etPostText.text.toString(),
            date,
            selectedUserId,
            AppConstants.TYPE_POST,
            true,
            postArrayMedia )

        return request
    }


    private fun sendPost(){
        if(etPostTitle.text.toString().isEmpty() && etPostText.text.toString().isEmpty())
            Toast.makeText(activity,getString(R.string.enter_post_info),Toast.LENGTH_LONG).show()
        else
            savePost()
    }

    private fun savePost(){
        loading.visibility=View.VISIBLE
        arrayBody= arrayListOf()
        try {
            if (arrayMedia.size>0) {
                for(i in arrayMedia.indices) {
                    if(arrayMedia[i].id==null || arrayMedia[i].id==0 ||arrayMedia[i].id==-1) {
                        myFile = this.saveBitmapToFile(File(arrayMedia[i].fileName))!!
                        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile)
                        body1 = MultipartBody.Part.createFormData(ApiParameters.IMAGE, myFile.name, requestFile)
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
            ?.savePost(jsonString,arrayBody
            )?.enqueue(object : Callback<ResponsePost> {
                override fun onResponse(call: Call<ResponsePost>, response: Response<ResponsePost>) {
                    loading.visibility=View.GONE
                    try{ onPostSaved(response.body())}catch (e:java.lang.Exception){Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()}
                }
                override fun onFailure(call: Call<ResponsePost>, throwable: Throwable) {
                    loading.visibility=View.VISIBLE
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })
   }


    private fun onPostSaved(response: ResponsePost?){
        if(response!!.id!=null) {
            if(MyApplication.isPostEdit)
               createDialog(activity!!, getString(R.string.edited_successfully))
            else
                createDialog(activity!!, getString(R.string.added_successfully))
        }
      //  Toast.makeText(activity,response!!.title,Toast.LENGTH_LONG).show()
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

    private fun setData(){
        etPostTitle.setText(MyApplication.selectedPost.title)
        etPostText.setText(MyApplication.selectedPost.details)

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

}