package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTeamTask {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("TeamId")
    @Expose
    var teamId: Int? = null
    @SerializedName("ProjectId")
    @Expose
    var projectId: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Summary")
    @Expose
    var summary: String? = null
    @SerializedName("AssignBy")
    @Expose
    var assignBy: Int? = null
    @SerializedName("StartDate")
    @Expose
    var startDate: String? = null
    @SerializedName("DueDate")
    @Expose
    var dueDate: String? = null
    @SerializedName("EndDate")
    @Expose
    var endDate: String? = null
    @SerializedName("EstimatedHours")
    @Expose
    var estimatedHours: Int? = null
    @SerializedName("Status")
    @Expose
    var status: Int? = null
    @SerializedName("Notes")
    @Expose
    var notes: String? = null
    @SerializedName("Predecessor")
    @Expose
    var predecessor: Int? = null
    @SerializedName("Deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("TeamName")
    @Expose
    var teamName: String? = null
    @SerializedName("AssignedByName")
    @Expose
    var assignedByName: String? = null
    @SerializedName("ProjectName")
    @Expose
    var projectName: String? = null
    @SerializedName("PredecessorName")
    @Expose
    var predecessorName: Any? = null

}