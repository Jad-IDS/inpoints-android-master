package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseComments {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null
    @SerializedName("CommentId")
    @Expose
    var commentId: Int? = null
    @SerializedName("Comment")
    @Expose
    var comment: String? = null
    @SerializedName("RepliesCount")
    @Expose
    var repliesCount: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("Date")
    @Expose
    var date: String? = null
    @SerializedName("Time")
    @Expose
    var time: String? = null
    @SerializedName("Replies")
    @Expose
    var replies: ArrayList<ResponseSubCommrent>? = null

    @SerializedName("liked")
    @Expose
    var liked: Boolean? = false

    var showSubComments=false

    constructor(
        id: Int?,
        postId: Int?,
        commentId: Int?,
        comment: String?,
        repliesCount: Int?,
        userId: Int?,
        userName: String?,
        image: String?,
        date: String?,
        time: String?,
        replies: ArrayList<ResponseSubCommrent>?,
        liked: Boolean?,
        showSubComments: Boolean?
    ) {
        this.id = id
        this.postId = postId
        this.commentId = commentId
        this.comment = comment
        this.repliesCount = repliesCount
        this.userId = userId
        this.userName = userName
        this.image = image
        this.date = date
        this.time = time
        this.replies = replies
        this.liked = liked
        this.showSubComments = showSubComments!!
    }
}