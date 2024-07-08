package com.mcyzj.exchange.server.socket

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException


class TCPServer(val port: Int) {
    var socket: ServerSocket
    var isclose = false
    val connection = HashMap<ConnectData, Socket>()

    init {
        socket = ServerSocket(port)
    }

    fun send(data: ByteArray, sender: ConnectData) {
        if (isclose) {
            return
        }
        try {
            val connect = connection[sender] ?: return
            if (connect.isClosed) {
                closeConnect(sender)
                return
            }
            val outputStream = connect.getOutputStream()
            outputStream.write(data)
        } catch (_: Exception) {
            closeConnect(sender)
        }
    }

    fun receive(size: Int = 1024, receiver: ConnectData): ReceiveData? {
        if (isclose) {
            return null
        }
        return try {
            val connect = connection[receiver] ?: return null

            if (connect.isClosed) {
                closeConnect(receiver)
                return null
            }

            val inputStream = connect.getInputStream()
            val data = ByteArray(1024)
            inputStream.read(data)
            val receiveData = ReceiveData(
                receiver,
                data
            )
            return receiveData
        } catch (e: SocketException) {
            e.printStackTrace()
            closeConnect(receiver)
            null
        } catch (e: IOException) {
            e.printStackTrace()
            closeConnect(receiver)
            null
        }
    }

    fun receiveJsonData(size: Int = 1024, receiver: ConnectData): JsonObject? {
        val data = receive(size, receiver) ?: return null
        return try {
            val str = String(data.data, 0, data.data.size)
            val json = Gson()
            json.fromJson(str, JsonObject::class.java)
        } catch (_:Exception) {
            null
        }
    }

    fun acceptConnect(): ConnectData {
        val connect = socket.accept()

        val connectData = ConnectData(
            connect.inetAddress.hostAddress,
            connect.port
        )
        connection[connectData] = connect
        return connectData
    }

    fun closeConnect(connectData: ConnectData) {
        val connect = connection[connectData] ?: return
        if (!connect.isClosed) {
            connect.close()
        }
        connection.remove(connectData)
    }
}

