package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseParticipant {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null
    @SerializedName("ConfirmedByOrganizer")
    @Expose
    var confirmedByOrganizer: Boolean? = null
    @SerializedName("ConfirmedByUser")
    @Expose
    var confirmedByUser: Boolean? = null
    @SerializedName("Present")
    @Expose
    var present: Boolean? = null
    @SerializedName("IsDeleted")
    @Expose
    var isDeleted: Boolean? = null

}