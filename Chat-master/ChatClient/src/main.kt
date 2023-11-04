import kotlinx.coroutines.runBlocking
import ru.qirces.net.Client
import ru.qirces.net.Communicator
import ru.qirces.net.GUI
import java.net.Socket

fun main() = runBlocking {
    val cmn = Communicator(Socket("localhost", 5004))
    val chatUI = GUI()
    Client(cmn, chatUI).start().join()
}