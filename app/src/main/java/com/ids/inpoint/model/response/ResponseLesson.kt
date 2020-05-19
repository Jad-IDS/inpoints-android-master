package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLesson {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("CourseId")
    @Expose
    var courseId: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Order")
    @Expose
    var order: Int? = null
    @SerializedName("Deleted")
    @Expose
    var deleted: Boolean? = null

}