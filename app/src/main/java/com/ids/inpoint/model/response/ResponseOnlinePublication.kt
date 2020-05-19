package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOnlinePublication {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("AuthorId")
    @Expose
    var authorId: Any? = null
    @SerializedName("AuthorName")
    @Expose
    var authorName: String? = null
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