package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RequestSaveFile {
    @SerializedName("Id")
    var Id : Int? = null

    @SerializedName("TeamId")
    var TeamId : Int? = null


    @SerializedName("Description")
    var Description : String? = null


    constructor(Id: Int?, TeamId: Int?, Description: String?) {
        this.Id = Id
        this.TeamId = TeamId
        this.Description = Description
    }
}