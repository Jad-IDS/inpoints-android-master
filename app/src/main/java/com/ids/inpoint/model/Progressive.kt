package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Progressive {

    @SerializedName("profile")
    @Expose
    var profile: Int? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
    @SerializedName("mime")
    @Expose
    var mime: String? = null
    @SerializedName("fps")
    @Expose
    var fps: Int? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("cdn")
    @Expose
    var cdn: String? = null
    @SerializedName("quality")
    @Expose
    var quality: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("origin")
    @Expose
    var origin: String? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null

}