package com.ids.inpoint.model

import android.os.Build
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.Cookie

class RequestVimeo {

    @SerializedName("files")
    @Expose
    var files: Files? = null
    @SerializedName("lang")
    @Expose
    var lang: String? = null
    @SerializedName("sentry")
    @Expose
    var sentry: Any? = null
    @SerializedName("ab_tests")
    @Expose
    var abTests: Any? = null
    @SerializedName("referrer")
    @Expose
    var referrer: Any? = null
    @SerializedName("cookie_domain")
    @Expose
    var cookieDomain: String? = null
    @SerializedName("timestamp")
    @Expose
    var timestamp: Int? = null
    @SerializedName("gc_debug")
    @Expose
    var gcDebug: Any? = null
    @SerializedName("expires")
    @Expose
    var expires: Int? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("session")
    @Expose
    var session: String? = null
    @SerializedName("cookie")
    @Expose
    var cookie: Cookie? = null
    @SerializedName("build")
    @Expose
    var build: Build? = null
    @SerializedName("urls")
    @Expose
    var urls: Any? = null
    @SerializedName("signature")
    @Expose
    var signature: String? = null
    @SerializedName("flags")
    @Expose
    var flags: Any? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("file_codecs")
    @Expose
    var fileCodecs: Any? = null

}