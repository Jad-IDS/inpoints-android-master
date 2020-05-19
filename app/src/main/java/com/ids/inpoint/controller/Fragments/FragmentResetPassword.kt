package com.ids.inpoint.controller.Fragments




import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ids.inpoint.controller.Activities.ActivityLogin
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.ids.inpoint.R
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.utils.*
import com.ids.inpoint.utils.AppHelper.Companion.hideKeyboard
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.btBack
import kotlinx.android.synthetic.main.fragment_reset_password.etEmail

import kotlinx.android.synthetic.main.loading_trans.*
import java.lang.Exception


class FragmentResetPassword : Fragment() , RVOnItemClickListener {

    private lateinit var birthDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var myBithDayCalendar: Calendar
    private var selectedDate = ""
    private var isPerson=true
    private var canClick= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_reset_password, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    private fun init(){
      //  activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        btResetPassword.setOnClickListener{checkReset()}

        btBack.setOnClickListener{
           (activity!! as ActivityLogin).removeFrag(AppConstants.RESET_FRAG)
        }


    }


    override fun onResume() {
        canClick=true
        super.onResume()
    }

    private fun checkReset(){
        if(etEmail.text.toString().isEmpty() )
            Toast.makeText(activity,getString(R.string.enter_email),Toast.LENGTH_LONG).show()
        else if(!AppHelper.isValidEmail(etEmail.text.toString()))
            Toast.makeText(activity,getString(R.string.enter_valid_email),Toast.LENGTH_LONG).show()
        else
            resetPassword()
        //(activity!! as ActivityLogin).removeFrag(AppConstants.SIGNUP_FRAG)
    }





    override fun onItemClicked(view: View, position: Int) {


    }



    private fun resetPassword(){
        loading.visibility=View.VISIBLE
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.resetPassword(
                etEmail.text.toString(),
                MyApplication.languageCode

            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    loading.visibility=View.GONE
                    try{createDialog(activity!!,response.body().toString())}catch (e:Exception){}
                    try{view?.let { activity?.hideKeyboard(it) }}catch (e:Exception){}
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
            .setCancelable(true)
            .setNegativeButton(c.getString(R.string.dialog_ok)) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}


