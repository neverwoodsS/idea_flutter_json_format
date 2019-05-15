package com.zll.format

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.lang.IllegalStateException

class ClazzGenerator(private val generateComments: Boolean, private val ignoreEmptyOrNull: Boolean) {
    val clazzes = mutableMapOf<String, List<Clazz>>()

    fun generate(name: String, string: String) = try {
        JsonParser().parse(string).let {
            if (it is JsonObject)
                it.asJsonObject
            else if (it is JsonArray)
                it.asJsonArray[0].asJsonObject
            else null
        }.let {
            Clazz(name, it)
        }
    } catch (jsonParseException: JsonParseException) {
        jsonParseException.printStackTrace()
        "error: not supported json"
    } catch (illegalStateException: IllegalStateException) {
        illegalStateException.printStackTrace()

        if (illegalStateException.message?.startsWith("Not a JSON Object") == true) {
            "error: not supported json"
        } else {
            "error: unknown"
        }
    }
}