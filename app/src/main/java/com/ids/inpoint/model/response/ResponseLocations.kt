package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLocations {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("ParentId")
    @Expose
    var parentId: Int? = null
    @SerializedName("ParentValueEn")
    @Expose
    var parentValueEn: Any? = null
    @SerializedName("ParentValueAr")
    @Expose
    var parentValueAr: Any? = null
    @SerializedName("Value")
    @Expose
    var value: Any? = null
    @SerializedName("ValueEn")
    @Expose
    var valueEn: String? = null
    @SerializedName("ValueAr")
    @Expose
    var valueAr: String? = null

}