package com.ids.inpoint.model



class Reviews {
    var userId: String?=null
    var userName: String?=null
    var userImageUrl: String?=null
    var userComment: String?=null
    var rating:Float?=null


    constructor(
        userId: String?,
        userName: String?,
        userImageUrl: String?,
        userComment: String?
    ) {
        this.userId = userId
        this.userName = userName
        this.userImageUrl = userImageUrl
        this.userComment = userComment

    }
}
