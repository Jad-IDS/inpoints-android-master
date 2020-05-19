package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.inpoint.model.comments
import java.util.ArrayList

class ResponsePost {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Title")
    @Expose
    var title: String? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null
    @SerializedName("Details")
    @Expose
    var details: String? = null
    @SerializedName("PublishDate")
    @Expose
    var publishDate: String? = null
    @SerializedName("Time")
    @Expose
    var time: String? = null
    @SerializedName("Language")
    @Expose
    var language: Any? = null
    @SerializedName("ViewNumber")
    @Expose
    var viewNumber: Int? = null
    @SerializedName("LikeNumber")
    @Expose
    var likeNumber: Int? = null
    @SerializedName("CommentNumber")
    @Expose
    var commentNumber: Int? = null
    @SerializedName("ShareLink")
    @Expose
    var shareLink: Any? = null
    @SerializedName("Verified")
    @Expose
    var verified: Boolean? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("NumberOfFollowers")
    @Expose
    var numberOfFollowers: Int? = null
    @SerializedName("Level")
    @Expose
    var level: Int? = null
    @SerializedName("Points")
    @Expose
    var points: Int? = null
    @SerializedName("Badges")
    @Expose
    var badges: Int? = null
    @SerializedName("DivType")
    @Expose
    var divType: Int? = null
    @SerializedName("ShowInTimeLine")
    @Expose
    var showInTimeLine: Boolean? = null
    @SerializedName("Color")
    @Expose
    var color: String? = null
    @SerializedName("ColorHex")
    @Expose
    var colorHex: String? = null
    @SerializedName("Liked")
    @Expose
    var liked: Boolean? = null
    @SerializedName("Followed")
    @Expose
    var followed: Boolean? = null
    @SerializedName("IsPublic")
    @Expose
    var isPublic: Boolean? = null
    @SerializedName("TeamUserId")
    @Expose
    var teamUserId: Int? = null
    @SerializedName("Location")
    @Expose
    var location: String? = null


    @SerializedName("CanQuit")
    @Expose
    var CanQuit: Int? = null


    @SerializedName("Medias")
    @Expose
    var medias: List<Media>? = null
    @SerializedName("EventDates")
    @Expose
    var eventDates: List<EventDate>? = null


    var showComments: Boolean? = false
    var settingsViewVisible: Boolean? = false
    var isShowMore: Boolean? = false
    var isParticipant: Int? = 0
    var arrayComments: ArrayList<ResponseComments>? = arrayListOf()


    constructor()
}