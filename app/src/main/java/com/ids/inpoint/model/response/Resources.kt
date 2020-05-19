package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Resources {

    @SerializedName("Id")
    @Expose
    var Id: Int? = null
    @SerializedName("Description")
    @Expose
    var Description: String? = null

    @SerializedName("TypeDescriptionEn")
    @Expose
    var TypeDescriptionEn: String? = null
    @SerializedName("TypeDescriptionAr")
    @Expose
    var TypeDescriptionAr: String? = null

    @SerializedName("Type")
    @Expose
    var Type: Int? = null

    @SerializedName("IsDeleted")
    @Expose
    var IsDeleted: Boolean? = null


    constructor(Id: Int?, Description: String?, Type: Int?) {
        this.Id = Id
        this.Description = Description
        this.Type = Type
    }
}