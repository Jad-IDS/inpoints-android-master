package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUser {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Bio")
    @Expose
    var bio: String? = null
    @SerializedName("City")
    @Expose
    var city: Int? = null
    @SerializedName("Location")
    @Expose
    var location: Int? = null
    @SerializedName("Nationality")
    @Expose
    var nationality: Int? = null
    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = null
    @SerializedName("PreferredLanguage")
    @Expose
    var preferredLanguage: String? = null
    @SerializedName("ShowToPublic")
    @Expose
    var showToPublic: Boolean? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Gender")
    @Expose
    var gender: Int? = null
    @SerializedName("DateOfBirth")
    @Expose
    var dateOfBirth: String? = null
    @SerializedName("Sub_Type")
    @Expose
    var subType: Int? = null
    @SerializedName("Website")
    @Expose
    var website: String? = null
    @SerializedName("WorkExperience")
    @Expose
    var workExperience: Int? = null
    @SerializedName("Overview")
    @Expose
    var overview: String? = null
    @SerializedName("Industry")
    @Expose
    var industry: Int? = null
    @SerializedName("FullAddress")
    @Expose
    var fullAddress: String? = null
    @SerializedName("CompanySize")
    @Expose
    var companySize: Int? = null
    @SerializedName("Specialties")
    @Expose
    var specialties: Int? = null


    constructor(
        id: Int?,
        bio: String?,
        city: Int?,
        location: Int?,
        nationality: Int?,
        phoneNumber: String?,
        preferredLanguage: String?,
        showToPublic: Boolean?,
        userName: String?,
        gender: Int?,
        dateOfBirth: String?,
        subType: Int?,
        website: String?,
        workExperience: Int?,
        overview: String?,
        industry: Int?,
        fullAddress: String?,
        companySize: Int?,
        specialties: Int?
    ) {
        this.id = id
        this.bio = bio
        this.city = city
        this.location = location
        this.nationality = nationality
        this.phoneNumber = phoneNumber
        this.preferredLanguage = preferredLanguage
        this.showToPublic = showToPublic
        this.userName = userName
        this.gender = gender
        this.dateOfBirth = dateOfBirth
        this.subType = subType
        this.website = website
        this.workExperience = workExperience
        this.overview = overview
        this.industry = industry
        this.fullAddress = fullAddress
        this.companySize = companySize
        this.specialties = specialties
    }
}