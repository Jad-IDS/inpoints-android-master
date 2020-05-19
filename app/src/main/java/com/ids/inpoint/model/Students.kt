package com.ids.inpoint.model



class Students {
    var userId: String?=null
    var userName: String?=null
    var userImageUrl: String?=null
    var address: String?=null
    var level: String?=null


    constructor(
        userId: String?,
        userName: String?,
        userImageUrl: String?,
        address: String?,
        level: String?

    ) {
        this.userId = userId
        this.userName = userName
        this.userImageUrl = userImageUrl
        this.address = address
        this.level = level

    }
}
