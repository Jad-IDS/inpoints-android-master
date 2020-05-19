package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCoursePublication {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Title")
    @Expose
    var title: String? = null
    @SerializedName("PublishDate")
    @Expose
    var publishDate: String? = null
    @SerializedName("Source")
    @Expose
    var source: String? = null
    @SerializedName("CreationDate")
    @Expose
    var creationDate: String? = null
    @SerializedName("Lang")
    @Expose
    var lang: String? = null
    @SerializedName("Priority")
    @Expose
    var priority: Int? = null
    @SerializedName("TagName")
    @Expose
    var tagName: String? = ""
    @SerializedName("CategoryName")
    @Expose
    var categoryName: String? = null
    @SerializedName("AuthorName")
    @Expose
    var authorName: String? = null
    @SerializedName("CourseName")
    @Expose
    var courseName: String? = null
    @SerializedName("LessonName")
    @Expose
    var lessonName: String? = null
    @SerializedName("Summary")
    @Expose
    var summary: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Published")
    @Expose
    var published: Boolean? = null
    @SerializedName("Featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("FileName")
    @Expose
    var fileName: String? = null

}