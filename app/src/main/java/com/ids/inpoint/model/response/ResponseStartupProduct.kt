package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseStartupProduct {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Medias")
    @Expose
    var medias: ArrayList<Media>? = null


    constructor(
        id: Int?,
        userId: Int?,
        name: String?,
        description: String?,
        medias: ArrayList<Media>?
    ) {
        this.id = id
        this.userId = userId
        this.name = name
        this.description = description
        this.medias = medias
    }
}