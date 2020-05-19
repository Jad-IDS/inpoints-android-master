package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestReview {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("CourseId")
    @Expose
    var courseId: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Rating")
    @Expose
    var rating: Int? = null
    @SerializedName("Deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("Text")
    @Expose
    var text: String? = null
    @SerializedName("Date")
    @Expose
    var date: String? = null

    constructor(
        id: Int?,
        courseId: Int?,
        userId: Int?,
        rating: Int?,
        deleted: Boolean?,
        text: String?,
        date: String?
    ) {
        this.id = id
        this.courseId = courseId
        this.userId = userId
        this.rating = rating
        this.deleted = deleted
        this.text = text
        this.date = date
    }
}