package com.strangenaut.networkclient.sender.data.repository

import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.sender.domain.repository.NetworkRepository
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import java.net.SocketException
import kotlin.random.Random

class NetworkRepositoryImpl : NetworkRepository {

    private var socket: DatagramSocket

    init {
        while (true) {
            try {
                val port = Random.nextInt(1025, 5000)

                socket = DatagramSocket(port)
                break
            } catch (e: SocketException) {
                e.printStackTrace()
            }
        }
    }

    override fun sendTcpPacket(ip: String, port: String, message: Message) {
        val socket = Socket(ip, port.toInt())

        try {
            val outputStream = socket.getOutputStream()

            outputStream.write(message.encodedPayload)
            outputStream.flush()

            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
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