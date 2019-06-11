package com.zll.format

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.lang.IllegalStateException

class ClazzGenerator(val settings: Settings?) {

    fun generate(name: String, string: String) = try {
        JsonParser().parse(string).let {
            when (it) {
                is JsonObject -> it.asJsonObject
                is JsonArray -> it.asJsonArray[0].asJsonObject
                else -> null
            }
        }.let { obj ->
            mutableListOf<Clazz>().let {
                Clazz(it, name, obj) to it
            }
        }.let { (clazz, clazzes) ->
            clazzes.reversed()
                    .map { printClazz(it == clazz, it, 0) }
                    .reduce { acc, s -> "$acc\n\n$s" }
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

    private fun printClazz(keepName: Boolean, clazz: Clazz, space: Int): String {
        val sb = StringBuilder()

        var spaceStr = ""
        repeat(space) { spaceStr += " " }

        val className = Util.toUpperCaseFirstOne((if (keepName) clazz.name else clazz.getClassName()))

        if (settings?.generateComments == true) {
            clazz.children?.map {
                "/// ${it.getComment()}\n"
            }?.forEach {
                sb.append(it)
            }
            sb.append("\n")
        }

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
        sb.append("$spaceStr    ").append("if (map == null) return null;")
        sb.append("\n")
        sb.append("$spaceStr    ").append(className).append(" ").append(clazz.getFieldName()).append(" = ").append(className).append("();")
        sb.append("\n")

        // 输出数据提取及转换
        clazz.children?.flatMap { it.getAssignments(clazz.getFieldName()) }?.filterNot { it.isEmpty() }?.map {
            "$spaceStr    $it\n"
        }?.forEach {
            sb.append(it)
        }

        // 输出 fromMap 尾
        sb.append("$spaceStr    return ${clazz.getFieldName()};")
        sb.append("\n")
        sb.append("$spaceStr  }")

        // 输出 toJson 头
        sb.append("\n")
        sb.append("\n")
        sb.append("$spaceStr  Map toJson() => {")
        sb.append("\n")

        // 输出数据提取及转换
        clazz.children?.map { "${it.getJsonAssignment()}," }?.map {
            "$spaceStr    $it\n"
        }?.forEach {
            sb.append(it)
        }

        // 输入 toJson 尾
        sb.append("$spaceStr  };")

        // 输出 class 尾
        sb.append("\n")
        sb.append(spaceStr).append("}")

        return sb.toString()
    }
}