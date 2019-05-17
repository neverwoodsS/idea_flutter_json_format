package com.zll.format

import com.intellij.ide.util.PropertiesComponent

class Settings {

    companion object {
        private const val KEY_COMMENT = "dart_json_format_comment"
    }

    var generateComments: Boolean

    init {
        val propertiesComponent = PropertiesComponent.getInstance()
        generateComments = propertiesComponent.getBoolean(KEY_COMMENT, true)
    }

    fun save() = PropertiesComponent.getInstance().apply {
        setValue(KEY_COMMENT, generateComments.toString())
    }
}