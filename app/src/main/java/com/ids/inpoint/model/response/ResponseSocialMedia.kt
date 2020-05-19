package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSocialMedia {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Icon")
    @Expose
    var icon: String? = null

    @SerializedName("StartupId")
    @Expose
    var StartupId: Int? = null

    @SerializedName("SocialMediaId")
    @Expose
    var SocialMediaId: Int? = null

    @SerializedName("Link")
    @Expose
    var Link: String? = null

    @SerializedName("IsDeleted")
    @Expose
    var IsDeleted: Boolean? = false
}