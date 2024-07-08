package com.mcyzj.exchange.server

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println("启动SimpleExchange服务端")
            SimpleExchangeServer().start()
        }

    }

}