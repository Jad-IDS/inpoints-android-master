package com.ids.inpoint.model


import com.google.gson.annotations.SerializedName
class Resources{
    var id: Int? = null
    var name: String? = null



    constructor(id: Int?, name: String?) {
        this.name = name
        this.id = id
    }
}