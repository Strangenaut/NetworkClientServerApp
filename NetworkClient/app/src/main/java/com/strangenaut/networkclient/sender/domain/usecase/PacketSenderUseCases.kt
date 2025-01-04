package com.strangenaut.networkclient.sender.domain.usecase

data class PacketSenderUseCases (
    val loadConfiguration: LoadConfiguration,
    val saveConfiguration: SaveConfiguration,
    val sendTcpPacket: SendTcpPacket,
    val sendUdpPacket: SendUdpPacket
)