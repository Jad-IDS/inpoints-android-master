package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class SendReplies {

    @SerializedName("ReplyId")
    var ReplyId: Int? = null

    @SerializedName("CommentId")
    var CommentId: Int? = null

    @SerializedName("Reply")
    var Reply: String? = null

    @SerializedName("ReplyUserId")
    var ReplyUserId: Int? = null

    @SerializedName("ReplyUserName")
    var ReplyUserName: String? = null

    @SerializedName("ReplyImage")
    var ReplyImage: String? = null

    @SerializedName("ReplyDate")
    var ReplyDate: String? = null

    @SerializedName("Time")
    var Time: String? = null


    constructor(
        ReplyId: Int?,
        CommentId: Int?,
        Reply: String?,
        ReplyUserId: Int?,
        ReplyUserName: String?,
        ReplyImage: String?,
        ReplyDate: String?,
        Time: String?
    ) {
        this.ReplyId = ReplyId
        this.CommentId = CommentId
        this.Reply = Reply
        this.ReplyUserId = ReplyUserId
        this.ReplyUserName = ReplyUserName
        this.ReplyImage = ReplyImage
        this.ReplyDate = ReplyDate
        this.Time = Time
    }
}