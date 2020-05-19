package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMedia {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null
    @SerializedName("LessonId")
    @Expose
    var lessonId: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("FileName")
    @Expose
    var fileName: String? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null
    @SerializedName("Icon")
    @Expose
    var icon: Any? = null
    @SerializedName("CourseId")
    @Expose
    var courseId: Int? = null
    @SerializedName("InstructorId")
    @Expose
    var instructorId: Int? = null
    @SerializedName("PublicationId")
    @Expose
    var publicationId: Int? = null
    @SerializedName("MainImage")
    @Expose
    var mainImage: Any? = null

}