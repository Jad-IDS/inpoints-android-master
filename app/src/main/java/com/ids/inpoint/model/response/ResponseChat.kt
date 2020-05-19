package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseChat {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("SenderId")
    @Expose
    var senderId: Int? = null
    @SerializedName("SenderName")
    @Expose
    var senderName: String? = null
    @SerializedName("SenderImage")
    @Expose
    var senderImage: String? = null
    @SerializedName("ReceiverId")
    @Expose
    var receiverId: Int? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("Content")
    @Expose
    var content: String? = null


    constructor(
        id: Int?,
        senderId: Int?,
        senderName: String?,
        senderImage: String?,
        receiverId: Int?,
        creationDate: String?,
        content: String?
    ) {
        this.id = id
        this.senderId = senderId
        this.senderName = senderName
        this.senderImage = senderImage
        this.receiverId = receiverId
        this.creationDate = creationDate
        this.content = content
    }
}