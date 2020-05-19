package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOnlineCourseDetail {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Code")
    @Expose
    var code: String? = null
    @SerializedName("InstructorId")
    @Expose
    var instructorId: Int? = null
    @SerializedName("InstructorName")
    @Expose
    var instructorName: String? = null
    @SerializedName("InstructorDescription")
    @Expose
    var instructorDescription: String? = null
    @SerializedName("CategoriesName")
    @Expose
    var categoriesName: String? = null
    @SerializedName("FileName")
    @Expose
    var fileName: String? = null
    @SerializedName("InstructorImage")
    @Expose
    var instructorImage: String? = null
    @SerializedName("CourseDescription")
    @Expose
    var courseDescription: String? = null

    @SerializedName("LessonName")
    @Expose
    var LessonName: String? = null

    @SerializedName("LessonDescription")
    @Expose
    var LessonDescription: String? = null

}