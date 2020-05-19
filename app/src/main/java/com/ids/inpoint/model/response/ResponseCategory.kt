package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCategory {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Value")
    @Expose
    var value: Any? = null
    @SerializedName("ValueEn")
    @Expose
    var valueEn: String? = null
    @SerializedName("ValueAr")
    @Expose
    var valueAr: String? = null
    @SerializedName("IconPath")
    @Expose
    var iconPath: String? = null

    var isVerified: Boolean? = false

    @SerializedName("PostId")
    @Expose
    var PostId: Int? = null

    @SerializedName("CategoryId")
    @Expose
    var CategoryId: Int? = null
}