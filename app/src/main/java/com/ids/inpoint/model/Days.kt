package com.ids.inpoint.model

class Days {
    var id: Int?=null
    var translatedName: String?=null
    var sentName: String?=null

    constructor(id: Int?, translatedName: String?, sentName: String?) {
        this.id = id
        this.translatedName = translatedName
        this.sentName = sentName
    }
}