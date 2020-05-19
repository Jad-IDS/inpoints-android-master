package com.ids.inpoint.model

import android.graphics.Bitmap
import android.net.Uri
import java.net.URI


class PostMedia {
    var id: Int? = null
    var fileName: String? = ""
    var type: Int? = null
    var isLocal: Boolean? = true
    var uri: Uri? = null
    var bitmap: Bitmap? = null


    constructor(id: Int?, fileName: String?, type: Int?, isLocal: Boolean?) {
        this.id = id
        this.fileName = fileName
        this.type = type
        this.isLocal = isLocal
    }

    constructor(id: Int?, fileName: String?, type: Int?, isLocal: Boolean?, uri: Uri?, bitmap: Bitmap?) {
        this.id = id
        this.fileName = fileName
        this.type = type
        this.isLocal = isLocal
        this.uri = uri
        this.bitmap = bitmap
    }

    constructor()

}
