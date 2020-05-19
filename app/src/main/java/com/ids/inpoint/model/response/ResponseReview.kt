package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseReview {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("ProfileImage")
    @Expose
    var profileImage: String? = null
    @SerializedName("Rating")
    @Expose
    var rating: Int? = null
    @SerializedName("Text")
    @Expose
    var text: String? = null
    @SerializedName("Duration")
    @Expose
    var duration: String? = null
    @SerializedName("AvgTotalRating")
    @Expose
    var avgTotalRating: Int? = null
    @SerializedName("Count")
    @Expose
    var count: Int? = null


    @SerializedName("ReviewId")
    @Expose
    var ReviewId: Int? = null

    @SerializedName("StartupId")
    @Expose
    var StartupId: Int? = null

    @SerializedName("Date")
    @Expose
    var Date: String? = null

    @SerializedName("Deleted")
    @Expose
    var Deleted: String? = null


}