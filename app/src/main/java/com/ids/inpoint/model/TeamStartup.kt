package com.ids.inpoint.model

import com.google.gson.annotations.SerializedName

class TeamStartup {
    @SerializedName("Id")
    var id: Int? = null

    @SerializedName("UserName")
    var userName: String? = null

    @SerializedName("Bio")
    var bio: String? = null

    @SerializedName("Image")
    var image: String? = null

    @SerializedName("CoverImage")
    var coverImage: String? = null

    @SerializedName("Type")
    var type: Int? = null

    @SerializedName("CreatedBy")
    var createdBy: Int? = null

    @SerializedName("ShowToPublic")
    var showToPublic: Boolean? = null

    @SerializedName("NumberOfFollowers")
    var numberOfFollowers: Int? = null

    @SerializedName("Level")
    var level: Int? = null

    @SerializedName("Points")
    var points: Int? = null

    @SerializedName("Badges")
    var badges: Int? = null

    @SerializedName("CanJoin")
    var canJoin: Int? = null

    @SerializedName("Admin")
    var admin: Boolean? = null


    @SerializedName("CanInvesting")
    var CanInvesting: Boolean? = null

    @SerializedName("CreationDate")
    var CreationDate: String? = null

    @SerializedName("DateOfBirth")
    var DateOfBirth: String? = null

    @SerializedName("Sub_Type")
    var Sub_Type: Int? = null

    constructor(
        id: Int? = null,
        userName: String? = null,
        bio: String? = null,
        image: String? = null,
        coverImage: String? = null,
        type: Int? = null,
        createdBy: Int? = null,
        showToPublic: Boolean? = null,
        numberOfFollowers: Int? = null,
        level: Int? = null,
        points: Int? = null,
        badges: Int? = null,
        canJoin: Int? = null,
        admin: Boolean? = null
    ) {
        this.id = id
        this.userName = userName
        this.bio = bio
        this.image = image
        this.coverImage = coverImage
        this.type = type
        this.createdBy = createdBy
        this.showToPublic = showToPublic
        this.numberOfFollowers = numberOfFollowers
        this.level = level
        this.points = points
        this.badges = badges
        this.canJoin = canJoin
        this.admin = admin
    }





}
