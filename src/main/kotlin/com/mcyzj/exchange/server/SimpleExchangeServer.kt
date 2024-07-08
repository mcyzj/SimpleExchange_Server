package com.mcyzj.exchange.server

import com.mcyzj.exchange.server.notice.NoticeServer
import org.json.simple.JSONObject
import com.mcyzj.exchange.server.socket.TCPServer
import com.mcyzj.exchange.server.voice.VoiceChatServer

class SimpleExchangeServer {
    companion object {
        lateinit var instance: SimpleExchangeServer
        var inStart: Boolean = false
        var start: Boolean = false
    }

    fun start() {
        if ((start).or(inStart)) {
            println("SimpleExchange已经初始化了！")
            return
        }
        inStart = true
        instance = this

        println("SimpleExchange启动语音聊天模块")
        NoticeServer(10001)

        println("SimpleExchange启动语音聊天模块")
        VoiceChatServer(10001)

        println("SimpleExchange完成启动")
        inStart = false
        start = true
    }
}