package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.inpoint.model.comments
import java.util.ArrayList

class ResponseMessage {

    @SerializedName("Message")
    @Expose
    var Message: String? = ""

}