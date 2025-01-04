package com.strangenaut.networkclient.sender.domain.usecase

import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.sender.domain.repository.NetworkRepository

class SendTcpPacket(private val repository: NetworkRepository) {

    operator fun invoke(ip: String, port: String, message: Message) {
        repository.sendTcpPacket(ip, port, message)
    }
}