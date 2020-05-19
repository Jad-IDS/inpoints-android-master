package com.ids.inpoint.model

import org.w3c.dom.Comment
import java.util.ArrayList

class comments {
    var userId: String?=null
    var userName: String?=null
    var userImageUrl: String?=null
    var userComment: String?=null
    var commentTime: String?=null
    var likesCount: Int?=null
    var isLiked: Boolean?=false
    var showSubComments: Boolean?=false
    var arraySubComments: ArrayList<comments>? = null

    constructor(
        userId: String?,
        userName: String?,
        userImageUrl: String?,
        userComment: String?,
        commentTime: String?,
        likesCount: Int?,
        isLiked: Boolean?,
        showSubComments:Boolean?,
        arraySubComments:ArrayList<comments>
    ) {
        this.userId = userId
        this.userName = userName
        this.userImageUrl = userImageUrl
        this.userComment = userComment
        this.commentTime = commentTime
        this.likesCount = likesCount
        this.isLiked = isLiked
        this.showSubComments=showSubComments
        this.arraySubComments = arraySubComments
    }
}
