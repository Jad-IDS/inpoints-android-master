package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseBranch {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Phone")
    @Expose
    var phone: String? = null
    @SerializedName("Location")
    @Expose
    var location: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null

    constructor(
        id: Int?,
        userId: Int?,
        name: String?,
        phone: String?,
        location: String?,
        description: String?
    ) {
        this.id = id
        this.userId = userId
        this.name = name
        this.phone = phone
        this.location = location
        this.description = description
    }
}