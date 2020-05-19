package com.ids.inpoint.utils


import com.google.gson.annotations.SerializedName
import com.ids.inpoint.model.response.SendReplies

/**
 * Created by Ibrahim on 11/15/2017.
 */

class JsonParameters {

    @SerializedName("PageUserId")
    var PageUserId: Int = 0

    @SerializedName("UserId")
    var UserId: Int=0

    @SerializedName("Status")
    var Status: Int=0

    @SerializedName("DateRang")
    lateinit var DateRang: String

    @SerializedName("Types")
    lateinit var Types: String


    @SerializedName("Categories")
    lateinit var Categories: String


    @SerializedName("ByName")
    lateinit var ByName: String

    @SerializedName("PostInfo")
     var PostInfo: Int=0

    @SerializedName("LastDate")
    lateinit var LastDate: String

    @SerializedName("Skip")
    var Skip : Int=0

    @SerializedName("ByTitle")
    lateinit var ByTitle : String

    @SerializedName("ApplyFilters")
    var ApplyFilters : Boolean=false



    @SerializedName("Id")
     var Id : Int=0

    @SerializedName("PostId")
    var PostId : Int=0

    @SerializedName("CommentId")
    lateinit  var CommentId : String


    @SerializedName("Comment")
     lateinit var Comment : String


    @SerializedName("RepliesCount")
    var RepliesCount : Int=0


    @SerializedName("UserName")
    lateinit var UserName : String

    @SerializedName("Image")
    lateinit var Image : String

    @SerializedName("Date")
    lateinit var Date : String

    @SerializedName("Time")
    lateinit var Time : String

    @SerializedName("Replies")
    lateinit  var Replies : SendReplies


    @SerializedName("Bio")
    lateinit var Bio : String

    @SerializedName("DateOfBirth")
    lateinit var DateOfBirth : String

    @SerializedName("PhoneNumber")
    lateinit var PhoneNumber : String


    @SerializedName("ShowToPublic")
    var ShowToPublic : Boolean=false

    @SerializedName("Email")
    lateinit var Email : String

    @SerializedName("Nationality")
    var Nationality : Int=0

    @SerializedName("Location")
    var Location : Int=0
    @SerializedName("City")
    var City : Int=0
    @SerializedName("Gender")
    var Gender : Int=0
    @SerializedName("FacebookId")
    var FacebookId : Int=0


    @SerializedName("PreferredLanguage")
    lateinit var PreferredLanguage : String
    @SerializedName("CoverImage")
    lateinit var CoverImage : String

    @SerializedName("Faculities")
    lateinit var Faculities : String


    @SerializedName("Type")
    var Type : Int=0
    @SerializedName("Sub_Type")
    var Sub_Type : Int=0

    @SerializedName("FullAddress")
    lateinit var FullAddress : String

    @SerializedName("Industry")
    var Industry : Int=0

    @SerializedName("Website")
    lateinit var Website : String


    @SerializedName("Overview")
    lateinit var Overview : String


    @SerializedName("CompanySize")
    var CompanySize : Int=0

    @SerializedName("Specialties")
    var Specialties : Int=0

    @SerializedName("WorkExperience")
    var WorkExperience : Int=0
    @SerializedName("NumberOfFollowers")
    var NumberOfFollowers : Int=0

    @SerializedName("Level")
    var Level : Int=0

    @SerializedName("Points")
    var Points : Int=0

    @SerializedName("Badges")
    var Badges : Int=0

    @SerializedName("NumberOfPosted")
    var NumberOfPosted : Int=0

    @SerializedName("Token")
    lateinit var Token : String


    @SerializedName("Take")
    var Take : Int=0





    constructor(
        param1: Int,
        param2: Int,
        param3: Int,
        param4: String,
        param5: String,
        param6: String,
        param7: String,
        param8: Int,
        param9: String,
        param10: Int,
        param11: Int,
        param12: String,
        param13: Boolean,
        type:Int
    ) {
        if(type==AppConstants.TYPE_PARAM_POST) {
            this.PageUserId = param1
            this.UserId = param2
            this.Status = param3
            this.DateRang = param4
            this.Types = param5
            this.Categories = param6
            this.ByName = param7
            this.PostInfo = param8
            this.LastDate = param9
            this.Skip = param10
            this.Take = param11
            this.ByTitle = param12
            this.ApplyFilters = param13
        }
    }

    constructor(

        Id: Int,
        PostId: Int,
        CommentId: Int,
        Comment: String,
        RepliesCount: Int,
        UserId: Int,
        UserName: String,
        Image: String,
        Date: String,
        Time: String,
        Replies: SendReplies
    ) {

        this.Id = Id
        this.PostId = PostId
        this.CommentId = CommentId.toString()
        this.Comment = Comment
        this.RepliesCount = RepliesCount
        this.UserName = UserName
        this.UserId = UserId
        this.Image = Image
        this.Date = Date
        this.Time = Time
        this.Replies = Replies
    }

    constructor(
        Id: Int,
        UserName: String,
        Image: String,
        Bio: String,
        DateOfBirth: String,
        PhoneNumber: String,
        ShowToPublic: Boolean,
        Email: String,
        Nationality: Int,
        Location: Int,
        City: Int,
        Gender: Int,
        FacebookId: Int,
        PreferredLanguage: String,
        CoverImage: String,
        Faculities: String,
        Type: Int,
        Sub_Type: Int,
        FullAddress: String,
        Industry: Int,
        Website: String,
        Overview: String,
        CompanySize: Int,
        Specialties: Int,
        WorkExperience: Int,
        NumberOfFollowers: Int,
        Level: Int,
        Points: Int,
        Badges: Int,
        NumberOfPosted: Int,
        Token: String
    ) {
        this.Id = Id
        this.UserName = UserName
        this.Image = Image
        this.Bio = Bio
        this.DateOfBirth = DateOfBirth
        this.PhoneNumber = PhoneNumber
        this.ShowToPublic = ShowToPublic
        this.Email = Email
        this.Nationality = Nationality
        this.Location = Location
        this.City = City
        this.Gender = Gender
        this.FacebookId = FacebookId
        this.PreferredLanguage = PreferredLanguage
        this.CoverImage = CoverImage
        this.Faculities = Faculities
        this.Type = Type
        this.Sub_Type = Sub_Type
        this.FullAddress = FullAddress
        this.Industry = Industry
        this.Website = Website
        this.Overview = Overview
        this.CompanySize = CompanySize
        this.Specialties = Specialties
        this.WorkExperience = WorkExperience
        this.NumberOfFollowers = NumberOfFollowers
        this.Level = Level
        this.Points = Points
        this.Badges = Badges
        this.NumberOfPosted = NumberOfPosted
        this.Token = Token
    }

    constructor(param1: Int, param2: Int, param3: String, param4: String,constructorType:Int) {
    if(constructorType==AppConstants.CONST_TYPE_SEND_COMMENT) {
        this.Id = param1
        this.PostId = param2
        this.CommentId = param3
        this.Comment = param4
    }
    }


}
