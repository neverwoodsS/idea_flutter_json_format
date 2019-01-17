package com.zll.format

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.lang.IllegalStateException
import java.util.HashMap

class ClassGenerator {
    val classes = mutableMapOf<String, List<Param>>()

    fun generate(string: String): String {
        return try {
            val fields = Param.json2Params(JsonParser().parse(string).asJsonObject)
            "class Temp {\n${printClassWithParams(fields, 2, "Temp")}\n}\n${buildClasses()}"
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

    private fun printClassWithParams(params: List<Param>, space: Int, className: String): String {
        val sb = StringBuilder()

        val tempClasses = HashMap<String, List<Param>>() // 统计子类

        var spaceStr = ""
        repeat(space) { spaceStr += " " }

        /* 基本类型参数声明与统计 **/
        val orderedList = params
                .filter { it.key == "String" || it.key == "int" || it.key == "double" || it.key == "bool" }
                .sortedBy { it.key }
                .map {
                    sb.append("$spaceStr${it.key} ${it.value};\n")
                    it.value
                }

        /* 对象类型参数声明与统计 **/
        val objectList = params
                .filter { it.key == "object" }
                .sortedBy { it.value }
                .map {
                    val clazzName = Util.toUpperCaseFirstOne(it.value + "Bean")
                    classes[clazzName] =  it.clazz
                    tempClasses[clazzName] = it.clazz
                    sb.append(spaceStr).append(clazzName).append(" ").append(it.value).append(";").append("\n")
                    NameValuePair(clazzName, it.value)
                }

        /* 基本类型 list 参数声明与统计 **/
        val listBaseList = params
                .filter { it.key.startsWith("List<") }
                .sortedBy { it.value }
                .map {
                    sb.append(spaceStr).append(it.key).append(" ").append(it.value).append(";").append("\n")
                    NameValuePair(it.key, it.value)
                }

        /* 对象类型 list 参数声明与统计 **/
        val listList = params
                .filter { "list" == it.key }
                .sortedBy { it.value }
                .map {
                    val clazzName = Util.toUpperCaseFirstOne(it.value + "ListBean")
                    classes[clazzName] = it.clazz
                    tempClasses[clazzName] = it.clazz
                    sb.append(spaceStr).append("List<").append(clazzName).append(">").append(" ").append(it.value).append(";").append("\n")
                    NameValuePair(clazzName, it.value)
                }

        val tempSpaceStr = "$spaceStr  "

        /* map.value 转换为对象的静态函数 start **/
        val fieldName = Util.toLowerCaseFirstOne(className)
        sb.append("\n").append(spaceStr)
                .append("static ").append(className).append(" fromMap").append("(Map<String, dynamic> map) {")
                .append("\n").append(tempSpaceStr)
                .append(className).append(" ").append(fieldName).append(" = new ").append(className).append("();")

        orderedList.forEach {
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(it).append(" = ").append("map['").append(it).append("'];")
        }

        objectList.forEach {
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(it.value).append(" = ").append(it.name).append(".fromMap(map['").append(it.value).append("']);")
        }

        listList.forEach {
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(it.value).append(" = ").append(it.name).append(".fromMapList(map['").append(it.value).append("']);")
        }

        /* map.value 转换为基础类型 list start **/
        for ((count, pair) in listBaseList.withIndex()) {
            sb.append("\n")
            sb.append("\n").append(tempSpaceStr).append("List<dynamic> dynamicList").append(count).append(" = map['").append(pair.value).append("'];")
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(pair.value).append(" = new List();")

            var function = "o.toString()"
            when (pair.name) {
                "List<int>" -> function = "int.parse(o.toString())"
                "List<double>" -> function = "double.parse(o.toString())"
                "List<bool>" -> function = "o.toString() == 'true'"
            }

            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(pair.value).append(".addAll(dynamicList").append(count).append(".map((o) => ").append(function).append("));")
            sb.append("\n")
        }
        /* map.value 转换为基础类型 list end **/

        sb.append("\n").append(tempSpaceStr).append("return ").append(fieldName).append(";\n")
        sb.append(spaceStr).append("}\n")
        /* map.value 转换为对象的静态函数 end **/

        /* map.value 转换为 list 的静态函数 start **/
        sb.append("\n").append(spaceStr)
                .append("static ").append("List<").append(className).append(">").append(" fromMapList").append("(dynamic mapList) {")
                .append("\n").append(tempSpaceStr).append("List<").append(className).append("> list = new List(mapList.length);")
                .append("\n").append(tempSpaceStr).append("for (int i = 0; i < mapList.length; i++) {")
                .append("\n").append(tempSpaceStr).append("  ").append("list[i] = fromMap(mapList[i]);")
                .append("\n").append(tempSpaceStr).append("}")
                .append("\n").append(tempSpaceStr).append("return list;")
                .append("\n").append(spaceStr).append("}")
                .append("\n")
        /* map.value 转换为 list 的静态函数 end **/

        // 遍历类中类（主要目的是添加进 classes 统计，而不 append）
        // 还是由于 dart 不支持内部类导致的
        tempClasses.forEach { key, value ->
            printClassWithParams(value, space + 2, key)
        }

        return sb.toString()
    }

    private fun buildClasses(): String {
        val sb = StringBuilder()

        // 开始定义类
        classes.forEach { key, value ->
            sb.append("\n")
                    .append("class").append(" ").append(key).append(" ").append("{").append("\n")
                    .append(printClassWithParams(value, 2, key))
                    .append("}").append("\n")
        }

        return sb.toString()
    }
}