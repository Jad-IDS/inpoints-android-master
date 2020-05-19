package com.ids.inpoint.controller.Adapters


import android.widget.ImageView
import android.widget.TextView


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener

import com.ids.inpoint.model.response.ResponseTeamTask
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*

class AdapterTeamTask(val arrayTasks: ArrayList<ResponseTeamTask>, private val itemClickListener: RVOnItemClickListener, type:Int) :
    RecyclerView.Adapter<AdapterTeamTask.VHprivacy>() {

    var taskType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_team_project, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        try{
            if(arrayTasks[position].predecessor!=null) {
                holder.tvTaskCode.text = arrayTasks[position].predecessor.toString()
            }else{
                holder.tvTaskCode.text=""
            }
        }catch (e:Exception){
            holder.tvTaskCode.text=""
        }
        try{ holder.tvTaskName.text=arrayTasks[position].name}catch (e:Exception){}
        try{ holder.tvType.text=arrayTasks[position].estimatedHours.toString()}catch (e:Exception){}


        try{ holder.tvStartDate.text= AppHelper.formatDate(holder.itemView.context,arrayTasks[position].startDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}
        try{ holder.tvDueDate.text= AppHelper.formatDate(holder.itemView.context,arrayTasks[position].dueDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}
        try{ holder.tvEndDate.text= AppHelper.formatDate(holder.itemView.context,arrayTasks[position].endDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}


    }

    override fun getItemCount(): Int {
        return arrayTasks.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var btTeam: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btTeam) as ImageView
        var btEdit: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btEdit) as ImageView
        var btDelete: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView

        var tvTaskName: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvProjectName) as TextView
        var tvTaskCode: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvProjectCode) as TextView
        var tvType: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvType) as TextView
        var tvClient: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvClient) as TextView
        var tvStartDate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvStartDate) as TextView
        var tvDueDate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvDueDate) as TextView
        var tvEndDate: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvEndDate) as TextView

        init {
            itemView.setOnClickListener(this)
            btTeam.setOnClickListener(this)
            btEdit.setOnClickListener(this)
            btDelete.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}
