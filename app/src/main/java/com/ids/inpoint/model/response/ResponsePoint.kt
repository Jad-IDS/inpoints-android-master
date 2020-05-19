package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePoint {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("DescriptionEn")
    @Expose
    var descriptionEn: String? = null
    @SerializedName("DescriptionAr")
    @Expose
    var descriptionAr: String? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("Points")
    @Expose
    var points: Int? = null

}