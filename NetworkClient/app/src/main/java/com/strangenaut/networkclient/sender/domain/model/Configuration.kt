package com.strangenaut.networkclient.sender.domain.model

data class Configuration(
    val ip: String = "",
    val port: String = "",
    val numberOfPackets: Int = 1,
    val iat: Long = 0,
    val message: Message? = null
)