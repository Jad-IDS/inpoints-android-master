package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTeamFile {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("DisplayName")
    @Expose
    var displayName: String? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Size")
    @Expose
    var size: String? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("Icon")
    @Expose
    var icon: String? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("TeamId")
    @Expose
    var teamId: Int? = null

}