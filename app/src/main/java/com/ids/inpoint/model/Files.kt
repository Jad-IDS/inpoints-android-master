package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Files {

    @SerializedName("dash")
    @Expose
    var dash: Any? = null
    @SerializedName("hls")
    @Expose
    var hls: Any? = null
    @SerializedName("progressive")
    @Expose
    var progressive: List<Progressive>? = null

}