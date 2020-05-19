package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserExtraCertification() {
    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Year")
    @Expose
    var year: Int? = null

}