package com.ids.inpoint.model.response



import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RequestEvent {
    @SerializedName("Id")
    var Id : Int? = null

    @SerializedName("Title")
    var Title : String? = null

    @SerializedName("Type")
    var Type  : Int? = null

    @SerializedName("Details")
    var Details : String? = null

    @SerializedName("Location")
    var Location : String? = null

    @SerializedName("PublishDate")
    var PublishDate : String? = null

    @SerializedName("UserId")
    var UserId : Int? = null

    @SerializedName("DivType")
    var DivType : Int? = null

    @SerializedName("ShowInTimeLine")
    var ShowInTimeLine  : Boolean? = null

    @SerializedName("IsPublic")
    var IsPublic  : Boolean? = null


    @SerializedName("Medias")
    var Medias  : List<RequestMedia>? = null

    @SerializedName("Fundings")
    var Fundings  : ArrayList<Funding>? = null

    @SerializedName("Resources")
    var Resources  : ArrayList<Resources>? = null

    @SerializedName("Organizers")
    var Organizers  : ArrayList<ResponseUser>? = null

    @SerializedName("Partners")
    var Partners  : ArrayList<ResponseUser>? = null

    @SerializedName("Dates")
    var Dates  : ArrayList<Dates>? = null


    constructor(
        Id: Int?,
        Title: String?,
        Type: Int?,
        Details: String?,
        Location: String?,
        PublishDate: String?,
        UserId: Int?,
        DivType: Int?,
        ShowInTimeLine: Boolean?,
        IsPublic: Boolean?,
        Medias: List<RequestMedia>?,
        Fundings: ArrayList<Funding>?,
        Resources: ArrayList<Resources>?,
        Organizers: ArrayList<ResponseUser>?,
        Partners: ArrayList<ResponseUser>?,
        Dates: ArrayList<Dates>?
    ) {
        this.Id = Id
        this.Title = Title
        this.Type = Type
        this.Details = Details
        this.Location = Location
        this.PublishDate = PublishDate
        this.UserId = UserId
        this.DivType = DivType
        this.ShowInTimeLine = ShowInTimeLine
        this.IsPublic = IsPublic
        this.Medias = Medias
        this.Fundings = Fundings
        this.Resources = Resources
        this.Organizers = Organizers
        this.Partners = Partners
        this.Dates = Dates
    }
}