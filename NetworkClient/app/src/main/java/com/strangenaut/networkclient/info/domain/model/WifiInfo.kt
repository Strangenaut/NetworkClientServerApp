package com.strangenaut.networkclient.info.domain.model

data class WifiInfo(
    val noWifiConnection: Boolean = false,
    val ip: String = "",
    val mac: String = "",
    val linkSpeed: String = "",
    val ssid: String = "",
    val bssid: String = "",
    val rssi: String = "",
    val dhcpInfo: String = "",
    val configuredNetworks: List<ConfiguredNetworkInfo> = listOf()
)