package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RequestPost {
    @SerializedName("Id")
    var Id : Int? = null

    @SerializedName("Title")
    var Title : String? = null

    @SerializedName("Type")
    var Type  : Int? = null

    @SerializedName("Details")
    var Details : String? = null

    @SerializedName("PublishDate")
    var PublishDate : String? = null

    @SerializedName("UserId")
    var UserId : Int? = null

    @SerializedName("DivType")
    var DivType : Int? = null

    @SerializedName("ShowInTimeLine")
    var ShowInTimeLine  : Boolean? = null

    @SerializedName("Medias")
    var Medias  : List<RequestMedia>? = null


    constructor(
        Id: Int?,
        Title: String?,
        Type: Int?,
        Details: String?,
        PublishDate: String?,
        UserId: Int?,
        DivType: Int?,
        ShowInTimeLine: Boolean?,
        Medias: List<RequestMedia>?
    ) {
        this.Id = Id
        this.Title = Title
        this.Type = Type
        this.Details = Details
        this.PublishDate = PublishDate
        this.UserId = UserId
        this.DivType = DivType
        this.ShowInTimeLine = ShowInTimeLine
        this.Medias = Medias
    }
}