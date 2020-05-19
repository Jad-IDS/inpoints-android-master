package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseConfiguration {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Label")
    @Expose
    var label: String? = null
    @SerializedName("Value")
    @Expose
    var value: Any? = null

}