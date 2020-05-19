package com.ids.inpoint.model.response

import com.google.gson.annotations.SerializedName

class RequestMedia {

    @SerializedName("MediaId")
    var MediaId  : Int? = null

    @SerializedName("FileName")
    var FileName : String? = null

    @SerializedName("FileType")
    var FileType : Int? = null

    constructor(MediaId: Int?, FileName: String?, FileType: Int?) {
        this.MediaId = MediaId
        this.FileName = FileName
        this.FileType = FileType
    }

    constructor(FileName: String?, FileType: Int?) {
        this.FileName = FileName
        this.FileType = FileType
    }


}