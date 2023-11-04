import kotlinx.coroutines.runBlocking
import ru.qirces.net.Server

fun main() = runBlocking {
    Server().start().join()
}