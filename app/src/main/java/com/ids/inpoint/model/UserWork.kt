package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserWork {
    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Company")
    @Expose
    var company: String? = null
    @SerializedName("Position")
    @Expose
    var position: String? = null
    @SerializedName("From")
    @Expose
    var from: String? = null
    @SerializedName("To")
    @Expose
    var to: String? = null

    constructor()
}