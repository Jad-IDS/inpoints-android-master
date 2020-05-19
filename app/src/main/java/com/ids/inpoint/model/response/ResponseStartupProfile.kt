package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseStartupProfile {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("MembershipId")
    @Expose
    var membershipId: Any? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Bio")
    @Expose
    var bio: String? = null
    @SerializedName("DateOfBirth")
    @Expose
    var dateOfBirth: String? = null
    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = null
    @SerializedName("ShowToPublic")
    @Expose
    var showToPublic: Boolean? = null
    @SerializedName("Email")
    @Expose
    var email: Any? = null
    @SerializedName("Nationality")
    @Expose
    var nationality: Any? = null
    @SerializedName("Location")
    @Expose
    var location: Int? = null
    @SerializedName("City")
    @Expose
    var city: Int? = null
    @SerializedName("Gender")
    @Expose
    var gender: Int? = null
    @SerializedName("FacebookId")
    @Expose
    var facebookId: Int? = null
    @SerializedName("PreferredLanguage")
    @Expose
    var preferredLanguage: Any? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("CoverImage")
    @Expose
    var coverImage: String? = null
    @SerializedName("Faculities")
    @Expose
    var faculities: Any? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null
    @SerializedName("Sub_Type")
    @Expose
    var subType: Int? = null
    @SerializedName("FullAddress")
    @Expose
    var fullAddress: String? = null
    @SerializedName("Industry")
    @Expose
    var industry: Int? = null
    @SerializedName("Website")
    @Expose
    var website: String? = null
    @SerializedName("Overview")
    @Expose
    var overview: String? = null
    @SerializedName("CompanySize")
    @Expose
    var companySize: Int? = null
    @SerializedName("Specialties")
    @Expose
    var specialties: Int? = null
    @SerializedName("WorkExperience")
    @Expose
    var workExperience: Int? = null
    @SerializedName("NumberOfFollowers")
    @Expose
    var numberOfFollowers: Int? = null
    @SerializedName("Level")
    @Expose
    var level: Int? = null
    @SerializedName("Points")
    @Expose
    var points: Int? = null
    @SerializedName("Badges")
    @Expose
    var badges: Int? = null
    @SerializedName("NumberOfPosted")
    @Expose
    var numberOfPosted: Int? = null
    @SerializedName("Interested")
    @Expose
    var interested: Int? = null
    @SerializedName("Token")
    @Expose
    var token: Any? = null
    @SerializedName("CreatedBy")
    @Expose
    var createdBy: Int? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("Locked")
    @Expose
    var locked: Boolean? = null
    @SerializedName("HidePosts")
    @Expose
    var hidePosts: Boolean? = null
    @SerializedName("Admin")
    @Expose
    var admin: Boolean? = null
    @SerializedName("RoleId")
    @Expose
    var roleId: Any? = null
    @SerializedName("Approved")
    @Expose
    var approved: Boolean? = null
    @SerializedName("Followed")
    @Expose
    var followed: Boolean? = null

}