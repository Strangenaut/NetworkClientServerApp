package com.strangenaut.networkclient.sender.data.repository

import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.sender.domain.repository.NetworkRepository
import java.io.OutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import kotlin.random.Random

class NetworkRepositoryImpl : NetworkRepository {

    private val socket: DatagramSocket

    init {
        val randomPort = Random.nextInt(1025, 5000)
        socket = DatagramSocket(randomPort)
    }

    override fun sendTcpPacket(ip: String, port: String, message: Message) {
        val socket = Socket(ip, port.toInt())

        val outputStream: OutputStream = socket.getOutputStream()
        outputStream.write(message.encodedPayload)
        outputStream.flush()

        socket.close()
    }

    override fun sendUdpPacket(ip: String, port: String, message: Message) {
        val inetAddress = InetAddress.getByAddress(ip.ipStringToByteArray())
        val buffer = message.encodedPayload
        buffer.decodeToString()

        val packet = DatagramPacket(buffer, buffer.size, inetAddress, port.toInt())

        socket.send(packet)
    }

    private fun String.ipStringToByteArray(): ByteArray {
        return this.split('.')
            .map {
                it.toInt().toByte()
            }.toByteArray()
    }
}