import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.zll.format.ClassGenerator
import java.io.File

fun main() {
//    val json = File("test/test.json").readText()
    val obj = JsonObject().apply {
//        addProperty("test0", "3")
//        addProperty("test1", false)
//        addProperty("test2", 2)

        val array1 = JsonArray()
        val array2 = JsonArray()
        val array3 = JsonArray()
        val array4 = JsonArray()
        array1.add(array2)
        array2.add(array3)
        array2.add(array4)
        array3.add(1)
        array3.add(2)
        array4.add(3)
        array4.add(4)
        add("array", array1)
    }


    val json = obj.toString()  //"{\"test\":\"3\"}"
    println(ClassGenerator(generateComments = true, ignoreEmptyOrNull = false).generate("Temp", json))
}