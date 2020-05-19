package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseEventFundRes {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null
    @SerializedName("TypeDescriptionEn")
    @Expose
    var typeDescriptionEn: String? = null
    @SerializedName("TypeDescriptionAr")
    @Expose
    var typeDescriptionAr: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("IsDeleted")
    @Expose
    var isDeleted: Boolean? = null

    @SerializedName("Amount")
    @Expose
    var amount: String? = null

    @SerializedName("Sponsored")
    @Expose
    var Sponsored: Int? = null

}