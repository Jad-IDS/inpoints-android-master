package com.ids.inpoint.controller.Fragments



import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ids.inpoint.R

import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface

import kotlinx.android.synthetic.main.fragment_update_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.lang.Exception


class FragmentUpdatePassword : Fragment() ,RVOnItemClickListener {


    override fun onItemClicked(view: View, position: Int) {
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.inpoint.R.layout.fragment_update_password, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setlisteners()
    }

    private fun init(){


    }

    private fun setlisteners(){
        btBack.setOnClickListener{activity!!.onBackPressed()}
        btSaveChanges.setOnClickListener{updatePassword()}
    }

    private fun updatePassword(){
        if(etOldPassword.text.toString().isEmpty() || etNewPassword.text.toString().isEmpty() || etVerifyPassword.text.toString().isEmpty())
            Toast.makeText(activity,getString(R.string.check_empty_fields),Toast.LENGTH_LONG).show()
        else if(etNewPassword.text.toString() != etVerifyPassword.text.toString())
            Toast.makeText(activity,getString(R.string.check_new_pass),Toast.LENGTH_LONG).show()
        else
            callUpdatePassword()
    }

    private fun callUpdatePassword(){
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updatePassword(etOldPassword.text.toString(),etNewPassword.text.toString()
            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try{
                        if(response.body()=="") {
                            Toast.makeText(activity, getString(R.string.password_updated), Toast.LENGTH_LONG).show()
                            activity!!.onBackPressed()
                        }else
                            AppHelper.createDialog(activity!!,response.body()!!)
                    }catch (E:Exception){ }
                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show()
                }
            })
    }






}
