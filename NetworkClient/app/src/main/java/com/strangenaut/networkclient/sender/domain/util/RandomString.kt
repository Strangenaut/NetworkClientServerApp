package com.strangenaut.networkclient.sender.domain.util

import kotlin.random.Random

fun randomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val seed = System.currentTimeMillis()
    val random = Random(seed)

    return (1..length)
        .map { chars[random.nextInt(chars.length)] }
        .joinToString("")
}