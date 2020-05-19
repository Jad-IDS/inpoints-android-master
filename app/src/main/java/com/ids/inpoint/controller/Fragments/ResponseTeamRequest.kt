package com.ids.inpoint.controller.Fragments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTeamRequest {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("TeamId")
    @Expose
    var teamId: Int? = null
    @SerializedName("Approved")
    @Expose
    var approved: Boolean? = null
    @SerializedName("Rejected")
    @Expose
    var rejected: Boolean? = null
    @SerializedName("NotificationId")
    @Expose
    var notificationId: Any? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("TeamName")
    @Expose
    var teamName: String? = null

}