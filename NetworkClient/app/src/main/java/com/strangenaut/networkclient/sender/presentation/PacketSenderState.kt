package com.strangenaut.networkclient.sender.presentation

import com.strangenaut.networkclient.sender.domain.model.Configuration

data class PacketSenderState(
    val configuration: Configuration? = null
)