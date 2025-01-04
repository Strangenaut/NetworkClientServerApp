package com.strangenaut.networkclient.info.domain.util

import java.net.InetAddress
import java.nio.ByteBuffer

fun Int.toIpString(): String {
    val bytes = ByteBuffer.allocate(4).putInt(this).array()
    return InetAddress.getByAddress(bytes).hostAddress ?: ""
}