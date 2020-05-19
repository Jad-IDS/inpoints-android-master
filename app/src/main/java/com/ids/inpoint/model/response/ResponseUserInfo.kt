package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserInfo {

    @SerializedName("Id")
    @Expose
    var id: Int? = 0
    @SerializedName("UserName")
    @Expose
    var userName: String? = ""
    @SerializedName("Bio")
    @Expose
    var bio: String? = ""
    @SerializedName("DateOfBirth")
    @Expose
    var dateOfBirth: String? = ""
    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = ""
    @SerializedName("ShowToPublic")
    @Expose
    var showToPublic: Boolean? = false

    @SerializedName("Email")
    @Expose
    var email: String? = ""
    @SerializedName("Nationality")
    @Expose
    var nationality: Int? = 0
    @SerializedName("Location")
    @Expose
    var location: Int? = 0
    @SerializedName("City")
    @Expose
    var city: Int? = 0
    @SerializedName("Gender")
    @Expose
    var gender: Int? = 0
    @SerializedName("FacebookId")
    @Expose
    var facebookId: Int? = 0
    @SerializedName("PreferredLanguage")
    @Expose
    var preferredLanguage: String? = ""
    @SerializedName("Image")
    @Expose
    var image: String? = ""
    @SerializedName("CoverImage")
    @Expose
    var coverImage: String? = ""
    @SerializedName("Faculities")
    @Expose
    var faculities: String? = ""
    @SerializedName("Type")
    @Expose
    var type: Int? = 0
    @SerializedName("Sub_Type")
    @Expose
    var subType: Int? = 0
    @SerializedName("FullAddress")
    @Expose
    var fullAddress: String? = ""
    @SerializedName("Industry")
    @Expose
    var industry: Int? = 0
    @SerializedName("Website")
    @Expose
    var website: String? = ""
    @SerializedName("Overview")
    @Expose
    var overview: String? = ""
    @SerializedName("CompanySize")
    @Expose
    var companySize: Int? = 0
    @SerializedName("Specialties")
    @Expose
    var specialties: Int? = 0
    @SerializedName("WorkExperience")
    @Expose
    var workExperience: Int? = 0
    @SerializedName("NumberOfFollowers")
    @Expose
    var numberOfFollowers: Int? = 0
    @SerializedName("Level")
    @Expose
    var level: Int? = 0
    @SerializedName("Points")
    @Expose
    var points: Int? = 0
    @SerializedName("Badges")
    @Expose
    var badges: Int? = 0
    @SerializedName("NumberOfPosted")
    @Expose
    var numberOfPosted: Int? = 0
    @SerializedName("Token")
    @Expose
    var token: String? = ""


    constructor(
        id: Int?,
        userName: String?,
        bio: String?,
        dateOfBirth: String?,
        phoneNumber: String?,
        showToPublic: Boolean?,
        email: String?,
        nationality: Int?,
        location: Int?,
        city: Int?,
        gender: Int?,
        facebookId: Int?,
        preferredLanguage: String?,
        image: String?,
        coverImage: String?,
        faculities: String?,
        type: Int?,
        subType: Int?,
        fullAddress: String?,
        industry: Int?,
        website: String?,
        overview: String?,
        companySize: Int?,
        specialties: Int?,
        workExperience: Int?,
        numberOfFollowers: Int?,
        level: Int?,
        points: Int?,
        badges: Int?,
        numberOfPosted: Int?,
        token: String?
    ) {
        this.id = id
        this.userName = userName
        this.bio = bio
        this.dateOfBirth = dateOfBirth
        this.phoneNumber = phoneNumber
        this.showToPublic = showToPublic
        this.email = email
        this.nationality = nationality
        this.location = location
        this.city = city
        this.gender = gender
        this.facebookId = facebookId
        this.preferredLanguage = preferredLanguage
        this.image = image
        this.coverImage = coverImage
        this.faculities = faculities
        this.type = type
        this.subType = subType
        this.fullAddress = fullAddress
        this.industry = industry
        this.website = website
        this.overview = overview
        this.companySize = companySize
        this.specialties = specialties
        this.workExperience = workExperience
        this.numberOfFollowers = numberOfFollowers
        this.level = level
        this.points = points
        this.badges = badges
        this.numberOfPosted = numberOfPosted
        this.token = token
    }

    constructor()
}