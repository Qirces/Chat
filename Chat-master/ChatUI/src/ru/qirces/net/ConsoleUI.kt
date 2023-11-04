package ru.qirces.net

import kotlinx.coroutines.*

//class ConsoleUI(override val send: (String) -> Unit) : ChatUI {
//
//    private val mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
//
//    init {
//        mainCoroutineScope.launch {
//            while (isActive) {
//                val s = readlnOrNull() ?: ""
//                send(s)
//            }
//        }
//    }
//
//    override fun parse(data: String) {
//        data.split(":", limit = 2).let {
//            when (it[0]) {
//                "INTR" -> {
//                    print("Представьте себя: ")
//                }
//
//                "REINTR" -> {
//                    print("Имя занято, выберите другое: ")
//                }
//
//                "NAMEOK" -> {
//                    println("Вы успешно вошли в чат")
//                }
//
//                "MSG" -> {
//                    println(it[1])
//                }
//
//                "NEW" -> {
//                    println("Пользователь ${it[1]} вошёл в чат")
//                }
//
//                "EXIT" -> {
//                    println("Пользователь ${it[1]} покинул чат")
//                }
//            }
//        }
//    }
//}