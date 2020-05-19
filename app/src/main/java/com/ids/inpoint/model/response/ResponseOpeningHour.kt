package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOpeningHour {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("FromTime")
    @Expose
    var fromTime: String? = null
    @SerializedName("ToTime")
    @Expose
    var toTime: String? = null
    @SerializedName("Day")
    @Expose
    var day: String? = null


    constructor(id: Int?, userId: Int?, fromTime: String?, toTime: String?, day: String?) {
        this.id = id
        this.userId = userId
        this.fromTime = fromTime
        this.toTime = toTime
        this.day = day
    }
}