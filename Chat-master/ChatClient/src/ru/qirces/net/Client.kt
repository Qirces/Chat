package ru.qirces.net

import kotlinx.coroutines.*

class Client(
    private val cmn: Communicator,
    private val chatUI: ChatUI
) {
    private val mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val parse = chatUI::parse
    private val send = cmn::sendData

    fun start() = mainCoroutineScope.launch {
        chatUI.send = send
        launch {
            cmn.startReceiving {
                parse(it)
            }
        }
    }
}