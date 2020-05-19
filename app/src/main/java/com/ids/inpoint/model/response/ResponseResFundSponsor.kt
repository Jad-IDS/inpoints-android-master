package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseResFundSponsor {

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
    @SerializedName("Sponsored")
    @Expose
    var sponsored: Int? = null

}