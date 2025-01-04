package com.strangenaut.networkclient.info.domain.model

data class ConnectivityInfo(
    val activeNetworkType: String = "Unknown",
    val allNetworkTypes: List<String> = listOf()
)
