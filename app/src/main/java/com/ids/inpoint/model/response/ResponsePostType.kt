package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePostType {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("DivType")
    @Expose
    var divType: Int? = null
    @SerializedName("Value")
    @Expose
    var value: Any? = null
    @SerializedName("ValueEn")
    @Expose
    var valueEn: String? = null
    @SerializedName("ValueAr")
    @Expose
    var valueAr: String? = null
    @SerializedName("Points")
    @Expose
    var points: Int? = null
    @SerializedName("VerifierPoints")
    @Expose
    var verifierPoints: Int? = null
    @SerializedName("Color")
    @Expose
    var color: String? = null
    @SerializedName("ColorHex")
    @Expose
    var colorHex: String? = null
    var isSelected: Boolean? = false
}