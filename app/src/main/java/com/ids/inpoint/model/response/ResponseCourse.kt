package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCourse {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("InstructorId")
    @Expose
    var instructorId: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Level")
    @Expose
    var level: Int? = null
    @SerializedName("Category")
    @Expose
    var category: Any? = null
    @SerializedName("Rating")
    @Expose
    var rating: Any? = null
    @SerializedName("Lang")
    @Expose
    var lang: String? = null
    @SerializedName("Deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("Code")
    @Expose
    var code: String? = null
    @SerializedName("CategoryName")
    @Expose
    var categoryName: String? = null
    @SerializedName("LevelName")
    @Expose
    var levelName: String? = null

}