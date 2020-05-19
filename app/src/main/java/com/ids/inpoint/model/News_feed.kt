package com.ids.inpoint.model

import java.util.ArrayList


class News_feed {

    var name: String? = null
    var level: String? = null
    var university: String? = null
    var text_post: String? = null
    var imagePostUrl: String? = null
    var userImage: String? = null
    var enableSettings: Boolean? = true
    var enableCorner: Boolean? = false
    var colorSettings: String? = null

    var likesCount: Int? = null
    var shareCount: Int? = null
    var isLiked: Boolean? = null
    var isShared: Boolean? = null

    var postType: Int? = null
    var commentsCount: Int? = null
    var postTime: String? = null
    var showComments: Boolean? = false
    var settingsViewVisible: Boolean? = false
    var arrayComments: ArrayList<comments>? = null


    var eventTitle:String?=null
    var arrayResources: ArrayList<Resources>? = null
    var eventDate:String?=null
    var eventDuration:String?=null
    var eventLocation:String?=null
    var isShowMore:Boolean?=false




    constructor(
        name: String?,
        level: String?,
        university: String?,
        text_post: String?,
        imagePostUrl: String?,
        userImage: String?,
        enableSettings: Boolean?,
        enableCorner:Boolean?,
        colorSettings: String?,
        likesCount: Int?,
        shareCount: Int?,
        isLiked: Boolean?,
        isShared: Boolean?,
        postType: Int?,
        commentsCount: Int?,
        postTime: String?,
        showComments:Boolean?,
        array: ArrayList<comments>?
    ) {
        this.name = name
        this.level = level
        this.university = university
        this.text_post = text_post
        this.imagePostUrl = imagePostUrl
        this.userImage=userImage
        this.enableSettings = enableSettings
        this.colorSettings = colorSettings
        this.likesCount = likesCount
        this.shareCount = shareCount
        this.isLiked = isLiked
        this.isShared = isShared
        this.enableCorner=enableCorner
        this.postType = postType
        this.commentsCount = commentsCount
        this.postTime=postTime
        this.showComments=showComments
        this.arrayComments = array
    }
}
