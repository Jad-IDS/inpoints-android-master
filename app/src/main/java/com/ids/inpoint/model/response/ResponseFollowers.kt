package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseFollowers {

    @SerializedName("Id")
    @Expose
    var id: Int? = 0
    @SerializedName("UserId")
    @Expose
    var userId: Int? = 0
    @SerializedName("UserName")
    @Expose
    var userName: String? = ""
    @SerializedName("Image")
    @Expose
    var image: String? = ""
    @SerializedName("IsFollowed")
    @Expose
    var isFollowed: Boolean? = false


    constructor(id: Int?, userId: Int?, userName: String?, image: String?, isFollowed: Boolean?) {
        this.id = id
        this.userId = userId
        this.userName = userName
        this.image = image
        this.isFollowed = isFollowed
    }
}