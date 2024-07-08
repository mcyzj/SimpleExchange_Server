package com.mcyzj.exchange.server.socket

data class ReceiveData (
    val sender: ConnectData,
    val data: ByteArray
)