package com.ids.inpoint.model

class ItemSpinnerTextImage{
    var id: Int? = null
    var text: String? = null
    var image: String? = null
    var isSelected: Boolean = false

    constructor(id: Int?, text: String?, image: String? = null, isSelected: Boolean = false) {
        this.id = id
        this.text = text
        this.image = image
        this.isSelected = isSelected
    }
}