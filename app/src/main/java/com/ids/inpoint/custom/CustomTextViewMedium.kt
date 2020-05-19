package com.ids.inpoint.custom


import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import com.ids.inpoint.utils.AppHelper


class CustomTextViewMedium : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()

    }

    private fun init() {
        if (!isInEditMode)
            typeface = AppHelper.getTypeFace(context)

    }

}
