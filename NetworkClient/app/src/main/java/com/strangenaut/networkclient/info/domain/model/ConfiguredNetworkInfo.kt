package com.strangenaut.networkclient.info.domain.model

data class ConfiguredNetworkInfo(
    val networkId: String = "",
    val ssid: String = "",
    val bssid: String = "",
    val priority: String = ""
)