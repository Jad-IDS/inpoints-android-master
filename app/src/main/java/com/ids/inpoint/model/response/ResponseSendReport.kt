package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSendReport {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Image")
    @Expose
    var image: String? = null
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null
    @SerializedName("DivType")
    @Expose
    var divType: Int? = null
    @SerializedName("Title")
    @Expose
    var title: String? = null
    @SerializedName("Details")
    @Expose
    var details: String? = null
    @SerializedName("Approved")
    @Expose
    var approved: Boolean? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("Problem")
    @Expose
    var problem: String? = null
    @SerializedName("ProblemText")
    @Expose
    var problemText: String? = null
    @SerializedName("Reason")
    @Expose
    var reason: String? = null


    constructor(
        id: Int?,
        userId: Int?,
        userName: String?,
        image: String?,
        postId: Int?,
        divType: Int?,
        title: String?,
        details: String?,
        approved: Boolean?,
        creationDate: String?,
        problem: String?,
        problemText: String?,
        reason: String?
    ) {
        this.id = id
        this.userId = userId
        this.userName = userName
        this.image = image
        this.postId = postId
        this.divType = divType
        this.title = title
        this.details = details
        this.approved = approved
        this.creationDate = creationDate
        this.problem = problem
        this.problemText = problemText
        this.reason = reason
    }
}