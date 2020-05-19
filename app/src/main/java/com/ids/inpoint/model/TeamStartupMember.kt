package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TeamStartupMember {
    @SerializedName("Id")
    var id: Int? = null
    @SerializedName("TeamId")
    var teamId: Int? = null
    @SerializedName("UserId")
    var userId: Int? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("Image")
    var image: String? = null
    @SerializedName("Admin")
    var admin: Boolean? = null

    @SerializedName("UserName")
    var UserName: String? = null

    @Expose(serialize = false)
    var level: Int = 0

    @SerializedName("IsFollowed")
    var isFollowed: Boolean? = false

    @SerializedName("StartupId")
    var StartupId: Int? = null
}