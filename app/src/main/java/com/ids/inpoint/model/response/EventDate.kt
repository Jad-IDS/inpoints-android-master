package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventDate {

    @SerializedName("EventDateId")
    @Expose
    var eventDateId: Int? = null
    @SerializedName("FromDate")
    @Expose
    var fromDate: String? = null
    @SerializedName("ToDate")
    @Expose
    var toDate: Any? = null
    @SerializedName("FromTime")
    @Expose
    var fromTime: String? = null
    @SerializedName("ToTime")
    @Expose
    var toTime: String? = null

}