package com.ids.inpoint.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOnlineCourses {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("InstructorId")
    @Expose
    var instructorId: Int? = null
    @SerializedName("InstructorName")
    @Expose
    var instructorName: String? = null
    @SerializedName("CategoryId")
    @Expose
    var categoryId: Int? = null
    @SerializedName("CategoryName")
    @Expose
    var categoryName: String? = null
    @SerializedName("FileName")
    @Expose
    var fileName: String? = null

}