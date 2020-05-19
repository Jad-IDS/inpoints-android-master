package com.ids.inpoint.utils


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.ids.inpoint.R
import com.ids.inpoint.controller.MyApplication

import com.ids.inpoint.model.PostMedia
import com.ids.inpoint.model.TeamStartup
import com.ids.inpoint.model.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ibrahim on 8/23/2017.
 */

class AppHelper {


    fun setbackgroundImage(context: Context, view: View, ImgUrl: String) {

        Glide.with(context)
            .load(ImgUrl)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
            .into(object : SimpleTarget<Bitmap>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    view.background = BitmapDrawable(context.resources, bitmap)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    super.onLoadCleared(placeholder)

                }
            })
    }

    companion object {

        var userId: String? = null
        var fragmentAvailable: Int? = null
        var dateFormatProfile = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        var dateEvent = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        fun getTypeFace(context: Context): Typeface {
                      return if (Locale.getDefault().language == "ar")
                          Typeface.createFromAsset(
                              context.applicationContext.assets,
                              "fonts/DroidKufiRegular.ttf"
                          )//"fonts/NeoTech-Medium.otf"
                      else
                          Typeface.createFromAsset(
                              context.applicationContext.assets,
                              "fonts/Raleway-Regular.ttf"
                          )//"fonts/NeoTech-Medium.otf"

           // return Typeface.DEFAULT

        }


        fun getTypeFaceBold(context: Context): Typeface {
            return if (Locale.getDefault().language == "ar")
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/DroidKufi-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

            else
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Raleway-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

         //   return Typeface.DEFAULT_BOLD
        }


        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun formatDate(c:Context,dateString:String,oldDateFormat:String,newDateFormat:String):String?{
            var format = SimpleDateFormat(oldDateFormat, Locale.US)
            val newDate = format.parse(dateString)
            format = SimpleDateFormat(newDateFormat, Locale.US)
            val date = format.format(newDate)
            return date
        }


        fun createDialog(c: Activity, message: String) {

            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        fun createYesNoDialog(c: Activity, message: String, doAction: () -> Unit) {
            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton(c.getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(c.getString(R.string.yes)) { dialog, _ ->
                    doAction()
                }
            val alert = builder.create()
            alert.show()
        }

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun setTextColor(context: Context, view: TextView, color: Int) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(ContextCompat.getColor(context, color))
            } else {
                view.setTextColor(context.resources.getColor(color))
            }
        }


        fun setTextColor(context: Context, view: Button, color: Int) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(ContextCompat.getColor(context, color))
            } else {
                view.setTextColor(context.resources.getColor(color))
            }
        }

        fun changeLanguage(context: Context, language: String) {

            when (language) {
                AppConstants.LANG_ARABIC -> Locale.setDefault(Locale("ar"))
                AppConstants.LANG_ENGLISH -> Locale.setDefault(Locale.ENGLISH)
                "0" -> { Locale.setDefault(Locale.ENGLISH) }
            }

            val configuration = Configuration()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(Locale.getDefault())
                configuration.setLayoutDirection(Locale.getDefault())

            } else
                configuration.locale = Locale.getDefault()


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context.createConfigurationContext(configuration);
            } else {
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
            }
            MyApplication.languageCode = language


        }

/*
        fun isIsLoggedIn(context: Context): Boolean {
            isLoggedIn = context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE)
                .getBoolean(AppConstants.IS_LOGGED_IN, false)
            return isLoggedIn
        }

        fun setIsLoggedIn(context: Context, isLoggedIn: Boolean) {
            context.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit()
                .putBoolean(AppConstants.IS_LOGGED_IN, isLoggedIn).apply()
        }
*/


        fun setLocal(context: Context) {

            if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
               LocaleUtils.setLocale(Locale("en"))
            } else if (MyApplication.languageCode == AppConstants.LANG_ARABIC) {
                LocaleUtils.setLocale(Locale("ar","LB"))
            }

        }

        fun isValidEmail(target: String): Boolean {

            return if (target.isEmpty()) {
                false
            } else {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }


        fun resetMemory() {
            MyApplication.arrayComments= arrayListOf()
            MyApplication.arrayTypes= arrayListOf()
            MyApplication.arrayFiles= arrayListOf()
            MyApplication.selectedPost= ResponsePost()
            MyApplication.selectedMedia= PostMedia()
            MyApplication.selectedOnlineCourse= ResponseOnlineCourses()
            MyApplication.selectedLesson= ResponseLesson()


            MyApplication.selectedStartupTeam= TeamStartup()

            MyApplication.arrayUsers= arrayListOf()




        }

        fun getTypeFaceLight(context: Context): Typeface {
            return if (Locale.getDefault().language == "ar")
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/GE SS Two Light.otf"
                )//fonts/NeoTech-Light.otf
            else if (Locale.getDefault().language == "fa")
            //return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/NAZANINB.TTF");
                Typeface.createFromAsset(context.applicationContext.assets, "fonts/BYekan.ttf")
            else
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Roboto-Light.ttf"
                )//fonts/NeoTech-Light.otf
        }


/*        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }*/


        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }

        fun setViewBackground(url: String, view: View) {
            Glide.with(view.context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                .into(object : SimpleTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        view.background = BitmapDrawable(view.context.resources, bitmap)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)

                    }
                })
        }



        fun setViewBackgroundResized(url: String, view: View) {
            Glide.with(view.context)
                .load(url)
                .asBitmap()
                .override(500, 300)
                .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                .into(object : SimpleTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        view.background = BitmapDrawable(view.context.resources, bitmap)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)

                    }
                })
        }



        fun share(context: Context, subject: String, text: String) {
            val intent = Intent(Intent.ACTION_SEND)

            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, text)

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }


        fun AddFragment(fragmentManager: FragmentManager, selectedFragment:Int, myFragment: Fragment, myTag:String){
            fragmentAvailable = selectedFragment
            fragmentManager.beginTransaction()
/*                .setCustomAnimations(
                    com.ids.inpoint.R.anim.enter_from_right,
                    com.ids.inpoint.R.anim.exit_to_left,
                    com.ids.inpoint.R.anim.enter_from_left,
                    com.ids.inpoint.R.anim.exit_to_right
                )*/
                .add(com.ids.inpoint.R.id.container, myFragment, myTag)
                .addToBackStack(null)
                .commit()
        }



        fun ReplaceFragment(fragmentManager: FragmentManager, selectedFragment:Int, myFragment: Fragment, myTag:String){
            fragmentAvailable = selectedFragment
            fragmentManager.beginTransaction()

                .replace(com.ids.inpoint.R.id.container, myFragment, myTag)
                /*              .setCustomAnimations(
                                  com.ids.inpoint.R.anim.enter_from_right,
                                  com.ids.inpoint.R.anim.exit_to_left,
                                  com.ids.inpoint.R.anim.enter_from_left,
                                  com.ids.inpoint.R.anim.exit_to_right
                              )*/
                .commit()

        }

        fun setImage(context: Context, img: ImageView, ImgUrl: String) {
            try {
                Glide.with(context)
                    .load(ImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .fitCenter()
                    .dontTransform()
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(img)
            } catch (e: Exception) {
            }

        }


        fun setImageResize(context: Context, img: ImageView, ImgUrl: String) {


            Glide
                .with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(500, 500)
                .centerCrop()
                .into(img);

        }


        fun setImageResize(context: Context, img: ImageView, ImgUrl: String,height:Int,width:Int){


            Glide
                .with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(width, height)
                .centerCrop()
                .into(img);

        }

        fun setRoundImageResize(context: Context, img: ImageView, ImgUrl: String,isLocal:Boolean) {
            Log.wtf("image_rounded", ImgUrl)
            if (isLocal) {
                Glide.with(context).load(File(ImgUrl)).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate()
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap) {
                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })
            } else {
                Glide.with(context).load(ImgUrl).asBitmap().centerCrop().dontAnimate().override(500, 500)
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap) {
                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })
            }
        }


        fun setImageResizePost(context: Context, img: ImageView, ImgUrl: String) {


            Glide
                .with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(700, 500)
                .centerCrop()
                .into(img);

        }

        fun setImage(context: Context, img: ImageView, ImgUrl: String, isLocal: Boolean) {
            try {
                if (isLocal) {
                    Glide.with(context)
                        .load(File(ImgUrl))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .fitCenter()
                        .dontTransform()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(img)
                } else {
                    Glide.with(context)
                        .load(ImgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .fitCenter()
                        .dontTransform()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(img)
                }


            } catch (e: Exception) {
            }

        }

        fun setRoundImage(context: Context, img: ImageView, ImgUrl: String, isLocal: Boolean) {
            Log.wtf("image_rounded", ImgUrl)
            if (isLocal) {
                Glide.with(context).load(File(ImgUrl)).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate()
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap) {
                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })
            } else {
                Glide.with(context).load(ImgUrl).asBitmap().centerCrop().dontAnimate()
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap) {
                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })
            }
        }


        fun hideSystemUI(activity: Activity) {
            val decorView: View
            decorView = activity.window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
        }


        fun getFinalPath(context: Context, isTranslatable: Boolean, isOneSize: Boolean, language: String): String {
            var drawablePath = AppConstants.DRAWABLE
            if (isTranslatable && !language.matches(MyApplication.languageCode.toRegex()))
                drawablePath = "$drawablePath-$language"
            if (!isOneSize) {
                when (context.resources.displayMetrics.densityDpi) {
                    DisplayMetrics.DENSITY_MEDIUM -> drawablePath = drawablePath + "-" + AppConstants.MDPI_FOLDER
                    DisplayMetrics.DENSITY_HIGH -> drawablePath = drawablePath + "-" + AppConstants.HDPI_FOLDER
                    DisplayMetrics.DENSITY_XHIGH -> drawablePath = drawablePath + "-" + AppConstants.XHDPI_FOLDER
                    DisplayMetrics.DENSITY_XXHIGH -> drawablePath = drawablePath + "-" + AppConstants.XXHDPI_FOLDER
                    DisplayMetrics.DENSITY_XXXHIGH -> drawablePath = drawablePath + "-" + AppConstants.XXHDPI_FOLDER
                    else -> drawablePath = drawablePath + "-" + AppConstants.XHDPI_FOLDER
                }

            }
            return drawablePath
        }

        /*     fun setMargins(context: Context?, pvCode: PinView?, i: Int, i1: Int, i2: Int, i3: Int) {

             }
     */

        fun setMargins(context: Context, view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val leftInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, left.toFloat(), context.resources
                        .displayMetrics
                ).toInt()
                val topInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, top.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val rightInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, right.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val bottomInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, bottom.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val p = view.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(leftInDp, topInDp, rightInDp, bottomInDp)
                view.requestLayout()
            }
        }


        fun setPaddings(context: Context, view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val leftInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, left.toFloat(), context.resources
                        .displayMetrics
                ).toInt()
                val topInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, top.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val rightInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, right.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val bottomInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, bottom.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val p = view.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(leftInDp, topInDp, rightInDp, bottomInDp)
                view.requestLayout()
            }
        }




        fun getScreenSize(context: Context): String {
            when (context.resources.displayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_MEDIUM -> return AppConstants.MDPI
                DisplayMetrics.DENSITY_HIGH -> return AppConstants.HDPI
                DisplayMetrics.DENSITY_XHIGH -> return AppConstants.XHDPI
                DisplayMetrics.DENSITY_XXHIGH -> return AppConstants.XXHDPI
                DisplayMetrics.DENSITY_XXXHIGH -> return AppConstants.XXXHDPI
                else -> return AppConstants.XXXHDPI
            }
        }



        fun checkVersion(c: Activity, newVersion: Int?): Int {

            var needUrgentUpdate = -1

            try {
                val pInfo: PackageInfo
                try {
                    pInfo = c.packageManager.getPackageInfo(c.packageName, 0)
                    val version = pInfo.versionCode
                    Log.wtf("newVersion", newVersion.toString() + "")
                    Log.wtf("versionCode", version.toString() + "")

                    try {

                        if (newVersion!! > version) {
                            needUrgentUpdate = 1
                        } else {
                            //continue to app
                            needUrgentUpdate = 0

                        }
                    } catch (e: Exception) {
                        Log.d("eee", "" + e)

                    }

                } catch (e: PackageManager.NameNotFoundException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }

            return needUrgentUpdate
        }

        private fun capitalize(model: String): String {
            if (model.length == 0) {
                return ""
            }
            val first = model.get(0)
            return if (Character.isUpperCase(first)) {
                model
            } else {
                Character.toUpperCase(first) + model.substring(1)
            }
        }

        fun getDeviceName(): String {

            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }


        private fun isOnline(): Boolean {

            val runtime = Runtime.getRuntime()
            try {

                val ipProcess = runtime.exec("/system/bin/ping -c 4 8.8.8.8")
                val exitValue = ipProcess.waitFor()
                return exitValue == 0

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return false
        }


        @JvmStatic
        fun isNetworkAvailable(context: Context): Boolean {

            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected && isOnline()
        }


        fun getAndroidVersion(): String {

            val release = Build.VERSION.RELEASE
            val sdkVersion = Build.VERSION.SDK_INT
            return "Android:$sdkVersion ($release)"
        }

        fun getVersionNumber(): Int {

            val pInfo: PackageInfo
            var version = -1
            try {
                pInfo = MyApplication.instance.packageManager
                    .getPackageInfo(MyApplication.instance.packageName, 0)
                version = pInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            return version
        }


        fun getReleaseVersion(context: Context): String {
            var version = ""
            var verCode = 0
            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                version = pInfo.versionName
                verCode = pInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return context.resources.getString(R.string.app_name) + " App Version #" +
                    version + "(" + verCode + ")"
            //return "Zahleh $version"
        }

/*    fun overrideFonts(context: Context, v: View) {

        val typeface: Typeface

        try {
            if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    val child = v.getChildAt(i)
                    overrideFonts(context, child)
                }
            } else if (v is TextView) {

                when (MyApplication.languageCode) {

                    AppConstants.LANG_ENGLISH -> typeface = Typeface.DEFAULT

                    else -> typeface = MyApplication.droidRegular
                }

                v.typeface = typeface
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }*/


         fun addDevice(context: Context,token:String) {



            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
            val cal = Calendar.getInstance()

            val model = getDeviceName()
            val osVersion = getAndroidVersion()

            val deviceToken = token
            val deviceTypeId = "2"

            val imei = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            val registrationDate = dateFormat.format(cal.time)
            val appVersion = getVersionNumber()

            var generalNotification = 1
             if(!MyApplication.showNotifications)
                 generalNotification=0
            val isProduction = 1

            val mobileRegisteredUserId = MyApplication.userLoginInfo.id
            val lang = MyApplication.languageCode

            var device=ResponseDevice(MyApplication.userLoginInfo.id,model,osVersion,deviceToken,deviceTypeId,imei,registrationDate,generalNotification,appVersion.toString(),isProduction,mobileRegisteredUserId,lang)

            RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
                ?.saveDevice(device)
                ?.enqueue(object : Callback<ResponseDevice> {
                    override fun onResponse(
                        call: Call<ResponseDevice>,
                        response: Response<ResponseDevice>
                    ) {
                        try {


                        } catch (E: java.lang.Exception) {
                        }
                    }

                    override fun onFailure(call: Call<ResponseDevice>, throwable: Throwable) {
                    }
                })
        }



    }





}
