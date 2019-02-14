import com.zll.format.ClassGenerator
import java.io.File

fun main(args: Array<String>) {
    val json = File("test/test.json").readText()
    println(ClassGenerator(true).generate("Temp", json))
}