package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Funding {

    @SerializedName("Id")
    @Expose
    var Id: Int? = null
    @SerializedName("Description")
    @Expose
    var Description: String? = null
    @SerializedName("Amount")
    @Expose
    var Amount: String? = null

    @SerializedName("PostId")
    @Expose
    var PostId: Int? = null

    @SerializedName("IsDeleted")
    @Expose
    var IsDeleted: Boolean? = null

    constructor(Id: Int?, Description: String?, Amount: String?) {
        this.Id = Id
        this.Description = Description
        this.Amount = Amount
    }
}