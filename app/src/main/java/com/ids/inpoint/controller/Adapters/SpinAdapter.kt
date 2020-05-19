package com.ids.inpoint.controller.Adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.ids.inpoint.model.Days

class SpinAdapter(
    context: Context, textViewResourceId: Int,
    private val values: ArrayList<Days>
) : ArrayAdapter<Days>(context, textViewResourceId, values) {

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): Days? {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].translatedName
        return label
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].translatedName

        return label
    }
}