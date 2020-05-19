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
import android.support.v4.content.PermissionChecker
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import com.google.gson.Gson
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.*
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.IFragmentImages

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.PostVideo
import com.ids.inpoint.model.response.*
import com.ids.inpoint.utils.*
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard


import kotlinx.android.synthetic.main.fragment_startup_profile_product.*
import kotlinx.android.synthetic.main.loading_trans.*
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


class FragmentStartupProfileProduct : Fragment() ,RVOnItemClickListener ,ViewPager.OnPageChangeListener,
    IFragmentImages {
    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
    }

    override fun onPageClicked(v: View) {
    }


    var vpMedia: ViewPager?= null
    private var dialog: Dialog? = null
    private var adapterProducts: AdapterStartupProducts?=null
    var arrayProducts= java.util.ArrayList<ResponseStartupProduct>()

    private var btCancel: LinearLayout?= null
    private var btSave: LinearLayout?= null
    private var btClose: LinearLayout?= null
    private var linearPhoto: LinearLayout?= null
    private var linearVideo: LinearLayout?= null
    private var etName: EditText?= null
    private var etDescription: EditText?= null
    private var rvMediaImages: RecyclerView?= null
    private var rvVideoLinks: RecyclerView?= null
    private var linearVideoLinks: LinearLayout?= null
    private var spVideoType: Spinner?= null
    private var btAddVideo: LinearLayout?= null
    private var etVideoUrl: EditText?= null
    private var loadingDialog: LinearLayout?= null
    private var isEdit=false

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002
    private val PERMISSION_GALLERY_WRITE = 1004
    private val IMAGE_DIRECTORY = "/inpoint"
    lateinit var adapterPickedImages:AdapterImagesPicked
    lateinit var adapterLinks:AdapterVideos
    var arraySelectedLinks=java.util.ArrayList<PostVideo>()
    var selectedVideoTypeId=0
    var productId=0
    var selectedVideoType=""
    var selectedVideoLink=""
    var selecteImage=""
    var arrayMedia=java.util.ArrayList<PostMedia>()

    lateinit var  myFile: File
    lateinit var body1: MultipartBody.Part
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>


    override fun onItemClicked(view: View, position: Int) {
       if(view.id==R.id.btView){
           showMediaPopup(adapterProducts!!.items[position].medias!!)
       }else if(view.id==R.id.btRemove){
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
       }else if(view.id==R.id.btEdit){

           var arrayPostMedia= java.util.ArrayList<PostMedia>()

          if(adapterProducts!!.items.size>0) {

              for (i in adapterProducts!!.items[position].medias!!.indices) {
                  try{
                  arrayPostMedia.add(

                      PostMedia(
                          adapterProducts!!.items[position].medias!![i].mediaId,
                          adapterProducts!!.items[position].medias!![i].fileName,
                          adapterProducts!!.items[position].medias!![i].fileType,
                          false

                      )

                  )}catch (e:Exception){
                      Log.wtf("exception",e.toString())
                  }


              }

          }

           addEditProduct(
               adapterProducts!!.items[position].id!!,
               adapterProducts!!.items[position].name!!,
               adapterProducts!!.items[position].description!!,
               arrayPostMedia

           )

       }else if(view.id==R.id.btDelete){
           deleteConfirm(activity!!, adapterProducts!!.items[position].id!!)
       }



    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_startup_profile_product, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getStartupProduct()

    }

    private fun init(){

      linearAddProducts.setOnClickListener{addEditProduct(0,"","", arrayListOf())}

    }



    private fun getStartupProduct(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getStartupProducts(MyApplication.selectedStartupTeam.id!!)?.enqueue(object : Callback<ArrayList<ResponseStartupProduct>> {
                override fun onResponse(call: Call<ArrayList<ResponseStartupProduct>>, response: Response<ArrayList<ResponseStartupProduct>>) {
                    try{ setInfo(response.body()!!)}catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseStartupProduct>>, throwable: Throwable) {
                    // Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            })

    }


    private fun setInfo(response: ArrayList<ResponseStartupProduct>) {

        arrayProducts.clear()
        arrayProducts.addAll(response)

        adapterProducts= AdapterStartupProducts(activity!!,arrayProducts,this)
        val glm = GridLayoutManager(activity, 1)
        rvProducts!!.adapter=adapterProducts
        rvProducts!!.layoutManager=glm

    }


    private fun setMediaPager(arrayMedia:ArrayList<Media>){
        val adapterImages = AdapterPopupMedia(arrayMedia, this)
        vpMedia!!.adapter = adapterImages
        vpMedia!!.addOnPageChangeListener(this)

        // vpLessonsMedia.currentItem = intent.getIntExtra(AppConstants.IMAGE_POSITION, 0)
    }




    fun showMediaPopup(arrayMedia: ArrayList<Media>) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_pager_media)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        vpMedia = dialog!!.findViewById<View>(R.id.vpMedia) as ViewPager
        setMediaPager(arrayMedia)
        dialog!!.show()

    }





    fun addEditProduct(id:Int,name:String,description:String,arrayMedia: ArrayList<PostMedia>) {

        dialog = Dialog(activity, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_startup_add_product)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(false)
        etName = dialog!!.findViewById<View>(R.id.etName) as EditText
        etDescription = dialog!!.findViewById<View>(R.id.etDescription) as EditText
        btCancel = dialog!!.findViewById<View>(R.id.btCancel) as LinearLayout
        btSave = dialog!!.findViewById<View>(R.id.btSave1) as LinearLayout
        btClose = dialog!!.findViewById<View>(R.id.ivClose) as LinearLayout
        linearPhoto =dialog!!.findViewById<View>(R.id.linearPhoto) as LinearLayout
        linearVideo =dialog!!.findViewById<View>(R.id.linearVideo) as LinearLayout
        rvMediaImages =dialog!!.findViewById<View>(R.id.rvMediaImages) as RecyclerView
        rvVideoLinks =dialog!!.findViewById<View>(R.id.rvVideoLinks) as RecyclerView
        linearVideoLinks=dialog!!.findViewById<View>(R.id.linearVideoLinks) as LinearLayout
        spVideoType=dialog!!.findViewById<View>(R.id.spVideoType) as Spinner
        btAddVideo=dialog!!.findViewById<View>(R.id.btAddVideo) as LinearLayout
        etVideoUrl=dialog!!.findViewById<View>(R.id.etVideoUrl) as EditText
        loadingDialog=dialog!!.findViewById<View>(R.id.loading) as LinearLayout
        arrayMedia.clear()
        linearPhoto!!.setOnClickListener{

            linearPhoto!!.setBackgroundResource(R.drawable.rectangular_post_active)
            linearVideo!!.setBackgroundResource(R.drawable.rectangular_post_button)
            selectImage(activity!!)

        }

        linearVideo!!.setOnClickListener{

            linearPhoto!!.setBackgroundResource(R.drawable.rectangular_post_button)
            linearVideo!!.setBackgroundResource(R.drawable.rectangular_post_active)
            //   etVideoUrl.visibility=View.GONE
            linearVideoLinks!!.visibility=View.VISIBLE

        }

        setPickedImages()
        setVideoTypeSpinner()
        setVideoLinksRecycler()

        if(id!=0){
            etName!!.setText(name)
            etDescription!!.setText(description)

            Handler().postDelayed({
                setData(id,arrayMedia)
            }, 200)
        }

        btAddVideo!!.setOnClickListener{
            if(etVideoUrl!!.text.toString().isEmpty())
                Toast.makeText(activity,getString(R.string.enter_video_link),Toast.LENGTH_LONG).show()
            else{
                arraySelectedLinks.add(PostVideo(0,selectedVideoTypeId,etVideoUrl!!.text.toString(),selectedVideoType))
                adapterLinks.notifyDataSetChanged()
                try{view?.let { activity?.hideKeyboard(it) }}catch (e: java.lang.Exception){}
                etVideoUrl!!.setText("")

            }
        }

        btSave!!.setOnClickListener{
            if(etName!!.text.toString().isNotEmpty()
                && etDescription!!.text.toString().isNotEmpty()
            ){
                saveStartupProduct(etName!!.text.toString(),etDescription!!.text.toString())
            }else
                AppHelper.createDialog(activity!!,getString(R.string.check_empty_fields))
        }

        if(id!=0){
            etName!!.setText(name)
            etDescription!!.setText(description)

        }

        btCancel!!.setOnClickListener{dialog!!.dismiss()}
        btClose!!.setOnClickListener{dialog!!.dismiss()}
        dialog!!.show()

    }



    private fun setVideoLinksRecycler(){
        adapterLinks= AdapterVideos(arraySelectedLinks,this)
        val glm = GridLayoutManager(activity, 1)
        rvVideoLinks!!.adapter=adapterLinks
        rvVideoLinks!!.layoutManager=glm
        rvVideoLinks!!.isNestedScrollingEnabled=false
    }

    private fun setVideoTypeSpinner(){
        val types = arrayOf(getString(R.string.youtube),getString(R.string.vimeo))
        //val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, types)
        val adapter = ArrayAdapter(activity, R.layout.spinner_text_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spVideoType!!.adapter = adapter;
        spVideoType!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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



    private fun setPickedImages(){
        adapterPickedImages= AdapterImagesPicked(arrayMedia,this,AppConstants.IMAGES_PICKED)
        val glm = GridLayoutManager(activity, 3)
        rvMediaImages!!.adapter=adapterPickedImages
        rvMediaImages!!.layoutManager=glm
        rvMediaImages!!.visibility=View.VISIBLE
        rvMediaImages!!.isNestedScrollingEnabled=false
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


    private fun saveStartupProduct(name: String,description: String){
        try{loadingDialog!!.visibility=View.VISIBLE}catch (e:Exception){}
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

        val jsonString = RequestBody.create(MediaType.parse("text/plain"), Gson().toJson(getRequestParameter(name,description)))
        Log.wtf("save_post_json_rb",jsonString.toString())
        Log.wtf("save_post_json", Gson().toJson(getRequestParameter(name,description)))

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveStartupProduct(jsonString,arrayBody
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try{loadingDialog!!.visibility=View.GONE}catch (e:Exception){}
                    dialog!!.dismiss()
                    try{ onProductSaved(response.isSuccessful)}catch (e:java.lang.Exception){Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    try{loadingDialog!!.visibility=View.GONE}catch (e:Exception){}
                    Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show()
                }
            })
    }


    private fun onProductSaved(success:Boolean) {
        if(success) {
            if(isEdit)
                createDialog(activity!!, getString(R.string.edited_successfully))
            else
                createDialog(activity!!, getString(R.string.added_successfully))
        }
        //  Toast.makeText(activity,response!!.title,Toast.LENGTH_LONG).show()
    }


    private fun getRequestParameter(name:String,description:String):ResponseStartupProduct{
        var date = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.UK).format(Date())

        var postArrayMedia= arrayListOf<Media>()
        for (i in arrayMedia.indices)
            postArrayMedia.add(Media(arrayMedia[i].id,arrayMedia[i].fileName!!.substring(arrayMedia[i].fileName!!.lastIndexOf("/")+1),AppConstants.MEDIA_TYPE_IMAGE))

        for (i in arraySelectedLinks.indices){
            if(arraySelectedLinks[i].MediaId!=null && arraySelectedLinks[i].MediaId!=0)
                postArrayMedia.add(Media(arraySelectedLinks[i].MediaId,arraySelectedLinks[i].link,arraySelectedLinks[i].typeId))
            else
                postArrayMedia.add(Media(arraySelectedLinks[i].link,arraySelectedLinks[i].typeId))
            }

        var id=null


        var request=ResponseStartupProduct(productId,
            MyApplication.selectedStartupTeam.id!!,
            name,
            description,
            postArrayMedia )

        return request
    }




    private fun setData(id:Int,arrayMedias:ArrayList<PostMedia>){
     
        productId=id

        for (i in arrayMedias.indices){
            if(arrayMedias[i].type==AppConstants.MEDIA_TYPE_IMAGE)
                arrayMedia.add(PostMedia(arrayMedias[i].id,arrayMedias[i].fileName ,arrayMedias[i].type,false,null,null))
            else
                arraySelectedLinks.add(PostVideo(arrayMedias[i].id!!,arrayMedias[i].type,arrayMedias[i].fileName,""))
        }
        
        
        adapterPickedImages.notifyDataSetChanged()
        if(arraySelectedLinks.size>0){
            adapterLinks.notifyDataSetChanged()
            linearVideoLinks!!.visibility=View.VISIBLE
        }

    }


    private fun createDialog(c: Activity, message: String) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ ->
                dialog.cancel()
                getStartupProduct()
                //activity!!.onBackPressed()

            }
        val alert = builder.create()
        alert.show()
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
                deleteStartupProduct(id)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun deleteStartupProduct(id:Int){
        loading.visibility=View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.deleteStartupProduct(id)?.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    loading.visibility=View.GONE
                    try{getStartupProduct()
                    }catch (e:Exception){}
                }
                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    loading.visibility=View.GONE
                }
            })

    }
}
