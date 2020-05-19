package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSearch {

    @SerializedName("Username")
    @Expose
    var username: String? = null
    @SerializedName("Title")
    @Expose
    var title: String? = null
    @SerializedName("Details")
    @Expose
    var details: String? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("Link")
    @Expose
    var link: String? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null

}