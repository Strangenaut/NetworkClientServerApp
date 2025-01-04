package com.strangenaut.networkclient.info.domain.usecase

data class NetworkInfoUseCases(
    val getConnectivityInfo: GetConnectivityInfo,
    val getWifiInfo: GetWifiInfo,
    val getTelephonyInfo: GetTelephonyInfo
)