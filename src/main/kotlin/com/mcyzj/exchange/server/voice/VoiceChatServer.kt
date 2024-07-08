package com.mcyzj.exchange.server.voice

import com.mcyzj.exchange.server.socket.ConnectData
import com.mcyzj.exchange.server.socket.UDPServer
import java.lang.Thread.sleep

class VoiceChatServer(val port: Int) {
    companion object {
        lateinit var server: UDPServer
        lateinit var chatThread: Thread
        var isChatStart = false
    }
    val connection = ArrayList<ConnectData>()

    var packetNumber = 0

    init {
        server = UDPServer(port)
        println("启动语音聊天服务器于 ${server.socket.localSocketAddress} 上")
        chatThread = Thread{
            chat()
        }
        chatThread.start()

        Thread {
            while (true) {
                println(packetNumber)
                packetNumber = 0
                sleep(1000)
            }
        }.start()
    }

    fun chat() {
        if (isChatStart) {
            return
        }

        isChatStart = true

        while (true) {
            val data = server.receive()
            packetNumber ++
            if (data == null) {
                println("聊天数据接收失败，三秒后尝试重新连接")
                continue
            }
            Thread {
                if (data.sender !in connection) {
                    connection.add(data.sender)
                }
                for (connect in connection) {
                    Thread {
                        if (connect == data.sender) {
                            server.send(data.data, connect)
                        }
                    }.start()
                }
            }.start()
        }
    }
}