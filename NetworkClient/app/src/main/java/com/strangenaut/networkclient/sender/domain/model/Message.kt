package com.strangenaut.networkclient.sender.domain.model

data class Message(
    val payload: String = ""
) {

    val encodedPayload: ByteArray = payload.toByteArray()
}
