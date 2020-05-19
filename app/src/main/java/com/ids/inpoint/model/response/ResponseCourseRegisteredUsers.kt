package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCourseRegisteredUsers {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Location")
    @Expose
    var location: String? = null
    @SerializedName("ProfileImage")
    @Expose
    var profileImage: String? = null
    @SerializedName("Count")
    @Expose
    var count: Int? = null

}