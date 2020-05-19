package com.ids.inpoint.controller.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.ids.inpoint.R
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper

class AdapterCustomSpinner(val context: Context, var listItemsTxt: ArrayList<ItemSpinner>,var layoutType: Int, var disableFirstItem:Boolean = false) : BaseAdapter() {


    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_sipnner_image, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.

/*        val params = view.layoutParams
        params.height = 60
        view.layoutParams = params*/

        vh.label.text = listItemsTxt[position].name
        if(layoutType==AppConstants.SPINNER_TEXT_IMAGE) {
            try {
                vh.cardImage.visibility=View.VISIBLE
                AppHelper.setImageResize(
                    context, vh.ivCategoryImage,
                    AppConstants.ICONS_URL + listItemsTxt[position].icon!!, 200, 200
                )
            } catch (e: java.lang.Exception) {
            }
        }else{
            vh.cardImage.visibility=View.GONE
        }
        return view
    }

    override fun getItem(position: Int): Any? {

        return null

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun isEnabled(position: Int): Boolean {
        if (disableFirstItem && position == 0 && listItemsTxt[position].id == 0) {
            return false
        } else {
            return super.isEnabled(position)
        }
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {

        val label: TextView = row?.findViewById(R.id.tvPrivacy) as TextView
        val ivCategoryImage: ImageView = row?.findViewById(R.id.ivCategoryImage) as ImageView
        val cardImage: CardView = row?.findViewById(R.id.cardImage) as CardView





    }
}