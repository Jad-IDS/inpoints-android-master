package com.ids.inpoint.controller.Base

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.view.ContextThemeWrapper
import java.util.*

open class BaseActivityLang : AppCompatActivity() {

    companion object {
        public var dLocale: Locale? = null
    }

    init {
        updateConfig(this)
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale==Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
}