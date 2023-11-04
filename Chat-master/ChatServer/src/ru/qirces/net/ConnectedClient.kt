package ru.qirces.net

import ChatDb
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.Socket
import java.util.Calendar
import java.time.LocalDateTime

/**
 * Организация работы с подключенным клиентом на стороне сервера
 * @param socket сокет подключенного клиента
 */
class ConnectedClient(
    private val socket: Socket,
) {
    companion object {
        private val _list = mutableListOf<ConnectedClient>()
        val list: List<ConnectedClient>
            get() = _list.toList()
    }

    private val cmn = Communicator(socket)
    private val db = ChatDb()

    private var name: String? = null
        private set(value) {
            value?.let { vl ->
                if (list.find { it.name == value } == null) {
                    field = vl
                    println("Пользователь $vl успешно вошел")
                    sendToAllConnectedClients(
                        { if (it == this) "NAMEOK" else "NEW" },
                        { if (it != this) vl else "" })
                } else cmn.sendData("REINTR:Пользователь уже авторизован")
            } ?: cmn.sendData("REINTR:Не указан логин")
        }

    init {
        _list.add(this)
    }

    suspend fun start() {
        coroutineScope {
            launch {
                try {
                    cmn.startReceiving { parse(it) }
                } catch (e: Throwable) {
                    _list.remove(this@ConnectedClient)
                    name?.let {
                        sendToAllConnectedClients({ "EXIT" }, { _ -> it })
                    }
                }
            }
            launch {
                cmn.sendData("INTR:")
            }
        }
    }

    private fun parse(data: String) {
        val str = data.split(":", limit = 2)
        str.forEach{println(it)}
        val date = LocalDateTime.now().toString()
        when (str[0]) {
            "MSG" -> {
                if (name != null) {
                    if(str[1].startsWith("/")){
                        val str2 = str[1].split(" ", limit = 2)
                        str2.forEach{println(it)}
                        val destin = str2[0].substring(1)
                        val message = str2[1]
                        sendToDestination({"MSG"}, {"${name} : Личное сообщение от ${name} : ${message}"}, destin)
                        sendToDestination({"MSG"}, {"${name} : Личное сообщение от вас к ${destin} : ${message}"}, "${name}")
                        //db.addHistory2(name!!, destin, date, message)
                    }
                    else
                    sendToAllConnectedClients(
                        { "MSG" },
                        {if (it != this) "${name} : ${name} : ${str[1]}" else "${name} : Вы : ${str[1]}"}
                    )
                    db.addHistory(name!!,date,str[1])
                    //db.addHistory2(name!!, "all", date, str[1])
                }
                else
                    cmn.sendData("INTR:")
            }

            "REG" -> {
                if (name != null) {
                    cmn.sendData("SYS:Вы уже авторизованы")
                    return
                }
                val logps = str[1].split(":", limit = 2)
                println("isFree" + db.isLoginFree(logps[0]))
                if (db.isLoginFree(logps[0])) {
                    db.addUser(logps[0], logps[1])
                    cmn.sendData("REGOK:Регистрация прошла успешно")
                    println("Зарегистрирован пользователь ${logps[0]}")
                } else cmn.sendData("REREG:Логин занят")
            }

            "LOG" -> {
                if (name != null) {
                    cmn.sendData("SYS:Вы уже авторизованы")
                    return
                }
                val loginPassword = str[1].split(":", limit = 2)
                if (db.checkUser(loginPassword[0], loginPassword[1]))
                    name = loginPassword[0]
                else
                    cmn.sendData("REINTR:Неверный логин или пароль")
            }

            else -> {
                cmn.sendData("SYS:Неизвестная команда")
            }
        }
    }

    private fun sendToAllConnectedClients(cmd: (ConnectedClient) -> String, data: (ConnectedClient) -> String) {
        _list.forEach {
            it.name?.let { _ ->
                it.cmn.sendData("${cmd(it)}:${data(it)}");
            }
        }
    }

    private fun sendToDestination(cmd: (ConnectedClient) -> String, data: (ConnectedClient) -> String, destin : String) {
        _list.forEach {
            if (it.name == destin) {
                it.name?.let { _ ->
                    it.cmn.sendData("${cmd(it)}:${data(it)}");
                }
            }
        }
    }

}