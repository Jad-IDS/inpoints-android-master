package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUser {

    @SerializedName("Id")
    @Expose
    var id: Int? = null

    @SerializedName("UserId")
    @Expose
    var UserId: Int? = null

    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null

    @SerializedName("Website")
    @Expose
    var Website: String? = null

    @SerializedName("IsNew")
    @Expose
    var IsNew: Boolean? = null

    @SerializedName("IsDeleted")
    @Expose
    var IsDeleted: Boolean? = null

    @SerializedName("IsFollowed")
    @Expose
    var IsFollowed: Boolean? = null

    @SerializedName("PostId")
    @Expose
    var PostId: Int? = null


    @SerializedName("Name")
    @Expose
    var name: String? = null

    @SerializedName("Type")
    @Expose
    var Type: Int? = null

    @SerializedName("StartupId")
    @Expose
    var StartupId: Int? = null


    constructor(id: Int?, userName: String?, image: String?, Website: String?, IsNew: Boolean?) {
        this.id = id
        this.userName = userName
        this.image = image
        this.Website = Website
        this.IsNew = IsNew
    }
    constructor(id: Int?, userId:Int,userName: String?, image: String?, Website: String?, IsNew: Boolean?,type:Int?,startupId:Int?) {
        this.id = id
        this.UserId = userId
        this.name = userName
        this.image = image
        this.Website = Website
        this.IsNew = IsNew
        this.Type=type
        this.StartupId=startupId
    }
}