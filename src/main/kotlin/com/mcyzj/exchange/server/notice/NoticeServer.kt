package com.mcyzj.exchange.server.notice

import com.mcyzj.exchange.server.socket.ConnectData
import com.mcyzj.exchange.server.socket.TCPServer
import java.net.Socket

class NoticeServer(port: Int) {
    var server: TCPServer

    init {
        server = TCPServer(port)
    }

    fun accept() {
        while (true) {
            val connect = server.acceptConnect()
            Thread{
                val json = server.receiveJsonData(1024, connect)
                if (json == null) {
                    server.closeConnect(connect)
                    return@Thread
                }
                try {
                    if (json.get("type").asString != "connect") {
                        server.closeConnect(connect)
                        return@Thread
                    }
                    val token = json.get("token").asString
                    if (token != "Tree0123") {
                        server.closeConnect(connect)
                        return@Thread
                    }
                } catch (_: Exception) {
                    println(json)
                    server.closeConnect(connect)
                    return@Thread
                }
            }.start()
        }
    }

    fun handle(connect: ConnectData) {

    }
}