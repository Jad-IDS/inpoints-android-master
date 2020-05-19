package com.ids.inpoint.model.response



import com.google.gson.annotations.SerializedName
class ItemSearchTab {
    var id: Int? = null
    var name: String? = null
    var selected:Boolean?=false


    constructor(id: Int?, name: String?, selected: Boolean?) {
        this.id = id
        this.name = name
        this.selected = selected
    }
}