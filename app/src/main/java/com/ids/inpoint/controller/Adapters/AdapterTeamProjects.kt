package com.ids.inpoint.controller.Adapters


import android.widget.ImageView
import android.widget.TextView


import com.ids.inpoint.model.ItemSpinner


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.custom.CustomTextViewMedium
import com.ids.inpoint.model.ResponseTeamProject
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import java.lang.Exception


import java.util.*


class AdapterTeamProjects(val arrayProjects: ArrayList<ResponseTeamProject>, private val itemClickListener: RVOnItemClickListener,type:Int) :
    RecyclerView.Adapter<AdapterTeamProjects.VHprivacy>() {

    var projectType=type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHprivacy {
        return VHprivacy(LayoutInflater.from(parent.context).inflate(com.ids.inpoint.R.layout.item_team_project, parent, false))
    }

    override fun onBindViewHolder(holder: VHprivacy, position: Int) {
        try{ holder.tvProjectCode.text=arrayProjects[position].code}catch (e:Exception){}
        try{ holder.tvProjectName.text=arrayProjects[position].name}catch (e:Exception){}
        try{ holder.tvType.text=arrayProjects[position].typeName}catch (e:Exception){}
        try{ holder.tvClient.text=arrayProjects[position].client}catch (e:Exception){}
        try{ holder.tvStartDate.text=AppHelper.formatDate(holder.itemView.context,arrayProjects[position].startDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}
        try{ holder.tvDueDate.text=AppHelper.formatDate(holder.itemView.context,arrayProjects[position].dueDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}
        try{ holder.tvEndDate.text=AppHelper.formatDate(holder.itemView.context,arrayProjects[position].endDate!!,"yyyy-MM-dd'T'hh:mm:ss","MM/dd/yyyy")}catch (e:Exception){}



    }

    override fun getItemCount(): Int {
        return arrayProjects.size
    }

    inner class VHprivacy(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var btTeam: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btTeam) as ImageView
        var btEdit: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btEdit) as ImageView
        var btDelete: ImageView = itemView.findViewById(com.ids.inpoint.R.id.btDelete) as ImageView

        var tvProjectName: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvProjectName) as TextView
        var tvProjectCode: TextView = itemView.findViewById(com.ids.inpoint.R.id.tvProjectCode) as TextView
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
