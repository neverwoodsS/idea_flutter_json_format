package com.zll.format

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.lang.IllegalStateException

class ClazzGenerator(private val generateComments: Boolean, private val ignoreEmptyOrNull: Boolean) {

    fun generate(name: String, string: String) = try {
//        JsonParser().parse(string).let {
//            if (it is JsonObject)
//                it.asJsonObject
//            else if (it is JsonArray)
//                it.asJsonArray[0].asJsonObject
//            else null
//        }.let {
//            Clazz(name, it)
//        }.let {
//            printClazz(it, 0)
//        }
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

    fun printClazz(keepName: Boolean, clazz: Clazz, space: Int): String {
        val commentSb = StringBuilder()
        val sb = StringBuilder()

        val tempClazzes = mutableListOf<Clazz>()

        var spaceStr = ""
        repeat(space) { spaceStr += " " }

        val className = Util.toUpperCaseFirstOne((if (keepName) clazz.name else clazz.getClassName()))

        // 输出 class 头
        sb.append(spaceStr).append("class ").append(className).append(" {")
        sb.append("\n")

        // 输出属性声明
        clazz.children?.map {
            "$spaceStr  ${it.getStatement()}\n"
        }?.forEach {
            sb.append(it)
        }

        // 输出 fromMap 头
        sb.append("\n")
        sb.append("$spaceStr  static ").append(className).append(" fromMap(Map<String, dynamic> map) {")
        sb.append("\n")

        // 输出 fromMap 尾
        sb.append("$spaceStr  }")

        // 输出 class 尾
        sb.append("\n")
        sb.append(spaceStr).append("}")

        return sb.toString()
    }
}