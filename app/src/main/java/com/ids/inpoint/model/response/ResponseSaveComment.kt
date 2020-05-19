package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSaveComment {

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
    var replies: Any? = null

}