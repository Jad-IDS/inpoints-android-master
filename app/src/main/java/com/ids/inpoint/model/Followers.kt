package com.ids.inpoint.model

import com.google.gson.annotations.SerializedName
class Followers {
    var name: String? = null
    var imageUrl: String? = null


    constructor(name: String?, imageUrl: String?) {
        this.name = name
        this.imageUrl = imageUrl
    }
}