package com.ids.inpoint.model

class PostVideo {
    var MediaId: Int?=null
    var typeId: Int?=null
    var link: String?=null
    var typeName:String?=null

    constructor(MediaId:Int,typeId: Int?, link: String?, typeName: String?) {
        this.MediaId=MediaId
        this.typeId = typeId
        this.link = link
        this.typeName = typeName
    }
}