package ru.qirces.net

interface ChatUI {
    fun parse(data: String)
    var send: ((String) -> Unit)?
}