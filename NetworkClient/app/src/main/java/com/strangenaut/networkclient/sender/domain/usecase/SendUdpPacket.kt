package com.strangenaut.networkclient.sender.domain.usecase

import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.sender.domain.repository.NetworkRepository

class SendUdpPacket(private val repository: NetworkRepository) {

    operator fun invoke(ip: String, port: String, message: Message) {
        repository.sendUdpPacket(ip, port, message)
    }
}