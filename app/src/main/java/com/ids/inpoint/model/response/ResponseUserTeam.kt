package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserTeam {

    @SerializedName("TeamId")
    @Expose
    var teamId: Int? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null

}