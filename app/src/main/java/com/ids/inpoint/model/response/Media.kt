package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Media {

    @SerializedName("MediaId")
    @Expose
    var mediaId: Int? = null
    @SerializedName("FileName")
    @Expose
    var fileName: String? = null
    @SerializedName("FileType")
    @Expose
    var fileType: Int? = null


    constructor(MediaId: Int?, FileName: String?, FileType: Int?) {
        this.mediaId = MediaId
        this.fileName = FileName
        this.fileType = FileType
    }

    constructor(FileName: String?, FileType: Int?) {
        this.fileName = FileName
        this.fileType = FileType
    }
}