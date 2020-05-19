package com.ids.inpoint.controller.Fragments


import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.*
import android.widget.Toast
import com.ids.inpoint.controller.Activities.ActivityLogin
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener

import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.btLogin


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.ids.inpoint.R
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.utils.*
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.etPassword
import kotlinx.android.synthetic.main.fragment_signup.btShowPassword
import kotlinx.android.synthetic.main.loading_trans.*
import java.lang.Exception


class FragementSignup : Fragment() , RVOnItemClickListener {

    private lateinit var birthDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var myBithDayCalendar: Calendar
    private var selectedDate = ""
    private var isPerson=true
    private var canClick= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_signup, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    private fun init(){
        //activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        btSignup.setOnClickListener{ signup() }
        myBithDayCalendar = Calendar.getInstance()

        birthDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myBithDayCalendar.set(Calendar.YEAR, year)
            myBithDayCalendar.set(Calendar.MONTH, monthOfYear)
            myBithDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etalender.setText( AppHelper.dateFormatProfile.format(myBithDayCalendar.time))
            selectedDate = AppHelper.dateFormatProfile.format(myBithDayCalendar.time)
        }

        linearCalendar.setOnClickListener{
            DatePickerDialog(activity!!, birthDateListener, myBithDayCalendar.get(Calendar.YEAR), myBithDayCalendar.get(Calendar.MONTH), myBithDayCalendar.get(Calendar.DAY_OF_MONTH)).show()
      }

        linearSwitch.setOnClickListener{
            if(isPerson){
                linearSwitch.gravity=Gravity.END
                tvType.text=getString(R.string.company)
            }else{
                linearSwitch.gravity=Gravity.START
                tvType.text=getString(R.string.person)
            }
            isPerson=!isPerson
        }

        btLogin.setOnClickListener{
           (activity!! as ActivityLogin).removeFrag(AppConstants.SIGNUP_FRAG)
        }
        btBack.setOnClickListener{btLogin.performClick()}

        btShowPassword.setOnTouchListener(View.OnTouchListener { _, event ->
            var selection=etPassword.selectionStart
            when (event.action) {

                MotionEvent.ACTION_UP -> {
                   etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                   try{ etPassword.setSelection(selection)}catch (e:Exception){}
                }

                MotionEvent.ACTION_DOWN -> {
                    etPassword.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                     try{ etPassword.setSelection(selection)}catch (e:Exception){}
                 }
           }
            true
        })


    }


    override fun onResume() {
        canClick=true
        super.onResume()
    }

    private fun signup(){
        if(etName.text.toString().isEmpty() || etalender.text.toString().isEmpty() || etEmail.text.toString().isEmpty() || etPassword.text.toString().isEmpty() )
             Toast.makeText(activity,getString(R.string.check_empty_fields),Toast.LENGTH_LONG).show()
        else if(!AppHelper.isValidEmail(etEmail.text.toString()))
             Toast.makeText(activity,getString(R.string.enter_valid_email),Toast.LENGTH_LONG).show()
        else
            register()
            //(activity!! as ActivityLogin).removeFrag(AppConstants.SIGNUP_FRAG)
    }





    override fun onItemClicked(view: View, position: Int) {


    }



    private fun register(){
        loading.visibility=View.VISIBLE
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.registration(
                etName.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etalender.text.toString(),
                (if (isPerson) 1 else 2),
                MyApplication.languageCode

            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    loading.visibility=View.GONE
                    try{createDialog(activity!!,response.body().toString())}catch (e:Exception){}

                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    Toast.makeText(activity,"failed", Toast.LENGTH_LONG).show()
                }
            })
    }



    fun createDialog(c: Activity, message: String) {

        val builder = AlertDialog.Builder(c)
        builder
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ ->
                dialog.cancel()
                try{view?.let { activity?.hideKeyboard(it) }}catch (e:Exception){}
                btLogin.performClick()
            }
        val alert = builder.create()
        alert.show()
    }
}


