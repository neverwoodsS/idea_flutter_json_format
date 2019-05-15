package com.zll.format

import com.google.gson.JsonArray
import com.google.gson.JsonObject

abstract class Clazz(
    open val name: String,
    open val content: Any?,
    open val children: List<Clazz>?
) {
    companion object {
        operator fun invoke(name: String, any: Any?): Clazz {
            // 处理空值
            if (any == null || "null" == any.toString())
                return EmptyClazz(name, any, null)

            // 处理对象
            if (any is JsonObject)
                return ObjectClazz(name, any, json2Clazz(any))

            // 处理数组
            if (any is JsonArray) {
                return if (any.size() == 0) {
                    ListClazz(name, any, null)
                } else {
                    val temp = Clazz("placeholder", any[0])
                    val deep = diveIntoNestedList(any)
                    ListClazz(name, any, temp.children, deep)
                }
            }

            // 处理基本类型
            if (any.isBoolean())
                return BaseClazz("bool", name, any, null)

            if (any.isInt() || any.isLong())
                return BaseClazz("int", name, any, null)

            if (any.isDouble() || any.isFloat())
                return BaseClazz("double", name, any, null)

            // 都不匹配的情况，默认为 String 类型
            return  BaseClazz("String", name, any, null)
        }

        fun json2Clazz(jsonObject: JsonObject): List<Clazz> {
            val list = mutableListOf<Clazz>()
            for (o in jsonObject.entrySet()) {
                val entry = o as Map.Entry<*, *>
                list.add(Clazz(entry.key.toString(), entry.value))
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
}

data class EmptyClazz(
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?
) : Clazz(name, content, children)

data class BaseClazz(
    val type: String,
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?
) : Clazz(name, content, children)

data class ObjectClazz(
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?
) : Clazz(name, content, children)

data class ListClazz(
    override val name: String,
    override val content: Any?,
    override val children: List<Clazz>?,
    val deep: Int = 1 // 嵌套深度
) : Clazz(name, content, children)