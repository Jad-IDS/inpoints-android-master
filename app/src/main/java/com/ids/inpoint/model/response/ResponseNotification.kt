package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseNotification {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Text")
    @Expose
    var text: String? = null
    @SerializedName("UserId")
    @Expose
    var userId: String? = null
    @SerializedName("Url")
    @Expose
    var url: String? = null
    @SerializedName("Seen")
    @Expose
    var seen: Boolean? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("TimeRange")
    @Expose
    var TimeRange: String? = null


    @SerializedName("Key")
    @Expose
    var key: Int? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null
}