package com.ids.inpoint.model

import com.google.gson.annotations.SerializedName
class ItemSpinner {
    var id: Int? = null
    var name: String? = null
    var icon: String? = null


    constructor(id: Int?, name: String?) {
        this.name = name
        this.id = id
    }

    constructor(id: Int?, name: String?, icon: String?) {
        this.id = id
        this.name = name
        this.icon = icon
    }


}