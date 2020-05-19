package com.ids.inpoint.model

open class ItemRectangularCard<T> {
    var id: Int = 0
    var title: String = ""
    var subtitle1: String = ""
    var subtitle2: String = ""
    var mainObject: T? = null

    constructor(
        id: Int = 0,
        title: String = "",
        subtitle1: String = "",
        subtitle2: String = "",
        mainObject: T? = null
    ){
        this.id = id
        this.title = title
        this.subtitle1 = subtitle1
        this.subtitle2 = subtitle2
        this.mainObject = mainObject
    }
}