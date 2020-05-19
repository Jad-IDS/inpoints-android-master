package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSubCommrent {

    @SerializedName("ReplyId")
    @Expose
    var ReplyId: Int? = null


    @SerializedName("CommentId")
    @Expose
    var CommentId: Int? = null

    @SerializedName("Reply")
    @Expose
    var Reply: String? = null

    @SerializedName("ReplyUserId")
    @Expose
    var ReplyUserId: Int? = null

    @SerializedName("ReplyUserName")
    @Expose
    var ReplyUserName: String? = null

    @SerializedName("ReplyImage")
    @Expose
    var ReplyImage: String? = null

    @SerializedName("ReplyDate")
    @Expose
    var ReplyDate: String? = null

    @SerializedName("Time")
    @Expose
    var Time: String? = null

    @SerializedName("liked")
    @Expose
    var liked: Boolean? = false

    constructor(
        ReplyId: Int?,
        CommentId: Int?,
        Reply: String?,
        ReplyUserId: Int?,
        ReplyUserName: String?,
        ReplyImage: String?,
        ReplyDate: String?,
        Time: String?,
        liked: Boolean?
    ) {
        this.ReplyId = ReplyId
        this.CommentId = CommentId
        this.Reply = Reply
        this.ReplyUserId = ReplyUserId
        this.ReplyUserName = ReplyUserName
        this.ReplyImage = ReplyImage
        this.ReplyDate = ReplyDate
        this.Time = Time
        this.liked = liked
    }
}