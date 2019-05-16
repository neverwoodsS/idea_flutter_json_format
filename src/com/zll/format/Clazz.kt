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
                    val deep = diveIntoNestedList(any)
                    ListClazz(root, name, any, null, temp, deep)
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

        fun diveIntoNestedList(array: JsonArray): Int {
            var count = 0

            var temp = array
            while (true) {
                count++

                if (temp.size() == 0 || temp.get(0) !is JsonArray) {
                    break
                }

                temp = temp.get(0) as JsonArray
            }

            return count
        }

        private fun Any.isInt() = toString().toIntOrNull() != null
        private fun Any.isLong() = toString().toLongOrNull() != null
        private fun Any.isDouble() = toString().toDoubleOrNull() != null
        private fun Any.isFloat() = toString().toFloatOrNull() != null
        private fun Any.isBoolean() = toString().let { it == "true" || it == "false" }
    }

    fun getStatement() = "${getClassName()} $name;"
    fun getFieldName() = Util.toLowerCaseFirstOne(getClassName())

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
    override fun getAssignments(parent: String) = listOf("$parent.$name = map['$name'];")
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
    override fun getAssignments(parent: String) = listOf("$parent.$name = map['$name'];")
    override fun map(obj: String): String {
        return when (type) {
            "bool" -> "$obj.toString() == 'true'"
            "int" -> "int.parse($obj.toString())"
            "double" -> "double.parse($obj.toString())"
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
    override fun getAssignments(parent: String) = listOf("$parent.$name = ${getClassName()}.fromMap(map['$name'])")
    override fun map(obj: String): String {
        return "${getClassName()}.fromMap($obj)"
    }
}

data class ListClazz(
    override val root: MutableList<Clazz>,
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?,
    val child: Clazz?,
    val deep: Int = 1 // 嵌套深度
) : Clazz(root, name, content, children) {

    override fun getClassName() = "List<${child?.getClassName() ?: "dynamic"}>"

    override fun map(obj: String): String {
        return if (child == null) "List()..addAll($obj as List)"
        else "List()..addAll(($obj as List).map((${obj}o) => ${child.map("${obj}o")}))"
    }

    override fun getAssignments(parent: String): List<String> {
        return listOf(
            "$parent.$name = List()..addAll(",
            "  (map['$name'] as List).map((o) => ${child?.map("o")})",
            ");"
        )
    }
}