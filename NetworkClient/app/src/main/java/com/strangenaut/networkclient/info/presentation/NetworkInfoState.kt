package com.strangenaut.networkclient.info.presentation

import com.strangenaut.networkclient.info.domain.model.ConnectivityInfo
import com.strangenaut.networkclient.info.domain.model.TelephonyInfo
import com.strangenaut.networkclient.info.domain.model.WifiInfo

data class NetworkInfoState(
    val connectivityInfo: ConnectivityInfo = ConnectivityInfo(),
    val wifiInfo: WifiInfo = WifiInfo(),
    val telephonyInfo: TelephonyInfo = TelephonyInfo(),
    val isUpdating: Boolean = false
)