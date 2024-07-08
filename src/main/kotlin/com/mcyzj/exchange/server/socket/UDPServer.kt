package com.mcyzj.exchange.server.socket

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

class UDPServer(val port: Int) {
    var socket: DatagramSocket
    var isclose = false

    init {
        try {
            socket = DatagramSocket(port)
        } catch (e: Exception) {
            println("无法绑定UDP端口 $port")
            throw e
        }
    }

    fun send(data: ByteArray, receiver: ConnectData) {
        if (isclose) {
            return
        }
        // 发送端
        try {
            // 构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。
            val packet = DatagramPacket(
                data, data.size,
                InetAddress.getByName(receiver.host), receiver.port
            )
            // 从此套接字发送数据报包
            socket.send(packet)
        } catch (e: SocketException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun receive(size: Int = 1024): ReceiveData? {
        if (isclose) {
            return null
        }
        // 接收端
        return try{
            //接收数据的buf数组并指定大小
            val buffer = ByteArray(size)
            //创建接收数据包，存储在buf中
            val packet = DatagramPacket(buffer, buffer.size)
            //接收操作
            socket.receive(packet)
            val data = packet.data // 接收的数据
            val receiveData = ReceiveData(
                ConnectData(
                    packet.address.hostAddress,
                    packet.port
                ),
                data
            )
            receiveData
        } catch (e: SocketException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun close() {
        socket.close()
        isclose = true
    }
}