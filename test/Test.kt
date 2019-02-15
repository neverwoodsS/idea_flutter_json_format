import com.zll.format.ClassGenerator
import java.io.File

fun main() {
    val json = File("test/test.json").readText()
    println(ClassGenerator(generateComments = true, ignoreEmptyOrNull = false).generate("Temp", json))
}