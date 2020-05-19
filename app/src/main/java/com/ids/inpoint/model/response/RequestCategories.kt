package com.ids.inpoint.model.response



import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RequestCategories {
    @SerializedName("PostId")
    var PostId : Int? = null

    @SerializedName("CategoryId")
    var CategoryId : Int? = null

    constructor(PostId: Int?, CategoryId: Int?) {
        this.PostId = PostId
        this.CategoryId = CategoryId
    }
}