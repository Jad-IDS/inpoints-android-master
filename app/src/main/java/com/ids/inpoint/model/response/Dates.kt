package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Dates {

    @SerializedName("Id")
    @Expose
    var Id: Int? = null
    @SerializedName("FromDate")
    @Expose
    var FromDate: String? = null
    @SerializedName("ToDate")
    @Expose
    var ToDate: String? = null


    @SerializedName("FromTime")
    @Expose
    var FromTime: String? = null
    @SerializedName("ToTime")
    @Expose
    var ToTime: String? = null

    @SerializedName("PostId")
    @Expose
    var PostId: Int? = null

    @SerializedName("IsDeleted")
    @Expose
    var IsDeleted: Boolean? = null


    constructor(Id: Int?, FromDate: String?, ToDate: String?, FromTime: String?, ToTime: String?) {
        this.Id = Id
        this.FromDate = FromDate
        this.ToDate = ToDate
        this.FromTime = FromTime
        this.ToTime = ToTime
    }
}