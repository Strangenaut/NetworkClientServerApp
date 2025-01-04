package com.strangenaut.networkclient.sender.presentation

import com.strangenaut.networkclient.sender.domain.model.Message

sealed class PacketSenderEvent {

    data class UpdateConfiguration(
        val ip: String = "",
        val port: String = "",
        val numberOfPackets: Int = 1,
        val iat: Long = 0,
        val message: Message? = null
    ) : PacketSenderEvent()

    data object SendTcpPacket : PacketSenderEvent()

    data object SendUdpPacket : PacketSenderEvent()
}