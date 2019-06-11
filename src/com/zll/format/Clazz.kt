package com.zll.format

import com.google.gson.JsonArray
import com.google.gson.JsonObject

abstract class Clazz(
    open val root: MutableList<Clazz>,
    open val name: String,
    open val content: Any?,
    open val children: List<Clazz>?
) {
    companion object {
        operator fun invoke(root: MutableList<Clazz>, name: String, any: Any?): Clazz {
            // 处理空值
            if (any == null || "null" == any.toString())
                return EmptyClazz(root, name, any, null)

            // 处理对象
            if (any is JsonObject)
                return ObjectClazz(root, name, any, json2Clazz(root, any))

            // 处理数组
            if (any is JsonArray) {
                return if (any.size() == 0) {
                    ListClazz(root, name, any, null, null)
                } else {
                    val temp = Clazz(root, name, any[0])
                    ListClazz(root, name, any, null, temp)
                }
            }

            // 处理基本类型
            if (any.isBoolean())
                return BaseClazz(root, "bool", name, any, null)

            if (any.isInt() || any.isLong())
                return BaseClazz(root, "int", name, any, null)

            if (any.isDouble() || any.isFloat())
                return BaseClazz(root, "double", name, any, null)

            // 都不匹配的情况，默认为 String 类型
            return  BaseClazz(root, "String", name, any, null)
        }

        fun json2Clazz(root: MutableList<Clazz>, jsonObject: JsonObject): List<Clazz> {
            val list = mutableListOf<Clazz>()
            for (o in jsonObject.entrySet()) {
                val entry = o as Map.Entry<*, *>
                list.add(Clazz(root, entry.key.toString(), entry.value))
            }
            return list
        }

        private fun Any.isInt() = toString().toIntOrNull() != null
        private fun Any.isLong() = toString().toLongOrNull() != null
        private fun Any.isDouble() = toString().toDoubleOrNull() != null
        private fun Any.isFloat() = toString().toFloatOrNull() != null
        private fun Any.isBoolean() = toString().let { it == "true" || it == "false" }
    }

    fun getStatement() = "${getClassName()} ${getCamelName()};"
    fun getFieldName() = Util.toLowerCaseFirstOne(getClassName())
    fun getCamelName() = name.split("_").reduce { acc, s -> "$acc${Util.toUpperCaseFirstOne(s)}" }
    fun getComment() = "$name : ${content.toString().replace("\n", "")}"
    fun getJsonAssignment() = "\"$name\": ${getCamelName()}"

    abstract fun getAssignments(parent: String): List<String>
    abstract fun getClassName(): String
    abstract fun map(obj: String): String
}

data class EmptyClazz(
    override val root: MutableList<Clazz>,
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?
) : Clazz(root, name, content, children) {

    override fun getClassName() = "dynamic"
    override fun getAssignments(parent: String) = listOf("$parent.${getCamelName()} = map['$name'];")
    override fun map(obj: String) = ""
}

data class BaseClazz(
    override val root: MutableList<Clazz>,
    val type: String,
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?
) : Clazz(root, name, content, children) {

    override fun getClassName() = type
    override fun getAssignments(parent: String) = listOf("$parent.${getCamelName()} = map['$name'];")
    override fun map(obj: String): String {
        return when (type) {
            "bool" -> "$obj.toString() == 'true'"
            "int" -> "int.tryParse($obj.toString())"
            "double" -> "double.tryParse($obj.toString())"
            else -> "$obj.toString()"
        }
    }
}

data class ObjectClazz(
    override val root: MutableList<Clazz>,
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?
) : Clazz(root, name, content, children) {
    init {
        root.add(this)
    }

    override fun getClassName() = "${Util.toUpperCaseFirstOne(name)}Bean"
    override fun getAssignments(parent: String) = listOf("$parent.${getCamelName()} = ${getClassName()}.fromMap(map['$name']);")
    override fun map(obj: String): String {
        return "${getClassName()}.fromMap($obj)"
    }
}

data class ListClazz(
    override val root: MutableList<Clazz>,
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?,
    val child: Clazz?
) : Clazz(root, name, content, children) {

    override fun getClassName() = "List<${child?.getClassName() ?: "dynamic"}>"

    override fun map(obj: String): String {
        return if (child == null || child is EmptyClazz) "List()..addAll($obj as List)"
        else "List()..addAll(($obj as List ?? []).map((${obj}o) => ${child.map("${obj}o")}))"
    }

    override fun getAssignments(parent: String): List<String> {
        return if (child == null || child is EmptyClazz) listOf("$parent.${getCamelName()} = map['$name'];")
        else listOf(
            "$parent.$name = List()..addAll(",
            "  (map['$name'] as List ?? []).map((o) => ${child.map("o")})",
            ");"
        )
    }
}