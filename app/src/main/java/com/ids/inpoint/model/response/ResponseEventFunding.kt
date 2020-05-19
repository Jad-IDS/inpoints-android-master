package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseEventFunding {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Amount")
    @Expose
    var amount: String? = null
    @SerializedName("IsDeleted")
    @Expose
    var isDeleted: Boolean? = null

}