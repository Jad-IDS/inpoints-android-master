package com.ids.inpoint.controller.Adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.model.response.ResponseLesson


import java.util.*


class AdapterLessons(val courses: ArrayList<ResponseLesson>, private val itemClickListener: RVOnItemClickListener) :
    RecyclerView.Adapter<AdapterLessons.VHprivacy>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_lessons, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {

        try{holder.tvLessonTile.text=courses[position].name}catch (e:Exception){}
        try{holder.tvCounter.text=(position+1).toString()}catch (e:Exception){}


    }

    override fun getItemCount(): Int {
        return courses.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvCounter: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvCounter) as TextView
        var tvLessonTile: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvLessonTile) as TextView


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
