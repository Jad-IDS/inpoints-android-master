package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseVimeo {

    @SerializedName("cdn_url")
    @Expose
    var cdnUrl: String? = null
    @SerializedName("vimeo_api_url")
    @Expose
    var vimeoApiUrl: String? = null
    @SerializedName("request")
    @Expose
    var request: RequestVimeo? = null
    @SerializedName("player_url")
    @Expose
    var playerUrl: String? = null
    @SerializedName("video")
    @Expose
    var video: Any? = null
    @SerializedName("user")
    @Expose
    var user: Any? = null
    @SerializedName("embed")
    @Expose
    var embed: Any? = null
    @SerializedName("view")
    @Expose
    var view: Int? = null
    @SerializedName("vimeo_url")
    @Expose
    var vimeoUrl: String? = null

}