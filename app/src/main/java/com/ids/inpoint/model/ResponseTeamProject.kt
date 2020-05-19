package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTeamProject {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Summary")
    @Expose
    var summary: String? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("StartDate")
    @Expose
    var startDate: String? = null
    @SerializedName("DueDate")
    @Expose
    var dueDate: String? = null
    @SerializedName("EndDate")
    @Expose
    var endDate: String? = null
    @SerializedName("Code")
    @Expose
    var code: String? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null
    @SerializedName("Client")
    @Expose
    var client: String? = null
    @SerializedName("IsDeleted")
    @Expose
    var isDeleted: Boolean? = null
    @SerializedName("TeamId")
    @Expose
    var teamId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("TypeName")
    @Expose
    var typeName: String? = null

}