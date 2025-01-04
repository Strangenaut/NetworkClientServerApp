package com.strangenaut.networkclient.sender.domain.repository

import com.strangenaut.networkclient.sender.domain.model.Message

interface NetworkRepository {

    fun sendTcpPacket(ip: String, port: String, message: Message)

    fun sendUdpPacket(ip: String, port: String, message: Message)
}