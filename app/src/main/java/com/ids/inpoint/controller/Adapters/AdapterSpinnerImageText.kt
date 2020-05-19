package com.ids.inpoint.controller.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.model.ItemSpinnerTextImage
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception

class AdapterSpinnerImageText(context: Context, var items: ArrayList<ItemSpinnerTextImage>) :
    ArrayAdapter<ItemSpinnerTextImage>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        return getCustomView(parent, convertView, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {

        return getCustomView(parent, convertView, position)
    }

    private fun getCustomView(
        parent: ViewGroup?,
        convertView: View?,
        position: Int
    ): View {
        val view = convertView ?:
            LayoutInflater.from(context).inflate(R.layout.item_spinner_image_text, parent, false)

        val ivImage: ImageView = view?.findViewById(R.id.ivImage) as ImageView
        val tvText: TextView = view?.findViewById(R.id.tvText) as TextView

        try {
            AppHelper.setRoundImageResize(
                view.context,
                ivImage,
                AppConstants.IMAGES_URL + items[position].image!!,
                false
            )
        } catch (E: Exception) {
        }

        tvText.text = items[position].text

        return view
    }
}